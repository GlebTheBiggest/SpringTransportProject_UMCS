package org.example.interfaces.repositories;

import org.example.models.User;

import java.util.List;

public interface UserRepo {
    List<User> getAll();
    User getById(String id);
    boolean add(User user);
    boolean remove(String id);
    void saveCsv();
    void readCsv();
    User getByLogin(String login);
    void saveJson();
    void readJson();
    void removeAll();
}
