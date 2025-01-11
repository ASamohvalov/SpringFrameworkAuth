package com.srt.SpringAuth.dao.impl;

import com.srt.SpringAuth.dao.AuthDao;
import com.srt.SpringAuth.models.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class AuthDaoImpl implements AuthDao {
    @PersistenceContext
    private EntityManager entityManager;

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
    public boolean authenticate(String username, String password) {

        return false;
    }

    @Override
    public String getPassword(String username) {
        return entityManager
                .createQuery("select u u.password from User where username = :username")
                .setParameter("username", username)
                .getSingleResult().toString();
    } 
}
