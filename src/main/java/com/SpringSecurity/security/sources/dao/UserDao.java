package com.SpringSecurity.security.sources.dao;

import com.SpringSecurity.security.sources.model.User;


import java.util.List;
import java.util.Optional;

public interface UserDao {
    public void addUserToDatabase(User user);

    public List<User> getAllUsersFromDatabase();

    public void deleteUserFromDatabase(long id);

    public Optional<User> getUserByIdFromDatabase(long id);

     public void updateUserInDatabase(User user);

    Optional<User> loadUserByUsername(String username);
}

