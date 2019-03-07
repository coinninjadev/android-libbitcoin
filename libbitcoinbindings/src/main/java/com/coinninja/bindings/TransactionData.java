package com.coinninja.bindings;

public class TransactionData {


    private String paymentAddress;
    private UnspentTransactionOutput[] utxos;
    private long amount;
    private long feeAmount;
    private long changeAmount;
    private DerivationPath changePath;


    public TransactionData() {
    }

    public TransactionData(UnspentTransactionOutput[] utxos, long amount, long feeAmount, long changeAmount, DerivationPath changePath, String paymentAddress) {
        this.utxos = utxos;
        this.amount = amount;
        this.feeAmount = feeAmount;
        this.changePath = changePath;
        this.paymentAddress = paymentAddress;
        this.changeAmount = changeAmount;
    }


    public DerivationPath getChangePath() {
        return changePath;
    }

    public UnspentTransactionOutput[] getUtxos() {
        return utxos;
    }

    public void setUtxos(UnspentTransactionOutput[] utxos) {
        this.utxos = utxos;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public void setChangePath(DerivationPath changePath) {
        this.changePath = changePath;
    }

    public String getPaymentAddress() {
        return paymentAddress;
    }

    public void setPaymentAddress(String paymentAddress) {
        this.paymentAddress = paymentAddress;
    }

    public static DerivationPath buildDerivationForChangeIndex(int index) {
        return new DerivationPath("m/49/0/0/1/" + index);
    }

    public static DerivationPath buildTestnetDerivationForChangeIndex(int index) {
        return new DerivationPath("m/49/1/0/1/" + index);
    }

    public long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(long changeAmount) {
        this.changeAmount = changeAmount;
    }
}
