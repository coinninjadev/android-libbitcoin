package com.coinninja.bindings.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.coinninja.bindings.Libbitcoin;
import com.coinninja.bindings.LibbitcoinTestnet;

public class MainActivity extends AppCompatActivity {
    String[] wordList = {"abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "abandon", "about"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        final Libbitcoin libbitcoin = new Libbitcoin();

        String[] seedWords = libbitcoin.getSeedWords();
        String externalChangeAddress = libbitcoin.getExternalChangeAddress(0);

        String ext = libbitcoin.getInternalChangeAddress(0);
        boolean isTestnet = libbitcoin.isTestnet();


        final Libbitcoin libbitcoinTestnet = new LibbitcoinTestnet();

        String[] seedWords2 = libbitcoinTestnet.getSeedWords();
        String externalChangeAddress2 = libbitcoinTestnet.getExternalChangeAddress(0);

        String ext2 = libbitcoinTestnet.getInternalChangeAddress(0);
        boolean isTestnet2 = libbitcoinTestnet.isTestnet();


        StringBuilder stringBuilder = new StringBuilder();

        for(String seedWord : seedWords){
            stringBuilder.append(seedWord);
            stringBuilder.append(" :: ");
        }

        stringBuilder.append(" :: "+externalChangeAddress);

        tv.setText(stringBuilder.toString());

        Button button = findViewById(R.id.get_balance);
    }
}
