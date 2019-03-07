package com.coinninja.bindings;

import com.coinninja.bindings.model.Transaction;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TransactionBuilderTest {

    private TransactionBuilder testnetBuilder;

    @Before
    public void setUp() throws Exception {

        String[] words = {"abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "about"};
        testnetBuilder = new TransactionBuilder(words, true);
    }

    @Test
    public void build_2utxos_from_same_address_testnet() throws Exception {

        DerivationPath inputPath = new DerivationPath(49, 1, 0, 0, 4);
        UnspentTransactionOutput utxo1 = new UnspentTransactionOutput("d4e29323e8c720bf12bd11ea9e73ed3eef22c79efe9cd1e43f4b52311e87429f", 0, 4900000L, inputPath);
        UnspentTransactionOutput utxo2 = new UnspentTransactionOutput("74a90934eff36e34da015b567d7995f0834866d7da4c9149ff65b1d5857f6a92", 0, 59000000L, inputPath);

        UnspentTransactionOutput[] utxos = new UnspentTransactionOutput[] {utxo1, utxo2};
        long amount = 4900000L;
        long feesAmount = 10000L;
        long changeAmount = 58990000L;
        DerivationPath changePath = TransactionData.buildTestnetDerivationForChangeIndex(2);

        TransactionData data = new TransactionData(utxos, amount, feesAmount, changeAmount, changePath, "2MtCTasDMj8AADvGvviACU38B2Xw3sR4QVP");

        String expectedTransaction = "010000000001029f42871e31524b3fe4d19cfe9ec722ef3eed739eea11bd12bf20c7e82393e2d400000000171600142abbb272fc295a839748778f450aee3616e38131ffffffff926a7f85d5b165ff49914cdad7664883f095797d565b01da346ef3ef3409a97400000000171600142abbb272fc295a839748778f450aee3616e38131ffffffff02a0c44a000000000017a9140a723a3bfd9d93b5831d05b5d5cf02b7c5683d1287b01d84030000000017a914b6eeefb4b8afa898286f9ebe3e9d70b6a9c0023d87024730440220226454680660eee28c7e3e81a2a16b3e7b9ede4289783cca7a8d34088b01049a02204a414096ab62d40bdfe25459abe982beb5d721655513492d51bf449819fde3f5012103765505df9cc00d2cd578c961a494214402283b9f6e8f28684e8798862057a02b02473044022031f9f373d9424448c9644a8e7bc47ddc71443eb6ed2a8439b50a5a5437daf48c0220148fab424210e0b6058c62987258f07ff6357107498161d9a5c8a6e3425f8217012103765505df9cc00d2cd578c961a494214402283b9f6e8f28684e8798862057a02b00000000";

        String actual = testnetBuilder.build(data).getRawTx();
        assertEquals(expectedTransaction, actual);
    }

    @Test
    public void build_replaceable_testnet() throws Exception {

        DerivationPath inputPath = new DerivationPath(49, 1, 0, 0, 4);
        UnspentTransactionOutput utxo1 = new UnspentTransactionOutput("d4e29323e8c720bf12bd11ea9e73ed3eef22c79efe9cd1e43f4b52311e87429f", 0, 4900000L, inputPath, true);
        UnspentTransactionOutput utxo2 = new UnspentTransactionOutput("74a90934eff36e34da015b567d7995f0834866d7da4c9149ff65b1d5857f6a92", 0, 59000000L, inputPath);

        UnspentTransactionOutput[] utxos = new UnspentTransactionOutput[] {utxo1, utxo2};
        long amount = 4900000L;
        long feesAmount = 10000L;
        long changeAmount = 58990000L;
        DerivationPath changePath = TransactionData.buildTestnetDerivationForChangeIndex(2);

        TransactionData data = new TransactionData(utxos, amount, feesAmount, changeAmount, changePath, "2MtCTasDMj8AADvGvviACU38B2Xw3sR4QVP");

        String expectedTransaction = "010000000001029f42871e31524b3fe4d19cfe9ec722ef3eed739eea11bd12bf20c7e82393e2d400000000171600142abbb272fc295a839748778f450aee3616e3813100000000926a7f85d5b165ff49914cdad7664883f095797d565b01da346ef3ef3409a97400000000171600142abbb272fc295a839748778f450aee3616e38131ffffffff02a0c44a000000000017a9140a723a3bfd9d93b5831d05b5d5cf02b7c5683d1287b01d84030000000017a914b6eeefb4b8afa898286f9ebe3e9d70b6a9c0023d8702473044022063e28930525921cf6b6cc98a2c6792ff794212e788e4f2155f6691b5f2ed412802207a263139865861a9c90c814b8ae778615b29a8221dc49a1cb71760861597254e012103765505df9cc00d2cd578c961a494214402283b9f6e8f28684e8798862057a02b024830450221008bf04e72b5032ef7b71f4e5b8b5ce792d52d19498590d180b3439fa30698ce3c02203a07c290375860ffb96f9cb64e436342c9f998f2adda6d9c6f58e50dbe650318012103765505df9cc00d2cd578c961a494214402283b9f6e8f28684e8798862057a02b00000000";

        Transaction actual = testnetBuilder.build(data);
        assertEquals(expectedTransaction, actual.rawTx);
    }

    @Test
    public void build_1_utxo_testnet() throws Exception {

        DerivationPath inputPath = new DerivationPath(49, 1, 0, 0, 4);
        UnspentTransactionOutput utxo1 = new UnspentTransactionOutput("d4e29323e8c720bf12bd11ea9e73ed3eef22c79efe9cd1e43f4b52311e87429f", 0, 4900000L, inputPath);

        UnspentTransactionOutput[] utxos = new UnspentTransactionOutput[] {utxo1};
        long amount = 10000L;
        long feesAmount = 10000L;
        long changeAmount = 4880000L;
        DerivationPath changePath = TransactionData.buildTestnetDerivationForChangeIndex(2);

        TransactionData data = new TransactionData(utxos, amount, feesAmount, changeAmount, changePath, "2MtCTasDMj8AADvGvviACU38B2Xw3sR4QVP");

        String expectedTransaction = "010000000001019f42871e31524b3fe4d19cfe9ec722ef3eed739eea11bd12bf20c7e82393e2d400000000171600142abbb272fc295a839748778f450aee3616e38131ffffffff02102700000000000017a9140a723a3bfd9d93b5831d05b5d5cf02b7c5683d128780764a000000000017a914b6eeefb4b8afa898286f9ebe3e9d70b6a9c0023d87024730440220146931784a867626c82256d2d57f9d46ef72b53aed9a9e4be796755d6d043998022048671e7e555228bacca6676d9d90d5b8d21a9a607d504a2561ab61e2b685b3b1012103765505df9cc00d2cd578c961a494214402283b9f6e8f28684e8798862057a02b00000000";

        String actualTransaction = testnetBuilder.build(data).getRawTx();
        assertEquals(expectedTransaction, actualTransaction);
    }

    @Test
    public void build_p2pkh_testnet() throws Exception {

        DerivationPath inputPath = new DerivationPath(49, 1, 0, 0, 4);
        UnspentTransactionOutput utxo1 = new UnspentTransactionOutput("d4e29323e8c720bf12bd11ea9e73ed3eef22c79efe9cd1e43f4b52311e87429f", 0, 4900000L, inputPath);

        UnspentTransactionOutput[] utxos = new UnspentTransactionOutput[] {utxo1};
        long amount = 10000L;
        long feesAmount = 10000L;
        long changeAmount = 4880000L;
        DerivationPath changePath = TransactionData.buildTestnetDerivationForChangeIndex(2);

        TransactionData data = new TransactionData(utxos, amount, feesAmount, changeAmount, changePath, "mjELfPDxFTSqga3AgSwJr2aB5mAPmDEwZS");

        String expectedTransaction = "010000000001019f42871e31524b3fe4d19cfe9ec722ef3eed739eea11bd12bf20c7e82393e2d400000000171600142abbb272fc295a839748778f450aee3616e38131ffffffff0210270000000000001976a91428bc1a7b9c8df240503e6f45e852f137c93275bb88ac80764a000000000017a914b6eeefb4b8afa898286f9ebe3e9d70b6a9c0023d870247304402204411b85681e9313edd1619fe85c06e78920c4b98cb69fa7cbdceefbfd8067030022068f96686ce32067b1d3d431c31d50c140999cae9a7d215139b05823653bfc443012103765505df9cc00d2cd578c961a494214402283b9f6e8f28684e8798862057a02b00000000";

        String actual = testnetBuilder.build(data).rawTx;
        assertEquals(expectedTransaction, actual);
    }
}
