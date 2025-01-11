package com.srt.SpringAuth.dao;

import com.srt.SpringAuth.models.User;

public interface AuthDao {
    boolean isUsernameExists(String username);
    void save(User user); 
    boolean authenticate(String username, String password); 
    String getPassword(String username);
}
