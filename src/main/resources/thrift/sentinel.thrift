namespace java com.stellar.judis.rpc

enum Type {
    PING = 1,
    PONG = 2,
    INSTRUCTION = 3
}

enum Directive {
    UPDATE = 1,
    SNAPSHOT = 2,
    MASTER = 3
 }

struct Ping {
    1:Type tp = Type.PING,
    2:required string address,
    3:required i32 port,
    4:required string body
}

struct Pong {
    1:Type tp = Type.PONG,
    2:required string address,
    3:required i32 port,
    4:required string body
}

struct Instruction {
    1:required Directive directive
}

struct Answer {
    1:bool success = false,
    2:string body
}

service SentinelOtherNode {
    Answer ping(1:Ping message),
    Answer pong(1:Pong message),
    Answer instruction(1:Instruction message)
}