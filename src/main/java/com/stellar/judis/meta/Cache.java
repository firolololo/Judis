package com.stellar.judis.meta;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 15:37
 */
public class Cache implements ICache<String, JudisElement> {
    public static class ExpireV {
        private JudisElement value;
        private LocalDateTime expire;
        public ExpireV(JudisElement value, LocalDateTime expire) {
            this.value = value;
            this.expire = expire;
        }

        public JudisElement getValue() {
            return this.value;
        }

        public LocalDateTime getExpire() {
            return this.expire;
        }

        public boolean isExpired() {
            if (expire == null) return false;
            return expire.isBefore(LocalDateTime.now());
        }
    }

    private Map<String, ExpireV> map = new HashMap<>();

    @Override
    public JudisElement get(String key) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            ExpireV v = map.get(key);
            if (v.expire == null)
                return v.value;
            else {
                // 过期
                if (v.expire.isBefore(LocalDateTime.now())) {
                    map.remove(key);
                    return null;
                }
                return v.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public JudisElement put(String key, JudisElement value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        ExpireV v = new ExpireV(value, null);
        ExpireV ex = map.put(key, v);
        return Optional.ofNullable(ex)
                .map(ev -> ev.value)
                .orElse(null);
    }

    @Override
    public JudisElement put(String key, JudisElement value, long times, TimeUnit unit) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        ExpireV v = new ExpireV(value, LocalDateTime.now().plusMinutes(unit.toMinutes(times)));
        ExpireV ex = map.put(key, v);
        return Optional.ofNullable(ex)
                .map(ev -> ev.value)
                .orElse(null);
    }

    @Override
    public LocalDateTime getExpireTime(String key) {
        Objects.requireNonNull(key);
        if (!map.containsKey(key))
            return null;
        ExpireV v = map.get(key);
        if (v.expire == null)
            return null;
        final LocalDateTime now = LocalDateTime.now();
        if (v.expire.isBefore(now))
            return null;
        return v.expire;
    }

    @Override
    public JudisElement put(String key, JudisElement value, LocalDateTime localDateTime) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        if (localDateTime == null)
            return put(key, value);
        else {
            if (localDateTime.isBefore(LocalDateTime.now()))
                return null;
            else {
                ExpireV v = new ExpireV(value, localDateTime);
                ExpireV ex = map.put(key, v);
                return Optional.ofNullable(ex)
                        .map(ev -> ev.value)
                        .orElse(null);
            }
        }
    }

    @Override
    public JudisElement remove(String key) {
        Objects.requireNonNull(key);
        ExpireV ex = map.remove(key);
        return Optional.ofNullable(ex)
                .map(ev -> ev.value)
                .orElse(null);
    }

    public Set<Map.Entry<String, ExpireV>> entry() {
        return map.entrySet();
    }
}
