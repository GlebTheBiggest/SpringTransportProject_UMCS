package org.example.impls.services;

import org.example.interfaces.repositories.UserRepo;
import org.example.interfaces.services.UserRepoService;

public class UserRepoServiceImpl implements UserRepoService {
    private final UserRepo userRepo;

    public UserRepoServiceImpl(UserRepo userRepo) {
        if (userRepo == null) {
            throw new IllegalArgumentException("UserRepo cannot be null!");
        }
        this.userRepo = userRepo;
    }

    @Override
    public UserRepo getUserRepo() {
        return this.userRepo;
    }

    @Override
    public void printAllUsers() {
        if (userRepo.getAll().isEmpty()) {
            System.out.println("No users found!");
        } else {
            userRepo.getAll().forEach(System.out::println);
        }
    }
}
