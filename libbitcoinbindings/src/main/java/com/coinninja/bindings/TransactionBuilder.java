package com.coinninja.bindings;

import com.coinninja.bindings.model.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

public class TransactionBuilder {
    public static final String BROADCAST_TX_MESSAGE_KEY = "broadcastTxMessage";
    public static final String BROADCAST_TX_RESPONSE_CODE_KEY = "broadcastTxResponseCode";

    private final Libbitcoin libbitcoin;

    public TransactionBuilder(String[] words) {
        this(words, false);
    }

    public TransactionBuilder(String[] words, boolean testnet) {
        libbitcoin = new Libbitcoin(words, testnet);
    }

    public TransactionBuilder(Libbitcoin libbitcoin) {
        this.libbitcoin = libbitcoin;
    }

    public Transaction build(TransactionData data) {
        int[] changePath = data.getChangePath() != null ? data.getChangePath().toInts() : new int[]{};
        libbitcoin.populateTransaction(data.getPaymentAddress(), data.getAmount(), data.getFeeAmount(),
                data.getChangeAmount(), changePath);
        for (UnspentTransactionOutput utxo : data.getUtxos()) {
            libbitcoin.populateUtxo(utxo.txId, utxo.amount, utxo.index, utxo.replaceable);
        }
        // Todo: passing more complex objects into JNI would allow us to simplify this method and eliminate the double loop.

        for (int i = 0; i < data.getUtxos().length; i++) {
            UnspentTransactionOutput utxo = data.getUtxos()[i];
            libbitcoin.signInput(i, utxo.txId, utxo.amount, utxo.path.toInts());
        }

        String rawTx = libbitcoin.encodeTransaction();
        String txId = libbitcoin.getTxId();
        return new Transaction(rawTx, txId);
    }


    public TransactionBroadcastResult buildAndBroadcast(TransactionData data) {
        String resultMessage;
        int resultCode;
        Transaction tx = null;
        try {
            tx = build(data);
            String resultJSON = libbitcoin.broadcast();
            JSONObject jsonObject = resultToJson(resultJSON);

            resultMessage = jsonObject.getString(BROADCAST_TX_MESSAGE_KEY);
            resultCode = jsonObject.getInt(BROADCAST_TX_RESPONSE_CODE_KEY);
        } catch (Exception e) {
            resultMessage = e.getMessage();
            resultCode = -1;
        }
        if (resultMessage.contains("Success:")) {
            return new TransactionBroadcastResult(resultCode,true, resultMessage, tx);
        } else {
            return new TransactionBroadcastResult(resultCode,false, resultMessage, tx);
        }
    }

    private JSONObject resultToJson(String resultJSON) {
        try {
            return new JSONObject(resultJSON);

        }catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(BROADCAST_TX_MESSAGE_KEY,e.getMessage());
                jsonObject.put(BROADCAST_TX_RESPONSE_CODE_KEY,-1);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return jsonObject;
        }
    }

    public String getTxId() {
        return libbitcoin.getTxId();
    }


}
