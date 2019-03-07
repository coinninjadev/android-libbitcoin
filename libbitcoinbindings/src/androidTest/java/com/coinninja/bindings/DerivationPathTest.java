package com.coinninja.bindings;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DerivationPathTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void directSet() throws Exception {
        DerivationPath derivationPath = new DerivationPath(49,1,2,3,4);
        assertEquals((Integer) 49, derivationPath.getPurpose());
        assertEquals((Integer) 1, derivationPath.getCoinType());
        assertEquals((Integer) 2, derivationPath.getAccount());
        assertEquals((Integer) 3, derivationPath.getChange());
        assertEquals((Integer) 4, derivationPath.getIndex());
    }

    @Test
    public void parseHappy() throws Exception {
        DerivationPath derivationPath = new DerivationPath("m/49/1/2/3/4");
        assertEquals((Integer) 49, derivationPath.getPurpose());
        assertEquals((Integer) 1, derivationPath.getCoinType());
        assertEquals((Integer) 2, derivationPath.getAccount());
        assertEquals((Integer) 3, derivationPath.getChange());
        assertEquals((Integer) 4, derivationPath.getIndex());
    }

    @Test
    public void toInts() throws Exception {
        int[] expected = new int[]{49,1,2,3,4};
        DerivationPath derivationPath = new DerivationPath("m/49/1/2/3/4");
        int[] actualInts = derivationPath.toInts();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actualInts[i]);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void toIntsRequiresSegwitPath() throws Exception {
        new DerivationPath("m/49/1/2/3").toInts();
    }

    @Test
    public void parsePartial() throws Exception {
        DerivationPath derivationPath = new DerivationPath("m/49");
        assertEquals((Integer) 49, derivationPath.getPurpose());
        assertNull(derivationPath.getCoinType());
        assertNull(derivationPath.getAccount());
        assertNull(derivationPath.getChange());
        assertNull(derivationPath.getIndex());
    }

    @Test
    public void parseEmptyString() throws Exception {
        DerivationPath derivationPath = new DerivationPath("");
        assertNull(derivationPath.getPurpose());
        assertNull(derivationPath.getCoinType());
        assertNull(derivationPath.getAccount());
        assertNull(derivationPath.getChange());
        assertNull(derivationPath.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse() throws Exception {
        new DerivationPath("m/49/1/2/3/4/5/6/7");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseBogus() throws Exception {
        new DerivationPath("rubbish");
    }

}