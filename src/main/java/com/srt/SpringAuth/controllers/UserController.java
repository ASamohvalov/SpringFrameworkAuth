package com.srt.SpringAuth.controllers;

import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.srt.SpringAuth.dto.jwt.JwtAuthDto;
import com.srt.SpringAuth.utils.RestControllerUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserController implements Controller {
    private final RestControllerUtil controllerUtil;
    private final JsonMapper jsonMapper;

    @SuppressWarnings("null")
    @Override
    @Nullable
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        response.setContentType("application/json");

        String json = request.getReader().lines().collect(Collectors.joining());
        JwtAuthDto jwt;

        if (controllerUtil.validatePostRequest(request, response) && 
                controllerUtil.isJsonValid(response, json, jsonMapper) && 
                (jwt = controllerUtil.isAuthenticate(json, response)) != null) {
            response.getWriter().write(String.format("{ \"message\": \"hello user\", \"jwt\": %s }}", jwt.toString()));
        }
        return null;
    }
}
