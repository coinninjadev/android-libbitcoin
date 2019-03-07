package com.coinninja.bindings;

import com.coinninja.bindings.model.Transaction;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MainnetTransactionBuilderTest {

    private TransactionBuilder mainnetBuilder;

    @Before
    public void setUp() throws Exception {
        String[] words = {"abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "about"};
        mainnetBuilder = new TransactionBuilder(words);
    }

    @Test
    public void build() throws Exception {

        DerivationPath inputPath = new DerivationPath(49, 0, 0, 0, 0);
        UnspentTransactionOutput utxo1 = new UnspentTransactionOutput("3480e31ea00efeb570472983ff914694f62804e768a6c6b4d1b6cd70a1cd3efa", 1, 449893L, inputPath);

        UnspentTransactionOutput[] utxos = new UnspentTransactionOutput[] {utxo1};
        long amount = 218384L;
        long feesAmount = 668L;
        long changeAmount = 230841L;

        DerivationPath changePath = TransactionData.buildDerivationForChangeIndex(0);

        TransactionData data = new TransactionData(utxos, amount, feesAmount, changeAmount, changePath, "3ERQiyXSeUYmxxqKyg8XwqGo4W7utgDrTR");

        String expectedTransaction = "01000000000101fa3ecda170cdb6d1b4c6a668e70428f6944691ff83294770b5fe0ea01ee380340100000017160014f990679acafe25c27615373b40bf22446d24ff44ffffffff02105503000000000017a9148ba60342bf59f73327fecab2bef17c1612888c3587b98503000000000017a9141cc1e09a63d1ae795a7130e099b28a0b1d8e4fae870247304402202698ff93ed28fce78ddfeced9edccf39e71738c72740714933012d7c30bce46e02206c0a7efe388a46f7898395a239b2a0f65244f202be44a6f634ad8a6ccbdeb4660121039b3b694b8fc5b5e07fb069c783cac754f5d38c3e08bed1960e31fdb1dda35c2400000000";

        String actual = mainnetBuilder.build(data).getRawTx();
        assertEquals(expectedTransaction, actual);

        String actualTxId =  mainnetBuilder.getTxId();
        String expectedTxId = "793690ad9bb57d55cdc5a3e340b0b53a808501a6fd9320d79f05a9e59e5e8683";

        assertEquals(expectedTxId, actualTxId);
    }

    @Test
    public void build_p2pkh_no_change() throws Exception {

        DerivationPath inputPath = new DerivationPath(49, 0, 0, 1, 7);
        UnspentTransactionOutput utxo1 = new UnspentTransactionOutput("f14914f76ad26e0c1aa5a68c82b021b854c93850fde12f8e3188c14be6dc384e", 1, 33253L, inputPath);

        UnspentTransactionOutput[] utxos = new UnspentTransactionOutput[] {utxo1};
        long amount = 23147L;
        long feesAmount = 10108L;

        TransactionData data = new TransactionData(utxos, amount, feesAmount, 0, null, "1HT6WtD5CAToc8wZdacCgY4XjJR4jV5Q5d");

        String expectedTransaction = "010000000001014e38dce64bc188318e2fe1fd5038c954b821b0828ca6a51a0c6ed26af71449f10100000017160014b4381165b195b3286079d46eb2dc8058e6f02241ffffffff016b5a0000000000001976a914b4716e71b900b957e49f749c8432b910417788e888ac02483045022100a0772115ba32fa00ad8c3ccf9661b3efa214f4b1c8c10cc8018bfca7981d45b102202a3b399a0d4c52182cda328d68be62bfafa1dae22847f597f3319d1f4c8dec89012103a45ef894ab9e6f2e55683561181be9e69b20207af746d60b95fab33476dc932400000000";

        Transaction build = mainnetBuilder.build(data);
        assertEquals(expectedTransaction, build.rawTx);

        String actualTxId =  mainnetBuilder.getTxId();
        String expectedTxId = "a69ce7a7447f2136c5b31c3fab17ad031a5b66f4d8e49566ed99d9c44d72b793";

        assertEquals(expectedTxId, actualTxId);
    }


}
