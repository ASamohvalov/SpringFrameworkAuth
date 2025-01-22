package com.srt.SpringAuth.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.srt.SpringAuth.dto.SignUpRequest;
import com.srt.SpringAuth.exceptions.UsernameAlreadyTakenException;
import com.srt.SpringAuth.services.SignUpService;
import com.srt.SpringAuth.utils.RestControllerUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignUpController implements Controller {
    private final JsonMapper jsonMapper;
    private final SignUpService signUpService;
    private final RestControllerUtil controllerUtil;

    @SuppressWarnings("null")
    @Override
    @Nullable
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("application/json");

        String requestData = request.getReader().lines().collect(Collectors.joining());
        if (controllerUtil.validatePostRequest(request, response) &&
                !controllerUtil.isRequestDataNull(requestData, response) &&
                controllerUtil.isJsonValid(response, requestData, jsonMapper)) {

            SignUpRequest signUpRequest = jsonMapper.readValue(requestData, SignUpRequest.class);
            
            Map<String, List<String>> validation = signUpService.validate(signUpRequest);
            if (validation.size() > 0) {
                String json = jsonMapper.writeValueAsString(validation);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(json);
                return null;
            }
            try {
                signUpService.signUp(signUpRequest);
                response.getWriter().write("user successfully registered");
            } catch (UsernameAlreadyTakenException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
        }

        return null;
    }
}
