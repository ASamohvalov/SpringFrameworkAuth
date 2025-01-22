package com.srt.SpringAuth.dao;

import com.srt.SpringAuth.models.User;

public interface AuthDao {
    boolean isUsernameExists(String username);
    void save(User user); 
    String getPassword(String username);
    User findByUsername(String username);
}
