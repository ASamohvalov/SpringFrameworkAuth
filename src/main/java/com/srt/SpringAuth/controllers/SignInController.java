package com.srt.SpringAuth.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.srt.SpringAuth.dto.SignInRequest;
import com.srt.SpringAuth.exceptions.AuthenticationFailedException;
import com.srt.SpringAuth.services.SignInService;
import com.srt.SpringAuth.utils.RestControllerUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignInController implements Controller {
    private final RestControllerUtil controllerUtil;
    private final JsonMapper jsonMapper;
    private final SignInService signInService;

    @SuppressWarnings("null")
    @Override
    @Nullable
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");

        String requestData = request.getReader().lines().collect(Collectors.joining());
        if (controllerUtil.validatePostRequest(request, response) &&
                !controllerUtil.isRequestDataNull(requestData, response) &&
                controllerUtil.isJsonValid(response, requestData, jsonMapper)) {

            SignInRequest signInRequest = jsonMapper.readValue(requestData, SignInRequest.class);

            Map<String, List<String>> validation = signInService.validate(signInRequest);
            if (validation.size() > 0) {
                String json = jsonMapper.writeValueAsString(validation);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(json);
                return null;
            }
            try {
                String jwt = jsonMapper.writeValueAsString(signInService.signIn(signInRequest));
                response.getWriter().write(
                        String.format("{ \"message\": \"user successfully authenticated\", \"jwt\": %s }", jwt)
                );
            } catch (AuthenticationFailedException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
        }

        return null;
    }
}
