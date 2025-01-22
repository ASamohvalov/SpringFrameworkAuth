package com.srt.SpringAuth.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.srt.SpringAuth.dao.AuthDao;
import com.srt.SpringAuth.dto.SignUpRequest;
import com.srt.SpringAuth.exceptions.UsernameAlreadyTakenException;
import com.srt.SpringAuth.models.User;
import com.srt.SpringAuth.utils.PBKDF2PasswordEncoder;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignUpService {
    private final AuthDao authDao;
    private final PBKDF2PasswordEncoder passwordEncoder;
    private final Validator validator;

    public void signUp(SignUpRequest request) throws UsernameAlreadyTakenException {
        if (authDao.isUsernameExists(request.getUsername())) {
            throw new UsernameAlreadyTakenException("this username already taken");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        authDao.save(user);
    }

    public Map<String, List<String>> validate(SignUpRequest request) {
        Set<ConstraintViolation<SignUpRequest>> constraintViolations = validator.validate(request);
        return constraintViolations.stream()
                .collect(Collectors.groupingBy(
                        key -> key.getPropertyPath().toString(), 
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));
    }
}
