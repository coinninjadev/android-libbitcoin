package com.coinninja.bindings;

import org.apache.commons.codec.binary.Hex;

public class Libbitcoin {

    public static final String PUBLIC = "PUBLIC";

    public Libbitcoin() {
        System.loadLibrary("LibbitcoinBindings");
        init(false);
    }

    public Libbitcoin(String[] wordList) {
        System.loadLibrary("LibbitcoinBindings");
        initWords(wordList, false);
    }

    protected Libbitcoin(boolean testnet) {
        System.loadLibrary("LibbitcoinBindings");
        init(testnet);
    }

    protected Libbitcoin(String[] wordList, boolean testnet) {
        System.loadLibrary("LibbitcoinBindings");
        initWords(wordList, testnet);
    }

    public byte[] getUncompressedPublicKey(DerivationPath derivationPath) {
        return getUncompressedPublicKey(derivationPath.toInts());
    }

    public native void initWords(String[] wordList, boolean testnet);
    public native void init(boolean isTest);
    public native String[] getSeedWords();
    public native String getExternalChangeAddress(int index);
    public native String getInternalChangeAddress(int index);
    public native String getCoinNinjaVerificationKey();
    public native String sign(String message);
    public native String encodeTransaction();
    public native boolean isTestnet();
    public native void populateTransaction(String paymentAddress, long amount, long feeAmount, long changeAmount, int[] changePath);
    public native void populateUtxo(String txId, long amount, int index, boolean replaceable);
    public native void signInput(int inputIndex, String txId, long amount, int[] derivationPath);
    public native String broadcast();
    public native String getTxId();
    public native boolean isBase58CheckEncoded(String address);

    public EncryptionKeys getEncryptionKeys(byte[] publicKey){
        return (EncryptionKeys) this.getKeys(publicKey);
    }

    public EncryptionKeys getEncryptionKeys(String publicKey){
        return (EncryptionKeys) this.getKeys(base16ToBytes(publicKey));
    }

    public DecryptionKeys getDecryptionKeys(DerivationPath path, byte[] publicKey){
        return (DecryptionKeys) this.decryptionKeys(path.toInts(), publicKey);
    }

    public native Object getKeys(byte[] publicKey);

    public native Object decryptionKeys(int[] derivationPath, byte[] publicKey);

    public native byte[] getUncompressedPublicKey(int[] derivationPath) ;

    public String getUncompressedPublicKeyHex(DerivationPath derivationPath) {
        byte[] uncompressedPublicKeyBytes = getUncompressedPublicKey(derivationPath);
        return bytesToBase16(uncompressedPublicKeyBytes);
    }

    private byte[] base16ToBytes(String str){
        byte[] val = new byte[str.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(str.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }

    private String bytesToBase16(byte[] bytes){
        StringBuilder val = new StringBuilder();
        for(byte b : bytes) {
            val.append(String.format("%02x", b));
        }
        return val.toString();
    }


}
