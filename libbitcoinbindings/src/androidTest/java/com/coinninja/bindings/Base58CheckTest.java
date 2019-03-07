package com.coinninja.bindings;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Base58CheckTest {
    private Libbitcoin libbitcoin;

    @Before
    public void setUp() throws Exception {
        libbitcoin = new Libbitcoin();
    }

    @Test
    public void test_a_good_P2SH_address() {
        String goodAddress = "394YWe6P48qdfFEc25wp2m8tuPF1eJFqBe";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(goodAddress);

        assertThat(isBase58CheckEncoded, equalTo(true));
    }

    @Test
    public void test_a_bad_P2SH_address() {
        String badAddress = "394YWe6P48qdfFEc25wp2m8tuPF1eJFqBejjj";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(badAddress);

        assertThat(isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_a_good_P2PKH_address() {
        String badAddress = "185GYRXheZckJNBHGEdt4b9GSYcXV9r7fw";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(badAddress);

        assertThat(isBase58CheckEncoded, equalTo(true));
    }

    @Test
    public void test_a_bad_P2PKH_address() {
        String badAddress = "185GYRXheZckJNBHGEdt4b9GSYcXV9r7fw555";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(badAddress);

        assertThat(isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_valid_BreadP2PKHAddress_isValid() {
        String address = "12vRFewBpbdiS5HXDDLEfVFtJnpA2x8NV8";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Valid Bread P2PKH should be valid Base58Check address", isBase58CheckEncoded, equalTo(true));
    }

    @Test
    public void test_invalid_BreadP2PKHAddress_isInvalid() {
        String address = "12vRFewBpbdiS5HXDDLEfVFtJnpA2";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid Bread P2PKH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_valid_P2PKHAddress_isValid() {
        String address = "16UwLL9Risc3QfPqBUvKofHmBQ7wMtjvM";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Valid P2PKH should be valid Base58Check address", isBase58CheckEncoded, equalTo(true));
    }

    @Test
    public void test_invalid_P2PKHAddressWithCharactersRemoved_isInvalid() {
        String address = "12vRFewBpbdiS5HXDDLEfVFt";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2PKH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_P2PKHAddressWithFirst10CharsRemoved_isInvalid() {
        String address = "diS5HXDDLEfVFtJnpA2x8NV8";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2PKH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_P2PKHAddressWithExtraLeadingDigit_isInvalid() {
        String address = "212vRFewBpbdiS5HXDDLEfVFtJnpA2x8NV8";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2PKH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_P2PKHAddressWithDifferentLeadingDigit_isInvalid() {
        String address = "42vRFewBpbdiS5HXDDLEfVFtJnpA2x8NV8";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2PKH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_valid_P2SHAddressFromCoinbase_isValid() {
        String address = "3EH9Wj6KWaZBaYXhVCa8ZrwpHJYtk44bGX";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Valid P2SH should be valid Base58Check address", isBase58CheckEncoded, equalTo(true));
    }

    @Test
    public void test_invalid_P2SHAddressWithLast5CharsRemoved_isInvalid() {
        String address = "3EH9Wj6KWaZBaYXhVCa8ZrwpHJYtk";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_P2SHAddressWithFirst5CharsRemoved_isInvalid() {
        String address = "j6KWaZBaYXhVCa8ZrwpHJYtk44bGX";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_P2SHAddressWithFirst10CharsRemoved_isInvalid() {
        String address = "ZBaYXhVCa8ZrwpHJYtk44bGX";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_P2SHAddressWithExtraLeadingDigit_isInvalid() {
        String address = "23EH9Wj6KWaZBaYXhVCa8ZrwpHJYtk44bGX";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_valid_DropBitP2SHP2WPKHAddress_isValid() {
        String address = "3Cd4xEu2VvM352BVgd9cb1Ct5vxz318tVT";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Valid P2SH(P2WPKH) should be valid Base58Check address", isBase58CheckEncoded, equalTo(true));
    }

    @Test
    public void test_invalid_DropBitP2SHP2WPKHAddressWithLast5CharsRemoved_isInvalid() {
        String address = "3Cd4xEu2VvM352BVgd9cb1Ct5vxz3";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH(P2WPKH) should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_DropBitP2SHP2WPKHAddressWithLast10CharsRemoved_isInvalid() {
        String address = "3Cd4xEu2VvM352BVgd9cb1Ct";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH(P2WPKH) should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_DropBitP2SHP2WPKHAddressWithFirst5CharsRemoved_isInvalid() {
        String address = "Eu2VvM352BVgd9cb1Ct5vxz318tVT";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH(P2WPKH) should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_DropBitP2SHP2WPKHAddressWithFirst10CharsRemoved_isInvalid() {
        String address = "M352BVgd9cb1Ct5vxz318tVT";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH(P2WPKH) should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_DropBitP2SHP2WPKHAddressWithExtraLeadingChar_isInvalid() {
        String address = "23Cd4xEu2VvM352BVgd9cb1Ct5vxz318tVT";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH(P2WPKH) should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_DropBitP2SHP2WPKHAddressWithDifferentLeadingChar_isInvalid() {
        String address = "4Cd4xEu2VvM352BVgd9cb1Ct5vxz318tVT";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid P2SH(P2WPKH) should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_valid_EthereumAddress_isInvalid() {
        String address = "0xF26C29D25a1E1696c5CC54DE4bf2AEc906EB4F79";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Valid Ethereum address should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_valid_BCHAddress_isInvalid() {
        String address = "qr45rul6luexjgg5h8p26c0cs6rrhwzrkg6e0hdvrf";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Valid BCH address should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_SongLyricsAddress_isInvalid() {
        String address = "Jenny86753098675309IgotIt";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid song lyrics should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_gibberishAddress_isInvalid() {
        String address = "31415926535ILikePi89793238462643";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid gibberish should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_FooAddress_isInvalid() {
        String address = "foo";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid foo should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_EmptyStringAddress_isInvalid() {
        String address = "";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid empty string should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }

    @Test
    public void test_invalid_bitcoinSchemeAddress_isInvalid() {
        String address = "bitcoin:3Cd4xEu2VvM352BVgd9cb1Ct5vxz318tVT";
        boolean isBase58CheckEncoded = libbitcoin.isBase58CheckEncoded(address);
        assertThat("Invalid empty string should be invalid Base58Check address", isBase58CheckEncoded, equalTo(false));
    }
}
