//
//  misc.cpp
//  android-libbitcoin
//
//  Created by Dan Sexton on 5/17/18.
//  Copyright © 2018 Coin Ninja, LLC. All rights reserved.
//

#include "misc.hpp"

void createPayToScriptOutputFrom(transaction &tx, wallet::payment_address address, uint64_t amount) {
    tx.outputs().push_back(
            output(amount, script(script().to_pay_script_hash_pattern(address.hash()))));
}

void createPayToKeyOutputFrom(transaction &tx, wallet::payment_address address, uint64_t amount) {
    tx.outputs().push_back(
            output(amount, script(script().to_pay_key_hash_pattern(address.hash()))));
}

operation::list witnessProgram(ec_compressed publicKey) {
    short_hash KeyHash = bitcoin_short_hash(publicKey);
    return {operation(opcode(0)), operation(to_chunk(KeyHash))};
}

wallet::hd_private childPrivateKey(wallet::hd_private privateKey, int index) {
    return privateKey.derive_private(index);
}

wallet::hd_private indexPrivateKeyForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path) {

    wallet::hd_private purposePrivateKey = childPrivateKey(privateKey, path.getHardenedPurpose());
    wallet::hd_private coinPrivateKey = childPrivateKey(purposePrivateKey, path.getHardenedCoin());
    wallet::hd_private accountPrivateKey = childPrivateKey(coinPrivateKey,
                                                           path.getHardenedAccount());
    wallet::hd_private changePrivateKey = childPrivateKey(accountPrivateKey, path.getChange());
    return childPrivateKey(changePrivateKey, path.getIndex());
}

ec_compressed compressedPublicKeyForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path) {
    wallet::hd_private indexPrivateKey = indexPrivateKeyForHardenedDerivationPath(privateKey, path);
    wallet::hd_public indexPublicKey = indexPrivateKey.to_public();
    return indexPublicKey.point();
}

data_chunk uncompressedPublicKeyForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path) {
    wallet::hd_private indexPrivateKey = indexPrivateKeyForHardenedDerivationPath(privateKey, path);
    wallet::hd_public indexPublicKey = indexPrivateKey.to_public();
    wallet::ec_public compressed = wallet::ec_public(indexPublicKey);
    bc::ec_uncompressed uncompressed;
    compressed.to_uncompressed(uncompressed);
    return to_chunk(uncompressed);
}

script P2WPKHForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path) {
    return script(witnessProgram((compressedPublicKeyForHardenedDerivationPath(privateKey, path))));
}

wallet::payment_address paymentAddressForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path) {
    ec_compressed compressedPublicKey = compressedPublicKeyForHardenedDerivationPath(privateKey,
                                                                                     path);

    //TODO: a derivation path is either testnet or mainnet. A libbitcoin is also either testnet or mainnet.
    //should there be gaurdrails?
    uint8_t format = wallet::payment_address::mainnet_p2sh;
    if (path.hasCoin() && path.getCoin() == 1) {
        format = wallet::payment_address::testnet_p2sh;
    }

    script P2WPKH = script(witnessProgram(compressedPublicKey));
    return wallet::payment_address(P2WPKH, format);
}

void createInputFrom(transaction &tx, chain::point_value utxo, bool replaceable) {
    input workingInput = input();
    workingInput.set_previous_output(output_point(utxo));
    if(replaceable){
        workingInput.set_sequence(0);
    } else{
        workingInput.set_sequence(max_input_sequence);
    }
    tx.inputs().push_back(workingInput);
}
