package com.imooc.miaosha_1.redis;

public class MiaoshaKey extends BasePrefix {
    private MiaoshaKey(String prefix) {
        super(prefix);
    }

    public MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");

    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mp");

    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");

}
