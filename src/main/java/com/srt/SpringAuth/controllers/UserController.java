package com.srt.SpringAuth.controllers;

import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
        
        if (controllerUtil.validatePostRequest(request, response) && 
                controllerUtil.isJsonValid(response, json, jsonMapper)) {
            JsonNode jsonNode = jsonMapper.readTree(json);
            if (!jsonNode.has("jwt")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("jwt not send");
                return null;
            }
            if (controllerUtil.isAuthenticate(jsonNode.get("jwt").asText(), response)) {
                response.getWriter().write("hello user");
            }
        }
        return null;
    }
}
