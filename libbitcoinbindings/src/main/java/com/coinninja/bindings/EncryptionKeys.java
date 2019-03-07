package com.coinninja.bindings;

public class EncryptionKeys {
    public byte[] encryptionKey;
    public byte[] hmacKey;
    public byte[] ephemeralPublicKey;

    public EncryptionKeys(byte[] encryptionKey, byte[] hmacKey, byte[] ephemeralPublicKey){
        this.encryptionKey = encryptionKey;
        this.hmacKey = hmacKey;
        this.ephemeralPublicKey = ephemeralPublicKey;
    }
}
