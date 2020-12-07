package com.stellar.judis.model;

import com.stellar.judis.server.JudisCoreBusiness;

/**
 * Created by wanglecheng on 2020/12/7.
 */
public class JudisGetResolver implements Resolver {
    @Override
    public String resolve(String... args) {
        return doGet(args);
    }

    public String doGet(String... args) {
        return JudisCoreBusiness.GET.invoke(args);
    }
}
