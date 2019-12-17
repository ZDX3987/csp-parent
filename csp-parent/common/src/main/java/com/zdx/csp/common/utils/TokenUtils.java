package com.zdx.csp.common.utils;

import com.zdx.csp.common.constants.Constans;

import java.util.UUID;

public class TokenUtils {

    /**
     * 返回User登录时的Token
     * @return
     */
    public static String getUserToken() {
        return Constans.TOKRN_USER + "-" + UUID.randomUUID();
    }
}
