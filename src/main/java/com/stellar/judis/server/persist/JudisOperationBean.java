package com.stellar.judis.server.persist;

import com.stellar.judis.exception.JudisCoreException;
import com.stellar.judis.meta.Cache;
import com.stellar.judis.meta.ElementHandle;
import com.stellar.judis.meta.JudisElement;
import com.stellar.judis.util.JsonUtil;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author firo
 * @version 1.0
 * @date 2020/12/30 17:31
 */
public enum JudisOperationBean {
    PUT("put") {
        @Override
        public void invoke(Cache cache, JudisElement element, String... args) throws JudisCoreException{
            if (args.length == 1)
                cache.put(args[0], element);
            else if (args.length == 2 && localDateTimePredicate(args[1]))
                cache.put(args[0], element, LocalDateTime.parse(args[1]));
            else
                throw new JudisCoreException();
        }
    },
    DELETE("delete") {
        @Override
        public void invoke(Cache cache, JudisElement element, String... args) throws JudisCoreException {
            if (args.length == 1)
                cache.remove(args[0]);
            else
                throw new JudisCoreException();
        }
    };
    private String operation;

    JudisOperationBean(String operation) {
        this.operation = operation;
    }
    private static final String OPERATION_JSON_OPERATION_SPLIT = ",";
    private static final String OPERATION_JSON_OPERATION_DESERIALIZE = "deserialize";

    public String parse(String className, JudisElement element, String... args) {
        if (null == className || className.equals("")) throw new RuntimeException("Invalid class name");
        if (element == null) throw new RuntimeException("Invalid element");
        ElementHandle handle = new ElementHandle();
        handle.setOpt(operation);
        handle.setClazz(className);
        handle.setBody(element.getTypeImpl().serialize());
        if (args.length == 0) throw new RuntimeException("Invalid param numbers");
        handle.setParam(args.length == 1 ? args[0] : String.join(OPERATION_JSON_OPERATION_SPLIT, args));
        return JsonUtil.jsonToString(handle);
    }

    abstract void invoke(Cache cache, JudisElement element, String... args) throws JudisCoreException;

    public static void execute(Cache cache, String jsonObject) {
        try {
            ElementHandle handle = JsonUtil.stringToJson(jsonObject, ElementHandle.class);
            if (handle == null) return;
            String opt = handle.getOpt();
            String className = handle.getClazz();
            String body = handle.getBody();
            String params = handle.getParam();
            JudisElement element;
            if (!judisElementMap.containsKey(className)) {
                Class<?> clazz = Class.forName(className);
                Method method = clazz.getDeclaredMethod(OPERATION_JSON_OPERATION_DESERIALIZE, String.class);
                element = (JudisElement)method.invoke(clazz.newInstance(), body);
                judisElementMap.put(className, element);
            } else {
                element = judisElementMap.get(className);
            }
            String[] args = params.split(OPERATION_JSON_OPERATION_SPLIT);
            for (JudisOperationBean operationBean: values()) {
                if (operationBean.operation.equals(opt)) {
                    operationBean.invoke(cache, element, args);
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Execute command error");
        }
    }

    public static List<String> parseCache(Cache cache) {
        List<String> records = new LinkedList<>();
        final LocalDateTime now = LocalDateTime.now();
        for(Map.Entry<String, Cache.ExpireV> entry: cache.entry()) {
            Cache.ExpireV v = entry.getValue();
            JudisElement element = v.getValue();
            String className = element.getClass().getName();
            LocalDateTime localDateTime = v.getExpire();
            if (localDateTime == null) {
                records.add(PUT.parse(className, element, entry.getKey()));
            } else if(now.isBefore(localDateTime)) {
                records.add(PUT.parse(className, element, entry.getKey(), localDateTime.toString()));
            }
        }
        return records;
    }

    private static final Map<String, JudisElement> judisElementMap = new HashMap<>();

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
