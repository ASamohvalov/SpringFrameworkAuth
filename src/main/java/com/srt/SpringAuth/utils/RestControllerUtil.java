package com.srt.SpringAuth.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestControllerUtil {

    public boolean validatePostRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().write("method not allowed");
            return false;
        }
        return true;
    }

    public boolean isRequestDataNull(String requestData, HttpServletResponse response) 
            throws Exception {
        if (requestData == null || requestData == "") {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("request data is empty");
            return true;
        }
        return false;
    }
}
