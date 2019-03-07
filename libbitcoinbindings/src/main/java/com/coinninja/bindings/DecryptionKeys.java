package com.coinninja.bindings;

public class DecryptionKeys {
    public byte[] encryptionKey;
    public byte[] hmacKey;

    public DecryptionKeys(byte[] encryptionKey, byte[] hmacKey){
        this.encryptionKey = encryptionKey;
        this.hmacKey = hmacKey;
    }
}
