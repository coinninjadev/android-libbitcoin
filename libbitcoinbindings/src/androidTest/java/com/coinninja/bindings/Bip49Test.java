package com.coinninja.bindings;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/* https://github.com/bitcoin/bips/blob/master/bip-0049.mediawiki
Test vectors
  masterseedWords = abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about
  masterseed = tprv8ZgxMBicQKsPe5YMU9gHen4Ez3ApihUfykaqUorj9t6FDqy3nP6eoXiAo2ssvpAjoLroQxHqr3R5nE3a5dU3DHTjTgJDd7zrbniJr6nrCzd (testnet)

  // Account 0, root = m/49'/1'/0'
  account0Xpriv = tprv8gRrNu65W2Msef2BdBSUgFdRTGzC8EwVXnV7UGS3faeXtuMVtGfEdidVeGbThs4ELEoayCAzZQ4uUji9DUiAs7erdVskqju7hrBcDvDsdbY (testnet)

  // Account 0, first receiving private key = m/49'/1'/0'/0/0
  account0recvPrivateKey = cULrpoZGXiuC19Uhvykx7NugygA3k86b3hmdCeyvHYQZSxojGyXJ
  account0recvPrivateKeyHex = 0xc9bdb49cfbaedca21c4b1f3a7803c34636b1d7dc55a717132443fc3f4c5867e8
  account0recvPublickKeyHex = 0x03a1af804ac108a8a51782198c2d034b28bf90c8803f5a53f76276fa69a4eae77f

  // Address derivation
  keyhash = HASH160(account0recvPublickKeyHex) = 0x38971f73930f6c141d977ac4fd4a727c854935b3
  scriptSig = <0 <keyhash>> = 0x001438971f73930f6c141d977ac4fd4a727c854935b3
  addressBytes = HASH160(scriptSig) = 0x336caa13e08b96080a32b5d818d59b4ab3b36742

  // addressBytes base58check encoded for testnet
  address = base58check(prefix | addressBytes) = 2Mww8dCYPUpKHofjgcXcBCEGmniw9CoaiD2 (testnet)
 */
public class Bip49Test {
    private Libbitcoin libbitcoin;

    @Test
    public void bip49ChangeAddress_TestNet() throws Exception {
        String[] masterseedWords = {"abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "about"};
        libbitcoin = new LibbitcoinTestnet(masterseedWords);

        String externalChangeAddress = libbitcoin.getExternalChangeAddress(0);

        String externalChangeAddress0_TestNet = "2Mww8dCYPUpKHofjgcXcBCEGmniw9CoaiD2";
        assertTrue(externalChangeAddress0_TestNet.contentEquals(externalChangeAddress));
    }

    @Test
    public void bip49ChangeAddress_MainNet() throws Exception {
        libbitcoin = new Libbitcoin(new String[]{"abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "about"});

        String externalChangeAddress = libbitcoin.getExternalChangeAddress(0);

        String externalChangeAddress0 = "37VucYSaXLCAsxYyAPfbSi9eh4iEcbShgf";
        assertEquals(externalChangeAddress0, externalChangeAddress);
    }
}
