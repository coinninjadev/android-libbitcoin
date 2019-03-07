package com.coinninja.bindings;

import com.coinninja.bindings.model.Transaction;

public class TransactionBroadcastResult {


    private int responseCode;
    private boolean success;
    private String message;
    private Transaction transaction;

    public TransactionBroadcastResult(int responseCode, boolean success, String message, Transaction transaction){
        this.responseCode = responseCode;
        this.success = success;
        this.message = message;
        this.transaction = transaction;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getTxId() {
        return transaction.getTxId();
    }

    public String getRawTx() {
        return transaction.getRawTx();
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
