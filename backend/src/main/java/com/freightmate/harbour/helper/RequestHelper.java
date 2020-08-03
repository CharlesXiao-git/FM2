package com.freightmate.harbour.helper;

import org.apache.logging.log4j.util.Strings;

import javax.servlet.http.HttpServletRequest;

public class RequestHelper {

    public static String extractRequestIp(HttpServletRequest request){
        String forwaredFor = request.getHeader("X-FORWARDED-FOR");
        String originIP = request.getRemoteAddr();

        return Strings.isBlank(forwaredFor) ? originIP : forwaredFor;
    }
}
