package com.srt.SpringAuth.services;

import com.srt.SpringAuth.dao.AuthDao;
import com.srt.SpringAuth.dto.SignInRequest;
import com.srt.SpringAuth.exceptions.AuthenticationFailedException;
import com.srt.SpringAuth.utils.PBKDF2PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignInService {
    private final AuthDao authDao;
    private final PBKDF2PasswordEncoder passwordEncoder;

    public void signIn(SignInRequest request) throws AuthenticationFailedException  {

        String storedPassword = authDao.getPassword(request.getUsername());
        if (storedPassword == null || !passwordEncoder.verify(request.getPassword(), storedPassword)) {
            throw new AuthenticationFailedException("incorrect password or username");
        }
        
    }
}
