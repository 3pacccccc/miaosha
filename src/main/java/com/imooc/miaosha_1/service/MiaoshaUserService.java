package com.imooc.miaosha_1.service;

import com.imooc.miaosha_1.dao.MiaoshaUserDao;
import com.imooc.miaosha_1.domain.MiaoshaUser;
import com.imooc.miaosha_1.domain.User;
import com.imooc.miaosha_1.exception.GlobalException;
import com.imooc.miaosha_1.redis.MiaoshaUserKey;
import com.imooc.miaosha_1.redis.RedisService;
import com.imooc.miaosha_1.result.CodeMsg;
import com.imooc.miaosha_1.util.MD5Util;
import com.imooc.miaosha_1.util.UUIDUtil;
import com.imooc.miaosha_1.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

    public MiaoshaUser getById(Long id) {
        return miaoshaUserDao.getById(id);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        // 延长有效期
        if (miaoshaUser != null) {
            addCookie(response, token, miaoshaUser);
        }
        return miaoshaUser;

    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        // 判断手机号是否存在
        MiaoshaUser miaoshaUser = miaoshaUserDao.getById(Long.parseLong(mobile));
        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 验证密码
        String dbPass = miaoshaUser.getPassword();
        String saltDB = miaoshaUser.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        // 生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, miaoshaUser);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
