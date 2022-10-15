package com.SpringSecurity.security.sources.service;

import com.SpringSecurity.security.sources.model.User;

import java.util.List;

public interface UserService {

    public void addUser(User user);

    public List<User> getAllUsers();

    public User getUser(long id);

    public void updateUser(User user);
    public void deleteUser(long id);
    public User getUserByName(String name);
}

