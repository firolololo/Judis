package com.stellar.judis.model;

import java.util.Arrays;

/**
 * Created by wanglecheng on 2020/12/6.
 */
public enum ResolverFactory implements Resolver {
    SET_RESOLVER("SET", new JudisSetResolver()),
    GET_RESOLVER("GET", new JudisGetResolver());

    private String command;
    private Resolver resolver;

    ResolverFactory(String command, Resolver resolver) {
        this.command = command;
        this.resolver = resolver;
    }

    @Override
    public String resolve(String... args) throws Exception {
        return this.resolver.resolve(args);
    }

    public static String dispatch(String command, String... args) throws Exception {
        for (ResolverFactory resolver: values()) {
            if (resolver.command.equals(command)) {
                return resolver.resolve(args);
            }
        }
        throw new RuntimeException("unsupported command:" + command);
    }

    public static String getCommand(String instruction) {
        String[] strs = instruction.split(JudisSymbol.SEG_SIGN.getSymbol());
        if (strs.length > 0) return strs[0];
        throw new RuntimeException("unsupported instruction:" + instruction);
    }

    public static String[] getParams(String instruction) {
        String[] strs = instruction.split(JudisSymbol.SEG_SIGN.getSymbol());
        if (strs.length > 0) return Arrays.copyOfRange(strs, 1, strs.length);
        throw new RuntimeException("unsupported instruction:" + instruction);
    }
}
