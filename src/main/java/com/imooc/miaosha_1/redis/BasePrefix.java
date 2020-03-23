package com.imooc.miaosha_1.redis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public int expireSeconds() {
        return expireSeconds;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

}
