package org.example.impls.services.repoServices;

import lombok.Data;
import org.example.interfaces.repositories.UserRepo;
import org.example.interfaces.services.UserRepoService;

@Data
public class UserRepoServiceImpl implements UserRepoService {
    private final UserRepo userRepo;

    public UserRepoServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
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
