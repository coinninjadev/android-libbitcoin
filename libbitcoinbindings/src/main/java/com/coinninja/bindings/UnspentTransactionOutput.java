package com.coinninja.bindings;

public class UnspentTransactionOutput {

    boolean replaceable;
    String txId;
    int index;
    long amount;
    DerivationPath path;

    public UnspentTransactionOutput(String txId, int index, long amount, DerivationPath path) {
        this(txId, index, amount, path, false);
    }
    public UnspentTransactionOutput(String txId, int index, long amount, DerivationPath path, boolean replaceable) {
        this.txId = txId;
        this.index = index;
        this.amount = amount;
        this.path = path;
        this.replaceable = replaceable;
    }

    public String getTxId() {
        return txId;
    }

    public int getIndex() {
        return index;
    }

    public long getAmount() {
        return amount;
    }

    public DerivationPath getPath() {
        return path;
    }

    public boolean isReplaceable(){
        return replaceable;
    }
}
