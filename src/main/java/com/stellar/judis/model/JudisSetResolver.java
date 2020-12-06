package com.stellar.judis.model;

import com.stellar.judis.server.JudisCoreBusiness;

/**
 * Created by wanglecheng on 2020/12/6.
 */
public class JudisSetResolver implements Resolver {
    @Override
    public String resolve(String... args) {
        return doResolve(args);
    }

    private String doResolve(String... args) {
        return JudisCoreBusiness.SET.invoke(args);
    }
}
