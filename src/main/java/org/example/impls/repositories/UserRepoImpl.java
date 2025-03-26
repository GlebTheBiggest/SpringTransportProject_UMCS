package org.example.impls.repositories;

import org.example.interfaces.repositories.UserRepo;
import org.example.models.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class UserRepoImpl implements UserRepo {
    private final List<User> users;
    private int id = 0;
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
    public User getUserById(int id) {
        return this.users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void add(User user) {
        if (user.getId() == 0) {
            user.setId(++id);
        }
        this.users.add(user);
        save();
    }

    @Override
    public void delete(int id) {
        boolean removed = this.users.removeIf(user -> user.getId() == id);
        if (removed) {
            save();
        }
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
        int idLargest = 0;
        try (Scanner input = new Scanner(path, StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 5) {
                    System.err.println("Invalid data format: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0]);
                    int vehicleId = Integer.parseInt(data[1]);
                    String name = data[2];
                    String password = data[3];
                    String role = data[4];

                    users.add(new User(id, vehicleId, name, password, role));

                    if (id > idLargest) {
                        idLargest = id;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
            this.id = idLargest;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
