package com.coinninja.bindings;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionDataTest {

    @Test
    public void setChangeIndex(){
        int index = 7;
        assertEquals(new DerivationPath("m/49/0/0/1/" + index), TransactionData.buildDerivationForChangeIndex(index));
    }

    @Test
    public void setChangeIndex_Testnet(){
        int index = 7;
        assertEquals(new DerivationPath("m/49/1/0/1/" + index), TransactionData.buildTestnetDerivationForChangeIndex(index));
    }

}