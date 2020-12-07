package com.stellar.judis.model;

import com.stellar.judis.server.JudisCoreBusiness;

import java.util.Arrays;

/**
 * Created by wanglecheng on 2020/12/6.
 */
public class JudisSetResolver implements Resolver {


    @Override
    public String resolve(String... args) {
        return doSet(args);
    }

    private String doSet(String... args) {
        String[] paddingArgs = Arrays.copyOf(args, JudisCoreBusiness.SET.getParams().size());
        System.out.println(Arrays.toString(paddingArgs));
        return JudisCoreBusiness.SET.invoke(paddingArgs);
    }
}
