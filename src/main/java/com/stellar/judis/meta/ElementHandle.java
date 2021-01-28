package com.stellar.judis.meta;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/28 14:24
 */
public class ElementHandle {
    private String opt;
    private String clazz;
    private String body;
    private String param;
    private String split = ",";

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }
}
