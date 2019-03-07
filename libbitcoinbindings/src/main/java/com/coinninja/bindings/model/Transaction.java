package com.coinninja.bindings.model;

public class Transaction {
    public final String  txId;
    public final String rawTx;

    public Transaction(String rawTx, String txId) {
        this.rawTx = rawTx;
        this.txId = txId;
    }

    public String getTxId() {
        return txId;
    }

    public String getRawTx() {
        return rawTx;
    }
}
