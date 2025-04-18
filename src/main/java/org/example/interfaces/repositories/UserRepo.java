package org.example.interfaces.repositories;

import org.example.models.User;

import java.util.List;

public interface UserRepo {
    List<User> getAll();
    User getUserById(String id);
    boolean add(User user);
    boolean remove(String id);
    boolean saveCsv();
    void readCsv();
    User getByLogin(String login);
    boolean saveJson();
    void readJson();
}
