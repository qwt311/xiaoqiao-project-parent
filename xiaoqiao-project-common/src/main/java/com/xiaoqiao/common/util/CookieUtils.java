package com.xiaoqiao.common.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qiaowentao on 2017/7/13.
 */
public class CookieUtils {

    private static final int EXPIRY_TIME = 864000;

    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if(expiry < 0){
            expiry = EXPIRY_TIME;
        }
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }


    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                //if (StringUtil.equalsIgnoreCase(cookie.getName(), name)) {
                if (StringUtils.equalsIgnoreCase(cookie.getName(), name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
