package com.coinninja.bindings;

import org.junit.Before;
import org.junit.Test;


import java.util.Base64;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;


public class LibbitcoinSigningTest {
    private Libbitcoin libbitcoin;

    @Before
    public void setUp() throws Exception {
        String[] words = {"abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "about"};
        libbitcoin = new Libbitcoin(words);
    }

    @Test
    public void Sign() throws Exception {
        String expected = "3045022100c515fc2ed70810f6b1383cfe8e81b9b41b08682511e92d557f1b1719391b521d02200d9d734fd09ce60586ac48b0a7eb587a50958cd9fa548ffa39088fc6ada12eec";
        String signed = libbitcoin.sign("Hello World");
        assertEquals(expected, signed);
    }

    @Test
    public void getCoinNinjaVerificationKey(){
        String expected = "024458596b5c97e716e82015a72c37b5d3fe0c5dc70a4b83d72e7d2eb65920633e";
        String coinNinjaVerificationKey = libbitcoin.getCoinNinjaVerificationKey();
        assertEquals(expected, coinNinjaVerificationKey);
    }

    @Test
    public void getUncompressedPublicKey_bytes(){
        byte[] expected = base16ToBytes("049b3b694b8fc5b5e07fb069c783cac754f5d38c3e08bed1960e31fdb1dda35c2449bdd1f0ae7d37a04991d4f5927efd359c13189437d9eae0faf7d003ffd04c89");

        DerivationPath derivationPath = new DerivationPath(49, 0, 0, 0, 0);
        byte[] uncompressedPublicKey = libbitcoin.getUncompressedPublicKey(derivationPath);

        assertThat(expected, equalTo(uncompressedPublicKey));
    }

    @Test
    public void getUncompressedPublicKey(){
        String expected = "049b3b694b8fc5b5e07fb069c783cac754f5d38c3e08bed1960e31fdb1dda35c2449bdd1f0ae7d37a04991d4f5927efd359c13189437d9eae0faf7d003ffd04c89";

        DerivationPath derivationPath = new DerivationPath(49, 0, 0, 0, 0);
        assertThat(expected, equalTo(libbitcoin.getUncompressedPublicKeyHex(derivationPath)));
    }

    @Test
    public void test_getEncryptionKeys() {
        byte[] uncompressedPublicKey = base16ToBytes("04904240a0aaec6af6f9b6c331f71feea2a4ed1549c06e5a6409fe92c5824dc4c54e26c2b2e27cfc224a6b782b35a2872b666f568cf37456262fbb065601b4d73a");
        EncryptionKeys keys1 = libbitcoin.getEncryptionKeys(uncompressedPublicKey);

        assertThat(keys1.encryptionKey.length, equalTo(32));
        assertThat(keys1.hmacKey.length, equalTo(32));
        assertThat(keys1.ephemeralPublicKey.length, equalTo(65));
        assertThat(keys1.ephemeralPublicKey[0], equalTo(new Byte("4")));

        EncryptionKeys keys2 = libbitcoin.getEncryptionKeys(uncompressedPublicKey);

        assertNotEquals(keys1.encryptionKey, keys2.encryptionKey);
        assertNotEquals(keys1.hmacKey, keys2.hmacKey);
        assertNotEquals(keys1.ephemeralPublicKey, keys2.ephemeralPublicKey);
    }

    @Test
    public void test_getEncryptionKeys_Hex() {
        String uncompressedPublicKey = "04904240a0aaec6af6f9b6c331f71feea2a4ed1549c06e5a6409fe92c5824dc4c54e26c2b2e27cfc224a6b782b35a2872b666f568cf37456262fbb065601b4d73a";
        EncryptionKeys keys1 = libbitcoin.getEncryptionKeys(uncompressedPublicKey);

        assertThat(keys1.encryptionKey.length, equalTo(32));
        assertThat(keys1.hmacKey.length, equalTo(32));
        assertThat(keys1.ephemeralPublicKey.length, equalTo(65));
        assertThat(keys1.ephemeralPublicKey[0], equalTo(new Byte("4")));

        EncryptionKeys keys2 = libbitcoin.getEncryptionKeys(uncompressedPublicKey);

        assertNotEquals(keys1.encryptionKey, keys2.encryptionKey);
        assertNotEquals(keys1.hmacKey, keys2.hmacKey);
        assertNotEquals(keys1.ephemeralPublicKey, keys2.ephemeralPublicKey);
    }

    @Test
    public void test_getDecryptionKeys() {
        DerivationPath path = new DerivationPath(49, 0, 0, 0, 0);
        byte[] decoded = Base64.getDecoder().decode("BBS6AnMOS9Y+uGsEDYQycHHzcC7PPmzuKDtSda842AtSANZjgm++vr8uEc/bWacKQDL+/KyL3CuIs+m+ueejbBs=");

        DecryptionKeys keys = libbitcoin.getDecryptionKeys(path, decoded);
        assertThat(keys.encryptionKey.length, equalTo(32));
        assertThat(keys.hmacKey.length, equalTo(32));
    }

    @Test
    public void test_encryption_and_decryption_keys_are_compatible() {
        //get a receiver uncompressed public key for derivationPath
        DerivationPath derivationPath = new DerivationPath(49, 0, 0, 0, 0);
        byte[] uncompressedPublicKey = libbitcoin.getUncompressedPublicKey(derivationPath);

        //sender builds encryption keys from rupk
        EncryptionKeys encryptionKeys = libbitcoin.getEncryptionKeys(uncompressedPublicKey);

        //receiver builds decryption keys from ephemeral public key and derivationPath
        DecryptionKeys decryptionKeys = libbitcoin.getDecryptionKeys(derivationPath, encryptionKeys.ephemeralPublicKey);

        //assert both encryptionKeys and hmacKeys are the same
        assertThat(encryptionKeys.encryptionKey, equalTo(decryptionKeys.encryptionKey));
        assertThat(encryptionKeys.hmacKey, equalTo(decryptionKeys.hmacKey));
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
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

}