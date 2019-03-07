//
//  hd_wallet.cpp
//  android-libbitcoin
//
//  Created by Dan Sexton on 5/17/18.
//  Copyright © 2018 Coin Ninja, LLC. All rights reserved.
//

#include "hd_wallet.hpp"
#include "misc.hpp"
#include "usable_address.hpp"

HD_Wallet::HD_Wallet(const wallet::word_list mnemonicSeed, bool _isTestnet) {
    testnet = _isTestnet;
    seed = to_chunk(wallet::decode_mnemonic(mnemonicSeed));
    mnemonic = mnemonicSeed;
    privateKeyM = initPrivateKey(seed);
}

HD_Wallet::HD_Wallet(bool _isTestnet) {
    testnet = _isTestnet;
    entropy = data_chunk(16);
    pseudo_random_fill(entropy);
    mnemonic = wallet::create_mnemonic(entropy);
    seed = to_chunk(wallet::decode_mnemonic(mnemonic));
    privateKeyM = initPrivateKey(seed);
}

wallet::word_list HD_Wallet::getMNemonic() {
    return mnemonic;
}

std::string HD_Wallet::getExternalChangeAddress(int index) {
    return usable_address(privateKeyM, derivation_path(49, testnet, 0, 0,
                                                        index)).buildPaymentAddress().encoded();
};

std::string HD_Wallet::getInternalChangeAddress(int index) {
    return usable_address(privateKeyM, derivation_path(49, testnet, 0, 1,
                                                        index)).buildPaymentAddress().encoded();
};

std::string HD_Wallet::getCoinNinjaSigningKey() {
    return encode_base16(privateKeyM.derive_private(42).to_public().point());
}

wallet::hd_private HD_Wallet::getCoinNinjaPrivateSigningKey() {
    return privateKeyM.derive_private(42);
}

bool HD_Wallet::isTestnet() {
    return testnet;
}

std::string HD_Wallet::broadcastTx() {
    //TODO: 2. test handle network connection failure
    //TODO: 3. test handle broadcast failure (invalid sig, already consumed utxo's, etc)
    //TODO: 4. test happy-path through mocked out lib.


    client::connection_type connection = {};
    connection.retries = 3;
    connection.timeout_seconds = 8;

    const char *testnetUrl = "tcp://testnet3.libbitcoin.net:19091";
    const char *mainnetUrl = "tcp://libbitcoin.coinninja.com:9091";

    connection.server = testnet ? config::endpoint(testnetUrl) : config::endpoint(mainnetUrl);
    client::obelisk_client client(connection);

    if (!client.connect(connection)) {
        return "Could Not Connect to Client;";
    }

    std::string broadcastTxMessage = "Received Error Code: Completion handler never invoked";
    int broadcastTxResponseCode = 0;

    client::proxy::result_handler on_done = [&broadcastTxMessage, &broadcastTxResponseCode](const code &ec) {
        std::string message = "Success: ";
        message.append(ec.message());

        broadcastTxMessage = message;
        broadcastTxResponseCode = ec.value();
    };

    client::dealer::error_handler on_error = [&broadcastTxMessage, &broadcastTxResponseCode](const code &ec) {
        std::string errors = "Received Error Code: ";
        errors.append(ec.message());

        broadcastTxMessage = errors;
        broadcastTxResponseCode = ec.value();
    };

    client.transaction_pool_broadcast(on_error, on_done, tx);
    client.wait();
   return broadcastResultToJSON(broadcastTxMessage, broadcastTxResponseCode);
}

//todo: this isn't safe
hash_digest HD_Wallet::stringToDigest(const char *message) {
    return bitcoin_hash(stringToDataChunk(message));
}

data_chunk HD_Wallet::stringToDataChunk(const char *message) {
    const uint8_t *bytes = (const uint8_t *) message;
    data_chunk chunk;
    data_sink ostream(chunk);
    ostream_writer sink(ostream);
    sink.write_bytes(bytes, (size_t) strlen(message));
    ostream.flush();

    return chunk;
}

std::string HD_Wallet::signMessage(const char *message) {

    hash_digest messageHash = stringToDigest(message);

    ec_signature signature;
    if (sign(signature, getCoinNinjaPrivateSigningKey().secret(), messageHash)) {
        der_signature derSignature;
        if (encode_signature(derSignature, signature)) {
            return encode_base16(derSignature);
        }
    }
    return "";
}

void HD_Wallet::populatePaymentOutput(const char *paymentAddress, jlong amount){
    wallet::payment_address toAddress = wallet::payment_address(paymentAddress);

    if (toAddress.version() == wallet::payment_address::mainnet_p2kh ||
            toAddress.version() == wallet::payment_address::testnet_p2kh) {
        createPayToKeyOutputFrom(tx, toAddress, static_cast<uint64_t>(amount));
    } else if (toAddress.version() == wallet::payment_address::mainnet_p2sh ||
                toAddress.version() == wallet::payment_address::testnet_p2sh) {
        createPayToScriptOutputFrom(tx, toAddress, static_cast<uint64_t>(amount));
    } else {
        throw "Illegal Payment Address";
    }
}

void HD_Wallet::populateTransaction(const char *paymentAddress, jlong amount, jlong fees, jlong changeAmount,
                    jint *derivationPath) {
    tx.set_version(1u);

    populatePaymentOutput(paymentAddress, amount);

    usable_address change(privateKeyM, derivation_path(derivationPath[0], derivationPath[1],
                                                        derivationPath[2], derivationPath[3],
                                                        derivationPath[4]));

    if(changeAmount >= 1) {
        createPayToScriptOutputFrom(tx, change.buildPaymentAddress(),
                                    static_cast<uint64_t>(changeAmount));
    }
}

data_chunk HD_Wallet::getUncompressedPublicKey(jint *derivationPath) {

    usable_address address(privateKeyM, derivation_path(derivationPath[0], derivationPath[1],
                                                        derivationPath[2], derivationPath[3],
                                                        derivationPath[4]));

    return address.getUncompressedPublicKey();
}

wallet::hd_private HD_Wallet::getPrivateKey(jint *derivationPath) {

    usable_address address(privateKeyM, derivation_path(derivationPath[0], derivationPath[1],
                                                        derivationPath[2], derivationPath[3],
                                                        derivationPath[4]));

    return address.buildPrivateKey();
}

void HD_Wallet::addUtxo(const char *txId_, jlong amount, jint index, jboolean replaceable) {
    char txId[65];
    strcpy(txId, txId_);

    chain::point utxoPoint(hash_literal(txId), static_cast<uint32_t>(index));

    chain::point_value utxo(utxoPoint, static_cast<uint64_t>(amount));

    createInputFrom(tx, utxo, (bool)(replaceable == JNI_TRUE));

}

void HD_Wallet::signInput(jint inputIndex, const char *txId_, jlong amount, jint *derivationPath) {
    usable_address signing_address(privateKeyM,
                                    derivation_path(derivationPath[0], derivationPath[1],
                                                    derivationPath[2], derivationPath[3],
                                                    derivationPath[4]));
    char txId[65];
    strcpy(txId, txId_);

    signForInput(tx, inputIndex, static_cast<uint64_t>(amount), signing_address);
}

const std::string &HD_Wallet::encodeTransaction() {
    return encode_base16(tx.to_data(true, true));
}

void HD_Wallet::clearTx() {
    //todo: Send 2 transactions in a row or instantiate a new TransactionBuilder for each send?
}

wallet::hd_private HD_Wallet::initPrivateKey(data_chunk seed) {
    if (!testnet) {
        return wallet::hd_private(seed);  // mainnet
    } else {
        return wallet::hd_private(seed, wallet::hd_private::testnet); // testnet
    }
}

std::string HD_Wallet::txId() {
    return encode_hash(tx.hash());
}

std::string HD_Wallet::broadcastResultToJSON(std::string broadcastTxMessage, int broadcastTxResponseCode) {
    return "{broadcastTxMessage:\""+broadcastTxMessage+"\",broadcastTxResponseCode:"+std::to_string(broadcastTxResponseCode)+"}";
}
