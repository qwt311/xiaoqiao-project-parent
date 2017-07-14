package com.xiaoqiao.web.controller.base;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qiaowentao on 2017/7/13.
 */
public class BaseController {

    /**
     * 获取客户端ip地址<br/>
     * eg.remoteAddr有可能为ngnix的ip, x-forwarded-for为ngnix转发的客户端真实ip地址;<br/>
     *
     * @param request
     * @return
     * @author niulu
     * @date 2013-11-22
     */
    public static String getRemoteIP(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getRemoteAddr());
        String forwardedIp = request.getHeader("x-forwarded-for");
        if (null != forwardedIp && !"unknown".equalsIgnoreCase(forwardedIp)) {
            builder.append(",").append(forwardedIp);
        }
        builder.trimToSize();
        return builder.toString();
    }

}
