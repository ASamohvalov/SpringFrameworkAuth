package com.srt.SpringAuth.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.srt.SpringAuth.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestControllerUtil {
    private final AuthenticationService authenticationService;

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

    public boolean isJsonValid(HttpServletResponse response, String json, JsonMapper jsonMapper)
            throws Exception {
        try {
            jsonMapper.readTree(json);
        } catch (JacksonException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("invalid json request");
            return false;
        }
        return true;
    }

    public boolean isAuthenticate(String jwt, HttpServletResponse response)
            throws Exception {
        if (!authenticationService.validateJwt(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("user is not authenticate");
            return false;
        }
        return true;
    }
}
