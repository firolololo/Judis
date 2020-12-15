namespace java com.stellar.judis.rpc

enum Operation {
    GET = 1,
    SET = 2
}

enum Type {
    REQUEST = 1,
    RESPONSE = 2,
    PING = 3,
    PONG = 4,
    EMPTY = 5
}

struct GetRequest {
    1: string key,
    2: Operation op = Operation.GET,
    3: Type tp = Type.REQUEST
}

struct SetRequest {
    1: string key,
    2: string value,
    3: i64 time = -1,
    4: Operation op = Operation.SET,
    5: Type tp = Type.REQUEST
}

struct GetResponse {
    1: bool success,
    2: string value,
    3: Operation op = Operation.GET,
    4: Type tp = Type.RESPONSE
}

struct SetResponse {
    1: bool success,
    2: string oldValue,
    3: Operation op = Operation.SET,
    4: Type tp = Type.RESPONSE
}

service Command {
    GetResponse getValue(1:string key),
    SetResponse setValue(1:string key, 2:string value, 3:i64 time = -1, 4:bool isPresent = true),
    list<GetResponse> getValueBatch(1:list<GetRequest> getRequests),
    list<SetResponse> setValueBatch(1:list<SetRequest> setRequests)
}

service Heartbeat {
    void ping()
}

