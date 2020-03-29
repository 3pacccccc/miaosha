package com.imooc.miaosha_1.util;

import com.imooc.miaosha_1.service.MiaoshaUserService;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class UserUtil {


    /**
     * 从前端传递过来的get 请求参数或者cookie中获取MiaoshaUser对象
     *
     * @param request 前端传递过来的网络请求
     * @return 当前系统登录的MiaoshaUser对象
     */
    public static String getTokenByParamOrCookie(HttpServletRequest request) {
        String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);

        Cookie[] cookies = request.getCookies();
        String cookieToken = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(MiaoshaUserService.COOKIE_NAME_TOKEN)) {
                cookieToken = cookie.getValue();
            }
            if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
                return null;
            }

        }
        return StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

    }

}
