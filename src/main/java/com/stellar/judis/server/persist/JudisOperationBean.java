package com.stellar.judis.server.persist;

import com.alibaba.fastjson.JSONObject;
import com.stellar.judis.exception.JudisCoreException;
import com.stellar.judis.server.core.CoreOperation;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 17:31
 */
public enum JudisOperationBean {
    PUT("put") {
        @Override
        public void invoke(CoreOperation<String, String> core, String... args) throws JudisCoreException{
            if (args.length == 2)
                core.put(args[0], args[1]);
            else if (args.length == 3 && localDateTimePredicate(args[2]))
                core.put(args[0], args[1], LocalDateTime.parse(args[2]));
            else
                throw new JudisCoreException();
        }
    },
    DELETE("delete") {
        @Override
        public void invoke(CoreOperation<String, String> core, String... args) throws JudisCoreException {
            if (args.length == 1)
                core.delete(args[0]);
            else
                throw new JudisCoreException();
        }
    };
    private String operation;

    JudisOperationBean(String operation) {
        this.operation = operation;
    }

    private static final String OPERATION_JSON_OPERATION_NAME = "opt";
    private static final String OPERATION_JSON_OPERATION_PARAM = "param";
    private static final String OPERATION_JSON_OPERATION_SPLIT = ",";
    public String parse(String... args) {
        JSONObject object = new JSONObject();
        object.put(OPERATION_JSON_OPERATION_NAME, operation);
        if (args.length == 0) throw new RuntimeException("Invalid param numbers");
        object.put(OPERATION_JSON_OPERATION_PARAM, args.length == 1 ? args[0] : String.join(OPERATION_JSON_OPERATION_SPLIT, args));
        return JSONObject.toJSONString(object);
    }

    abstract public void invoke(CoreOperation<String, String> core, String... args) throws JudisCoreException;

    private static boolean numberPredicate(String str) {
        if (str == null) return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }

    private static boolean localDateTimePredicate(String time) {
        try {
            LocalDateTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
