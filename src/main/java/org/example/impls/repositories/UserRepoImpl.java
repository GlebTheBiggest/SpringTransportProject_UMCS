package org.example.impls.repositories;

import org.example.interfaces.repositories.UserRepo;
import org.example.models.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class UserRepoImpl implements UserRepo {
    private final List<User> users;
    private static final String FILE_NAME = "users.csv";

    public UserRepoImpl() {
        this.users = new ArrayList<>();
        Path path = Paths.get(FILE_NAME);
        if (Files.exists(path)) {
            read();
        }
    }

    @Override
    public User getByLogin(String login) {
        return this.users.stream()
                .filter(user -> Objects.equals(user.getLogin(), login))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        List<User> usersCopy = new ArrayList<>();
        for (User user : this.users) {
            usersCopy.add(user.cloneUser());
        }
        return usersCopy;
    }

    @Override
    public User getUserById(String id) {
        return this.users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean add(User user) {
        for (User u : this.users) {
            if (user.getId().equals(u.getId())) {
                return false;
            }
        }
        this.users.add(user);
        save();
        return true;
    }

    @Override
    public boolean remove(String id) {
        boolean removed = this.users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            save();
        }
        return removed;
    }

    @Override
    public boolean save() {
        Path file = Paths.get(FILE_NAME);
        List<String> lines = new ArrayList<>();

        for (User user : this.users) {
            lines.add(user.toCsv());
        }

        try {
            Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void read() {
        Path path = Paths.get(FILE_NAME);
        try (Scanner input = new Scanner(path, StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 4) {
                    System.err.println("Invalid data format: " + line);
                    continue;
                }
                try {
                    String id = data[0];
                    String name = data[1];
                    String password = data[2];
                    String role = data[3];
                    users.add(new User(id, name, password, role));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
