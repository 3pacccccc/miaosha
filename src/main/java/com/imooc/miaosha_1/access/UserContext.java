package com.imooc.miaosha_1.access;

import com.imooc.miaosha_1.domain.MiaoshaUser;

public class UserContext {

    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static  void setUserHolder(MiaoshaUser miaoshaUser){
        userHolder.set(miaoshaUser);
    }

    public static MiaoshaUser getUserHolder(){
        return userHolder.get();
    }
}
