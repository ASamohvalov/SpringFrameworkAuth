package com.srt.SpringAuth.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.srt.SpringAuth.dao.AuthDao;
import com.srt.SpringAuth.dto.SignInRequest;
import com.srt.SpringAuth.dto.jwt.JwtAuthDto;
import com.srt.SpringAuth.exceptions.AuthenticationFailedException;
import com.srt.SpringAuth.models.User;
import com.srt.SpringAuth.utils.PBKDF2PasswordEncoder;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignInService {
    private final AuthDao authDao;
    private final PBKDF2PasswordEncoder passwordEncoder;
    private final Validator validator;
    private final AuthenticationService authenticationService;

    @Transactional
    public JwtAuthDto signIn(SignInRequest request) throws AuthenticationFailedException {
        User user = authDao.findByUsername(request.getUsername());
        if (user == null || !passwordEncoder.verify(request.getPassword(), user.getPassword())) {
            throw new AuthenticationFailedException("incorrect password or username");
        }

        return authenticationService.authenticate(user); 
    }

    public Map<String, List<String>> validate(SignInRequest request) {
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(request);
        return constraintViolations.stream()
                .collect(Collectors.groupingBy(
                        key -> key.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));
    }
}
