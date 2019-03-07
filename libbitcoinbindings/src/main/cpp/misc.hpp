//
//  misc.hpp
//  android-libbitcoin
//
//  Created by Dan Sexton on 5/17/18.
//  Copyright © 2018 Coin Ninja, LLC. All rights reserved.
//

#ifndef misc_hpp
#define misc_hpp

#include "libbitcoin.hpp"
#include "derivation_path.hpp"

void createPayToScriptOutputFrom(transaction &tx, wallet::payment_address address, uint64_t amount);
void createPayToKeyOutputFrom(transaction &tx, wallet::payment_address address, uint64_t amount);
operation::list witnessProgram(ec_compressed publicKey);
wallet::hd_private childPrivateKey(wallet::hd_private privateKey, int index);
wallet::hd_private indexPrivateKeyForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path);
ec_compressed compressedPublicKeyForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path);
script P2WPKHForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path);
wallet::payment_address paymentAddressForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path);
void createInputFrom(transaction &tx, chain::point_value utxo, bool replaceable);
data_chunk uncompressedPublicKeyForHardenedDerivationPath(wallet::hd_private privateKey, derivation_path path) ;

#endif /* misc_hpp */
