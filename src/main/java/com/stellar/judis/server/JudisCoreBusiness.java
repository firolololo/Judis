package com.stellar.judis.server;

import com.stellar.judis.ExpireHashMap;
import com.stellar.judis.exception.JudisCoreException;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wanglecheng on 2020/12/6.
 */
public enum JudisCoreBusiness {
    GET("key") {
        @Override
        public String invoke(String... args) throws Exception {
            if (args.length != getParams().size() || args[0] == null) throw new JudisCoreException();
            return map.get(args[0]);
        }
    },


    SET("key", "value", "EX", "[NX|XX]") {
        @Override
        public String invoke(String... args) throws Exception {
            if (args.length != getParams().size() || args[0] == null || args[1] == null) throw new JudisCoreException();
            if (KEY_NOT_EXIST.equals(args[3])) {
                if (map.get(args[0]) != null) throw new JudisCoreException();
            }
            if (args[2] == null) return map.put(args[0], args[1]);
            if (numberPredicate(args[2])) {
                map.putExpire(args[0], args[1], Long.parseLong(args[2]), ChronoUnit.SECONDS);
            }
            return INVOKE_SUCCESS;
        }
    };

    JudisCoreBusiness(String... params) {
        this.params = new ArrayList<>(params.length);
        this.params.addAll(Arrays.asList(params));
    }

    public abstract String invoke(String... args) throws Exception;

    public boolean numberPredicate(String str) {
        if (str == null) return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }

    public List<String> getParams() {return params;}

    private List<String> params;
    private static final String KEY_NOT_EXIST = "NX";
//    private static final String KEY_EXIST = "XX";
    private static final String INVOKE_SUCCESS = "OK";;
    private static ExpireHashMap<String, String> map  = new ExpireHashMap<>();
}
