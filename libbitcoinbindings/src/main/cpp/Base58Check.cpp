
#include "Base58Check.hpp"

using namespace bc;

bool Base58Check::addressIsBase58CheckEncoded(std::string address) {
    data_chunk chunk;
    decode_base58(chunk, address);

    if (chunk.size() < 4) {
        return false;
    }

    data_chunk checksum_chunk;
    for (auto i = chunk.end() - 4; i != chunk.end(); ++i) {
        checksum_chunk.push_back(*i);
    }
    std::string last_four = encode_base16(checksum_chunk);

    data_chunk chunk_to_hash;
    for (auto i = chunk.begin(); i != chunk.end() - 4; ++i) {
        chunk_to_hash.push_back(*i);
    }
    hash_digest double_hashed_address = sha256_hash(sha256_hash(chunk_to_hash));

    data_chunk first_four_from_hash;
    for (auto i = double_hashed_address.begin(); i != double_hashed_address.begin() + 4; ++i) {
        first_four_from_hash.push_back(*i);
    }
    std::string first_four = encode_base16(first_four_from_hash);

    bool valid = (first_four == last_four);
    return valid;
}
