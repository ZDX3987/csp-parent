package com.zdx.csp.common.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 */
public class CookieUtil {

    public static void addCookie(HttpServletResponse resp, String name, String value,
                                 int maxAge) {
        Cookie cookie = new Cookie(name, value);
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        resp.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse resp, String name) {
        if (!StringUtils.isEmpty(name)) {
            Cookie cookie = new Cookie(name, null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        }
    }

    public static String getValue(HttpServletRequest req, String name) {
        if (!StringUtils.isEmpty(name)) {
            Cookie[] cookies = req.getCookies();
            for (Cookie cookie:cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
