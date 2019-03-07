package com.coinninja.bindings;

public class LibbitcoinTestnet extends Libbitcoin{

    public LibbitcoinTestnet() {
        super(true);
    }

    public LibbitcoinTestnet(String[] wordList) {
        super(wordList, true);
    }
}
