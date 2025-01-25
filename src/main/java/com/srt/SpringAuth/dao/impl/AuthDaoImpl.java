package com.srt.SpringAuth.dao.impl;

import java.util.List;

import com.srt.SpringAuth.dao.AuthDao;
import com.srt.SpringAuth.models.User;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthDaoImpl implements AuthDao {
    private final EntityManager entityManager;

    @Override
    public boolean isUsernameExists(String username) {
        Long count = (Long) entityManager
                .createQuery("select count(u) from User u where u.username = :username")
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public String getPassword(String username) {
        List<String> passwords = entityManager
                .createQuery("select u.password from User u where u.username = :username", String.class)
                .setParameter("username", username)
                .getResultList();

        return passwords.isEmpty() ? null : passwords.get(0);
    } 

    @Override
    public User findByUsername(String username) {
        List<User> result = entityManager
                .createQuery("select u from User u where username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }
}
