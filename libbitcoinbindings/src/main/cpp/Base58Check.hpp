
#ifndef Base58Check_hpp
#define Base58Check_hpp

#ifdef __cplusplus

#include <bitcoin/bitcoin.hpp>

#endif

class Base58Check {
public:
    static bool addressIsBase58CheckEncoded(std::string address);
};

#endif /* Base58Check_hpp */
