//
//  hd_wallet.hpp
//  android-libbitcoin
//
//  Created by Dan Sexton on 5/17/18.
//  Copyright © 2018 Coin Ninja, LLC. All rights reserved.
//

#ifndef hd_wallet_hpp
#define hd_wallet_hpp

#include "libbitcoin.hpp"
#include <jni.h>

class HD_Wallet {
public:
    HD_Wallet(const wallet::word_list mnemonicSeed, bool _isTestnet);
    HD_Wallet(bool _isTestnet);
    wallet::word_list getMNemonic();
    std::string getExternalChangeAddress(int index);
    std::string getInternalChangeAddress(int index);
    std::string getCoinNinjaSigningKey();
    wallet::hd_private getCoinNinjaPrivateSigningKey();
    wallet::hd_private getPrivateKey(jint *derivationPath);
    bool isTestnet();
    std::string broadcastTx();
    data_chunk getUncompressedPublicKey(jint *derivationPath);
        hash_digest stringToDigest(const char *message);
        data_chunk stringToDataChunk(const char *message);
    std::string signMessage(const char *message);
    void populatePaymentOutput(const char *paymentAddress, jlong amount);
    void populateTransaction(const char *paymentAddress, jlong amount, jlong fees, jlong changeAmount, jint *derivationPath);
    void addUtxo(const char *txId_, jlong amount, jint index, jboolean replaceable);
    void signInput(jint inputIndex, const char *txId_, jlong amount, jint *derivationPath);
    const std::string &encodeTransaction();
    void clearTx();
    std::string txId();

private:
    bool testnet;
    data_chunk entropy;
    data_chunk seed;
    wallet::word_list mnemonic;
    wallet::hd_private privateKeyM;
    chain::transaction tx;

    wallet::hd_private initPrivateKey(data_chunk seed);

    std::string broadcastResultToJSON(std::string broadcastTxMessage, int broadcastTxResponseCode);
};

#endif /* hd_wallet_hpp */
