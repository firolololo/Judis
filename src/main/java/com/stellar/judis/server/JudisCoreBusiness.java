package com.stellar.judis.server;

import com.stellar.judis.ExpireHashMap;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wanglecheng on 2020/12/6.
 */
public enum JudisCoreBusiness {
    GET("key") {
        @Override
        public String invoke(String... args) {
            if (args.length != this.params.size() || args[0] == null) return INVOKE_FAIL;
            return (String)map.get(args[0]);
        }
    },


    SET("key", "value", "EX", "PX", "[NX|XX]") {
        @Override
        @SuppressWarnings("unchecked")
        public String invoke(String... args) {
            if (args.length != this.params.size() || args[0] == null || args[1] == null) return INVOKE_FAIL;
            if (KEY_NOT_EXIST.equals(args[4])) {
                if (map.get(args[0]) != null) return INVOKE_FAIL;
            }
            if (args[2] == null && args[3] == null) map.put(args[0], args[1]);
            if (numberPredicate(args[2])) {
                map.putExpire(args[0], args[1], Long.valueOf(args[2]), ChronoUnit.SECONDS);
            }
            if (numberPredicate(args[3])) {
                map.putExpire(args[0], args[1], Long.valueOf(args[3]), ChronoUnit.MILLIS);
            }
            return INVOKE_SUCCESS;
        }
    };

    JudisCoreBusiness(String... params) {
        this.params = new ArrayList<>(params.length);
        for (String param: params) {
            this.params.add(param);
        }
    }

    public abstract String invoke(String... args);

    public boolean numberPredicate(String str) {
        if (str == null) return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }
    private List<String> params;
    private static final String KEY_NOT_EXIST = "NX";
//    private static final String KEY_EXIST = "XX";
    private static final String INVOKE_SUCCESS = "OK";
    private static final String INVOKE_FAIL = "NIL";
    private static ExpireHashMap map  = new ExpireHashMap();
}
