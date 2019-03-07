//
//  bindings.hpp
//  android-libbitcoin
//
//  Created by Dan Sexton on 5/17/18.
//  Copyright © 2018 Coin Ninja, LLC. All rights reserved.
//

#ifndef bindings_hpp
#define bindings_hpp

#include <string>
#include <boost/regex.hpp>
#include <iostream>
#include <sstream>
#include <string.h>
#include <iostream>

#include <android/log.h>
#include <jni.h>

#include "libbitcoin.hpp"
#include "hd_wallet.hpp"
#include "misc.hpp"
#include "usable_address.hpp"
#include "Base58Check.hpp"
#include "cipher_key_vendor.hpp"
#include "cipher_keys.hpp"
#include "encryption_cipher_keys.hpp"

extern "C"
{
    JNIEXPORT void JNICALL
    Java_com_coinninja_bindings_Libbitcoin_initWords(JNIEnv *env, jobject instance,
                                                    jobjectArray seedWordList,
                                                    jboolean testnet);

    JNIEXPORT void JNICALL
    Java_com_coinninja_bindings_Libbitcoin_init(JNIEnv *env, jobject instance, jboolean isTestNet);

    JNIEXPORT jobjectArray JNICALL
    Java_com_coinninja_bindings_Libbitcoin_getSeedWords(JNIEnv *env, jobject instance);

    JNIEXPORT jstring JNICALL
    Java_com_coinninja_bindings_Libbitcoin_getExternalChangeAddress(JNIEnv *env, jobject instance, jint index);

    JNIEXPORT jstring JNICALL
    Java_com_coinninja_bindings_Libbitcoin_getInternalChangeAddress(JNIEnv *env, jobject instance, jint index);

    JNIEXPORT jboolean JNICALL
    Java_com_coinninja_bindings_Libbitcoin_isTestnet(JNIEnv *env, jobject instance);

    int *convertJints(JNIEnv *env, jintArray array);

    JNIEXPORT jstring JNICALL
    Java_com_coinninja_bindings_Libbitcoin_getCoinNinjaVerificationKey(JNIEnv *env, jobject instance);

    JNIEXPORT jstring JNICALL
    Java_com_coinninja_bindings_Libbitcoin_sign(JNIEnv *env, jobject instance, jstring message_);

    JNIEXPORT jstring JNICALL
    Java_com_coinninja_bindings_Libbitcoin_encodeTransaction(JNIEnv *env, jobject instance);

    JNIEXPORT void JNICALL
    Java_com_coinninja_bindings_Libbitcoin_populateTransaction(JNIEnv *env, jobject instance,
                                                            jstring paymentAddress_, jlong amount,
                                                            jlong feeAmount, jlong changeAmount,
                                                            jintArray changePath_);

    JNIEXPORT void JNICALL
    Java_com_coinninja_bindings_Libbitcoin_signInput(JNIEnv *env, jobject instance, jint inputIndex,
                                                    jstring txId_, jlong amount,
                                                    jintArray derivationPath_);

    JNIEXPORT jstring JNICALL
    Java_com_coinninja_bindings_Libbitcoin_broadcast(JNIEnv *env, jobject instance);

    JNIEXPORT void JNICALL
    Java_com_coinninja_bindings_Libbitcoin_populateUtxo(JNIEnv *env, jobject instance, jstring txId_,
                                                        jlong amount, jint index, jboolean replaceable);
    JNIEXPORT jboolean JNICALL
    Java_com_coinninja_bindings_Libbitcoin_isBase58CheckEncoded(JNIEnv *env, jobject instance, jstring address_);

    JNIEXPORT jobject JNICALL
    Java_com_coinninja_bindings_Libbitcoin_getKeys(JNIEnv *env, jobject instance, jbyteArray publicKey);

    JNIEXPORT jobject JNICALL
    Java_com_coinninja_bindings_Libbitcoin_decryptionKeys(JNIEnv *env, jobject instance, jintArray path_, jbyteArray publicKey);

    JNIEXPORT jbyteArray JNICALL
    Java_com_coinninja_bindings_Libbitcoin_getUncompressedPublicKey(JNIEnv *env, jobject instance,
                                                            jintArray path_);
}

#endif /* bindings_hpp */
