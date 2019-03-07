#include "bindings.hpp"

HD_Wallet *hdWallet;

JNIEXPORT void JNICALL
Java_com_coinninja_bindings_Libbitcoin_initWords(JNIEnv *env, jobject instance,
                                                jobjectArray seedWordList,
                                                jboolean testnet) {

    std::vector<std::string> mnemonicSeed;

    int size = env->GetArrayLength(seedWordList);

    for (int i = 0; i < size; ++i) {
        jstring string = (jstring) (env->GetObjectArrayElement(seedWordList, i));
        const char *word = env->GetStringUTFChars(string, 0);
        mnemonicSeed.push_back(word);

        env->ReleaseStringUTFChars(string, word);
        env->DeleteLocalRef(string);
    }

    hdWallet = new HD_Wallet(mnemonicSeed, testnet);
}

JNIEXPORT void JNICALL
Java_com_coinninja_bindings_Libbitcoin_init(JNIEnv *env, jobject instance, jboolean isTestNet) {
    hdWallet = new HD_Wallet(isTestNet);
}

JNIEXPORT jobjectArray JNICALL
Java_com_coinninja_bindings_Libbitcoin_getSeedWords(JNIEnv *env, jobject instance) {

    jobjectArray ret;

    wallet::word_list currentMNemonic = hdWallet->getMNemonic();

    ret = (jobjectArray) env->NewObjectArray(currentMNemonic.size(),
                                            env->FindClass("java/lang/String"),
                                            env->NewStringUTF(""));

//    if (wallet::validate_mnemonic(currentMNemonic)) {
        for (long i = 0; i < currentMNemonic.size(); i++) {
            std::string mn = currentMNemonic[i];
            jstring outString = env->NewStringUTF(mn.c_str());

            env->SetObjectArrayElement(ret, i, outString);
        }
//    }
    return (ret);
}

JNIEXPORT jstring JNICALL
Java_com_coinninja_bindings_Libbitcoin_getExternalChangeAddress(JNIEnv *env, jobject instance,
                                                                jint index) {

    const std::string &change = hdWallet->getExternalChangeAddress(index);

    return env->NewStringUTF(change.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_coinninja_bindings_Libbitcoin_getInternalChangeAddress(JNIEnv *env, jobject instance,
                                                                jint index) {
    const std::string &change = hdWallet->getInternalChangeAddress(index);

    jstring changeAddress = env->NewStringUTF(change.c_str());
    return changeAddress;
}

JNIEXPORT jboolean JNICALL
Java_com_coinninja_bindings_Libbitcoin_isTestnet(JNIEnv *env, jobject instance) {
    return static_cast<jboolean>(hdWallet->isTestnet());
}

int *convertJints(JNIEnv *env, jintArray array) {
    return env->GetIntArrayElements(array, reinterpret_cast<jboolean *>(true));
}

JNIEXPORT jstring JNICALL
Java_com_coinninja_bindings_Libbitcoin_getCoinNinjaVerificationKey(JNIEnv *env,
                                                                jobject instance) {

    const std::string &returnValue = hdWallet->getCoinNinjaSigningKey();

    return env->NewStringUTF(returnValue.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_coinninja_bindings_Libbitcoin_sign(JNIEnv *env, jobject instance, jstring message_) {
    const char *message = env->GetStringUTFChars(message_, 0);

    std::string returnValue = hdWallet->signMessage(message);

    env->ReleaseStringUTFChars(message_, message);

    return env->NewStringUTF(returnValue.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_coinninja_bindings_Libbitcoin_encodeTransaction(JNIEnv *env, jobject instance) {



    const std::string &transaction = hdWallet->encodeTransaction();

    return env->NewStringUTF(transaction.c_str());
}

JNIEXPORT void JNICALL
Java_com_coinninja_bindings_Libbitcoin_populateTransaction(JNIEnv *env, jobject instance,
                                                        jstring paymentAddress_, jlong amount,
                                                        jlong feeAmount, jlong changeAmount,
                                                        jintArray changePath_) {
    const char *paymentAddress = env->GetStringUTFChars(paymentAddress_, 0);
    jint *changePath = env->GetIntArrayElements(changePath_, NULL);

    hdWallet->populateTransaction(paymentAddress, amount, feeAmount, changeAmount, changePath);

    env->ReleaseStringUTFChars(paymentAddress_, paymentAddress);
    env->ReleaseIntArrayElements(changePath_, changePath, 0);
}

JNIEXPORT void JNICALL
Java_com_coinninja_bindings_Libbitcoin_signInput(JNIEnv *env, jobject instance, jint inputIndex,
                                                jstring txId_, jlong amount,
                                                jintArray derivationPath_) {
    const char *txId = env->GetStringUTFChars(txId_, 0);
    jint *derivationPath = env->GetIntArrayElements(derivationPath_, NULL);

    hdWallet->signInput(inputIndex, txId, amount, derivationPath);

    env->ReleaseStringUTFChars(txId_, txId);
    env->ReleaseIntArrayElements(derivationPath_, derivationPath, 0);
}

JNIEXPORT jstring JNICALL
Java_com_coinninja_bindings_Libbitcoin_broadcast(JNIEnv *env, jobject instance) {

    std::string returnValue = hdWallet->broadcastTx();
    hdWallet->clearTx();

    return env->NewStringUTF(returnValue.c_str());


}

JNIEXPORT void JNICALL
Java_com_coinninja_bindings_Libbitcoin_populateUtxo(JNIEnv *env, jobject instance, jstring txId_,
                                                    jlong amount, jint index,  jboolean replaceable) {
    const char *txId = env->GetStringUTFChars(txId_, 0);

    hdWallet->addUtxo(txId, amount, index, replaceable);

    env->ReleaseStringUTFChars(txId_, txId);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_coinninja_bindings_Libbitcoin_getTxId(JNIEnv *env, jobject instance) {

    std::string returnValue = hdWallet->txId();

    return env->NewStringUTF(returnValue.c_str());
}

JNIEXPORT jboolean JNICALL
Java_com_coinninja_bindings_Libbitcoin_isBase58CheckEncoded(JNIEnv *env, jobject instance, jstring address_) {
    const char *address = env->GetStringUTFChars(address_, 0);
    bool isBase58= Base58Check::addressIsBase58CheckEncoded(address);

    env->ReleaseStringUTFChars(address_, address);

    return static_cast<jboolean>(isBase58);
}

uint8_t* as_c_byte_array(JNIEnv *env, jbyteArray array) {
    int len = env->GetArrayLength (array);
    uint8_t* buf = new uint8_t[len];
    env->GetByteArrayRegion (array, 0, len, reinterpret_cast<jbyte*>(buf));
    return buf;
}


jbyteArray as_byte_array(JNIEnv *env, uint8_t* buf, int length) {
    jbyteArray array = env->NewByteArray (length);
    env->SetByteArrayRegion (array, 0, length, reinterpret_cast<jbyte*>(buf));
    return array;
}

char* as_unsigned_char_array(JNIEnv *env, jbyteArray array) {
    int len = env->GetArrayLength (array);
    char* buf = new char[len];
    env->GetByteArrayRegion (array, 0, len, reinterpret_cast<jbyte*>(buf));
    return buf;
}

data_chunk bytes_to_data_chunk(uint8_t *bytes, int length){
    data_chunk chunk;
    for(int i = 0; i < length; i++){
        chunk.push_back(bytes[i]);
    }
    return chunk;
}

JNIEXPORT jobject JNICALL
Java_com_coinninja_bindings_Libbitcoin_getKeys(JNIEnv *env, jobject instance, jbyteArray publicKey) {

    jclass clazz = env->FindClass("com/coinninja/bindings/EncryptionKeys");
    jmethodID constructor = env->GetMethodID(clazz, "<init>", "([B[B[B)V");

    int length = env->GetArrayLength(publicKey);

    uint8_t *publicKeyBytes = as_c_byte_array(env, publicKey);

    data_chunk keyChunk = bytes_to_data_chunk(publicKeyBytes, length);

    encryption_cipher_keys keys = cipher_key_vendor::encryption_cipher_keys_for_uncompressed_public_key(keyChunk);

    hash_digest encryption_key = keys.get_encryption_key();
    hash_digest hmac_key = keys.get_hmac_key();
    data_chunk ephemeral_public_key = keys.get_ephemeral_public_key();

    jbyteArray encryption = as_byte_array(env, encryption_key.data(), hash_size);
    jbyteArray hmac = as_byte_array(env, hmac_key.data(), hash_size);
    jbyteArray ephemeral = as_byte_array(env, ephemeral_public_key.data(), 65);

    return env->NewObject(clazz, constructor, encryption, hmac, ephemeral);
}


JNIEXPORT jbyteArray JNICALL
Java_com_coinninja_bindings_Libbitcoin_getUncompressedPublicKey(JNIEnv *env, jobject instance,
                                                        jintArray path_){
    jint *path = env->GetIntArrayElements(path_, NULL);

    data_chunk returnValue = hdWallet->getUncompressedPublicKey(path);

    env->ReleaseIntArrayElements(path_, path, 0);

    return as_byte_array(env, returnValue.data(), 65);
}


JNIEXPORT jobject JNICALL
Java_com_coinninja_bindings_Libbitcoin_decryptionKeys(JNIEnv *env, jobject instance, jintArray path_, jbyteArray publicKey){
    jclass clazz = env->FindClass("com/coinninja/bindings/DecryptionKeys");
    jmethodID constructor = env->GetMethodID(clazz, "<init>", "([B[B)V");
    int length = env->GetArrayLength(publicKey);

    uint8_t *publicKeyBytes = as_c_byte_array(env, publicKey);

    data_chunk keyChunk = bytes_to_data_chunk(publicKeyBytes, length);

    jint *path = env->GetIntArrayElements(path_, NULL);
    hd_private privateKey = hdWallet->getPrivateKey(path);

    cipher_keys keys = cipher_key_vendor::decryption_cipher_keys(privateKey, keyChunk);

    hash_digest encryption_key = keys.get_encryption_key();
    hash_digest hmac_key = keys.get_hmac_key();

    jbyteArray encryption = as_byte_array(env, encryption_key.data(), hash_size);
    jbyteArray hmac = as_byte_array(env, hmac_key.data(), hash_size);

    env->ReleaseIntArrayElements(path_, path, 0);

    return env->NewObject(clazz, constructor, encryption, hmac);
}

