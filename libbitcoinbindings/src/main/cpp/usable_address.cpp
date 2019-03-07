//
//  usable_address.cpp
//  android-libbitcoin
//
//  Created by Dan Sexton on 5/17/18.
//  Copyright © 2018 Coin Ninja, LLC. All rights reserved.
//

#include "usable_address.hpp"
#include "misc.hpp"

usable_address::usable_address(wallet::hd_private privateKey, derivation_path path) {
    this->privateKey = privateKey;
    this->path = path;
    if (path.hasCoin() && path.getCoin() == 1) {
        testnet = true;
    }
}

wallet::payment_address usable_address::buildPaymentAddress() {
    return paymentAddressForHardenedDerivationPath(privateKey, path);
}

ec_compressed usable_address::buildCompressedPublicKey() {
    return compressedPublicKeyForHardenedDerivationPath(privateKey, path);
}

data_chunk usable_address::getUncompressedPublicKey() {
    return uncompressedPublicKeyForHardenedDerivationPath(privateKey, path);
}

script usable_address::buildP2WPKH() {
    return P2WPKHForHardenedDerivationPath(privateKey, path);
}

wallet::hd_private usable_address::buildPrivateKey() {
    return indexPrivateKeyForHardenedDerivationPath(privateKey, path);
}

void signForInput(transaction &tx, int inputIndex, uint64_t amount,
                  usable_address usableAddress) {
    //Make Signature
    script script_code = script::to_pay_key_hash_pattern(
            bitcoin_short_hash(usableAddress.buildCompressedPublicKey()));
    endorsement sig;
    script().create_endorsement(sig, usableAddress.buildPrivateKey().secret(), script_code, tx,
                                static_cast<uint32_t>(inputIndex), sighash_algorithm::all,
                                script_version::zero, amount);

    data_chunk scriptChunk = to_chunk(usableAddress.buildP2WPKH().to_data(true));
    tx.inputs()[inputIndex].set_script(script(scriptChunk, false));
    //Make Witness
    data_stack witness_data{sig, to_chunk(usableAddress.buildCompressedPublicKey())};
    tx.inputs()[inputIndex].set_witness(witness(witness_data));
}
