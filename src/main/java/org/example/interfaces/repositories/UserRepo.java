package org.example.interfaces.repositories;

import org.example.models.User;

import java.util.List;

public interface UserRepo {
    List<User> getAll();
    User getUserById(int id);
    void add(User user);
    void delete(int id);
    boolean save();
    void read();
    User getByLogin(String login);
}
