package com.SpringSecurity.security.sources.dao;

import com.SpringSecurity.security.sources.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    /**пришлось убрать коструктор с параметром EntityManager потому что после перехода на SpringBoot
     * от нас ушёл файл конфигурации с определением бина EntityManager, и сейчас
     * в реализованном кострукторе с этим параметром идет мерзкое красное подчёркивание
     * бесит
     * */
   @PersistenceContext
    private   EntityManager entityManager;

    @Override
    public void addUserToDatabase(User user) {
        entityManager.persist(user);
    }

    @Override
    public List<User> getAllUsersFromDatabase() {
        TypedQuery<User> query = entityManager.createQuery("FROM User", User.class);
        return query.getResultList();

    }

    @Override
    public void deleteUserFromDatabase(long id) {
        entityManager.remove(getUserByIdFromDatabase(id).get());
    }

    @Override
    public Optional<User> getUserByIdFromDatabase(long id) {

        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public void updateUserInDatabase(User user) {
        entityManager.merge(user);
    }

    @Override
    public Optional<User> loadUserByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("select user FROM User user " +
                        "JOIN fetch user.roles roles " +
                        "where user.name = :username", User.class)
                .setParameter("username",username);
        return Optional.ofNullable(query.getSingleResult());
    }
}
