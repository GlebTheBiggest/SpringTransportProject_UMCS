package org.example.interfaces.repositories;

import org.example.models.User;

import java.util.List;

public interface UserRepo {
    List<User> getAll();
    User getUserById(String id);
    boolean add(User user);
    boolean remove(String id);
    boolean save();
    void read();
    User getByLogin(String login);
}
