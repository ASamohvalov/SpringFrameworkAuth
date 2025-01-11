package com.srt.SpringAuth.controllers;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;
    private final SignInService signInService;

    @SuppressWarnings("null")
    @Override
    @Nullable
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");

        if (controllerUtil.validatePostRequest(request, response) ) {
            if (request.getReader() == null) {
                
                return null;
            }
            SignInRequest signInRequest = objectMapper.readValue(request.getReader(), SignInRequest.class);
            try {
                signInService.signIn(signInRequest);
                response.getWriter().write("user successfully authenticated");
            } catch (AuthenticationFailedException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
        }

        return null;
    }
}
