namespace java com.stellar.judis.rpc

enum CommandType {
    CONNECT = 1,
    STRING = 2,
    HASH = 3,
    SORTEDSET = 4
}

struct CommandResponse {
    1:bool success,
    2:CommandType commandType,
    3:string data
}

struct ConnectPair {
    1:string address,
    2:i32 port
}

struct StringPair {
    1:string key,
    2:string value
}

struct SortedSetPair {
    1:double score,
    2:string member
}

enum BitOption {
    AND = 1, OR = 2, NOT = 3, XOR = 4
}

service ClientConnectCommand {
    CommandResponse connect(1:required ConnectPair clientInfo)
}

service ClientStringCommand {
    CommandResponse getString(1:required string key),
    CommandResponse setString(1:required string key, 2:required string value, 3:i64 time = -1, 4:bool isPresent = true),
    CommandResponse appendString(1:required string key, 2:required string value),
    CommandResponse mgetString(1:required list<string> keys),
    CommandResponse msetString(1:required list<StringPair> pairs),
    CommandResponse incr(1:required string key),
    CommandResponse incrBy(1:required string key, 2:i64 increment = 1),
    CommandResponse decr(1:required string key),
    CommandResponse decrBy(1:required string key, 2:i64 decrement = 1),
    CommandResponse getBit(1:required string key, 2:required i32 offset),
    CommandResponse setBit(1:required string key, 2:required i32 offset, 3:required string value),
    CommandResponse countBit(1:required string key, 2:i32 start = 0, 3:i32 stop = -1),
    CommandResponse topBit(1:required BitOption option, 2:required string key, 3:required list<string> keys)
}

service ClientSortedSetCommand {
    CommandResponse zadd(1:required string key, 2:required list<SortedSetPair> element),
    CommandResponse zcard(1:required string key),
    CommandResponse zcount(1:required string key, 2:required double min, 3:required double max),
    CommandResponse zincrBy(1:required string key, 2:required double increment, 3:required string member),
    CommandResponse zrem(1:required string key, 2:required list<string> members),
    CommandResponse zrange(1:required string key, 2:i32 start = 0, 3:i32 stop = -1, 4:bool withScores = false),
    CommandResponse zrangeByScore(1:required string key, 2:required double min, 3:required double max, 4:bool withScores = false, 5:i32 limit),
    CommandResponse zremRangeByRank(1:required string key, 2:i32 start = 0, 3:i32 stop = -1),
    CommandResponse zremRangeByScore(1:required string key, 2:required double min, 3:required double max),
    CommandResponse zscore(1:required string key, 2:required string member)
}