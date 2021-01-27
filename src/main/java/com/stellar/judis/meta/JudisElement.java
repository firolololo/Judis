package com.stellar.judis.meta;

import com.stellar.judis.rpc.CommandType;

import java.time.LocalDateTime;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/12 16:15
 */
public class JudisElement implements Comparable<JudisElement>{
    private CommandType type;
    private IJudisElementType typeImpl;
    private LocalDateTime lastVisitTime;

    public JudisElement() {
        this.lastVisitTime = LocalDateTime.now();
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public IJudisElementType getTypeImpl() {
        return typeImpl;
    }

    public void setTypeImpl(IJudisElementType typeImpl) {
        this.typeImpl = typeImpl;
    }

    public LocalDateTime getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(LocalDateTime lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    @Override
    public int compareTo(JudisElement o) {
        return this.lastVisitTime.compareTo(o.getLastVisitTime());
    }
}
