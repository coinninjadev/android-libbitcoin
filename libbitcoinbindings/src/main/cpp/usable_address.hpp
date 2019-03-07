//
//  usable_address.hpp
//  android-libbitcoin
//
//  Created by Dan Sexton on 5/17/18.
//  Copyright © 2018 Coin Ninja, LLC. All rights reserved.
//

#ifndef usable_address_hpp
#define usable_address_hpp

#include "derivation_path.hpp"

class usable_address {
public:
    usable_address(wallet::hd_private privateKey, derivation_path path);

    wallet::payment_address buildPaymentAddress();
    ec_compressed buildCompressedPublicKey();
    script buildP2WPKH();
    wallet::hd_private buildPrivateKey();
    data_chunk getUncompressedPublicKey();

private:
    wallet::hd_private privateKey;
    derivation_path path = 0;
    bool testnet = false;
};

void signForInput(transaction &tx, int inputIndex, uint64_t amount, usable_address usableAddress);

#endif /* usable_address_hpp */
