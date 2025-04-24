package org.example.impls.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.dao.UserDao;
import org.example.interfaces.repositories.UserRepo;
import org.example.models.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import static org.example.app.utils.HibernateUtil.getSessionFactory;

public class UserRepoImpl implements UserRepo {
    //private final List<User> users;
    private static final String CSV_FILE_NAME = "users.csv";
    private static final String JSON_FILE_NAME = "users.json";
    private final UserDao userDao;

    public UserRepoImpl() {
        //this.users = new ArrayList<>();
        this.userDao = new UserDao(getSessionFactory());
        /*Path path = Paths.get(JSON_FILE_NAME);
        if (Files.exists(path)) {
            readJson();
        }*/
        //users.addAll(userDao.findAll());
    }

    @Override
    public User getByLogin(String login) {
        /*return this.users.stream()
                .filter(user -> Objects.equals(user.getLogin(), login))
                .findFirst()
                .orElse(null);*/
        return userDao.findByLogin(login);
    }

    @Override
    public List<User> getAll() {
        /*List<User> usersCopy = new ArrayList<>();
        for (User user : this.users) {
            usersCopy.add(user.cloneUser());
        }
        return usersCopy;*/
        return userDao.findAll();
    }

    @Override
    public User getById(String id) {
        /*return this.users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);*/
        return userDao.findById(id);
    }

    @Override
    public boolean add(User user) {
        /*for (User u : userDao.findAll()) {
            if (user.getId().equals(u.getId())) {
                return false;
            }
        }
        this.users.add(user);
        return true;*/

        if (userDao.findById(user.getId()) != null) {
            return false;
        }
        userDao.save(user);
        return true;
    }

    @Override
    public boolean remove(String id) {
        //return this.users.removeIf(user -> user.getId().equals(id));
        if (userDao.findById(id) == null) {
            return false;
        } else {
            userDao.delete(userDao.findById(id));
            return true;
        }
    }

    @Override
    public void removeAll() {
        userDao.deleteAll();
    }

    @Override
    public void saveCsv() {
        List<String> lines = new ArrayList<>();
        Path file = Paths.get(CSV_FILE_NAME);

        for (User user : userDao.findAll()) {
            StringBuilder sb = new StringBuilder();
            sb.append("User(")
                    .append("id=").append(user.getId()).append("; ")
                    .append("name=").append(user.getLogin()).append("; ")
                    .append("password=").append(user.getPassword()).append("; ")
                    .append("role=").append(user.getRole())
                    .append(")");
            lines.add(sb.toString());
        }

        try {
            Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public void readCsv() {
        Path path = Paths.get(CSV_FILE_NAME);

        try (Scanner input = new Scanner(path, StandardCharsets.UTF_8)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) continue;

                try {
                    if (!line.startsWith("User(") || !line.endsWith(")")) {
                        System.err.println("Invalid format: " + line);
                        continue;
                    }

                    line = line.substring(5, line.length() - 1); // Видаляємо "User(" та ")"
                    String[] parts = line.split("; ");
                    String id = null, name = null, password = null, role = null;

                    for (String part : parts) {
                        String[] keyValue = part.split("=", 2);
                        if (keyValue.length != 2) {
                            System.err.println("Invalid key-value format: " + part);
                            continue;
                        }

                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();

                        switch (key) {
                            case "id" -> id = value;
                            case "name" -> name = value;
                            case "password" -> password = value;
                            case "role" -> role = value;
                        }
                    }

                    //users.add(new User(id, name, password, role));
                    userDao.save(new User(id, name, password, role));
                } catch (Exception e) {
                    System.err.println("Error parsing user: " + line + ", reason: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void saveJson() {
        Path file = Paths.get(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), userDao.findAll());
        } catch (IOException e) {
            System.err.println("Error saving JSON file: " + e.getMessage());
        }
    }

    @Override
    public void readJson() {
        Path path = Paths.get(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<User> loadedUsers = objectMapper.readValue(
                    path.toFile(),
                    new TypeReference<>() {
                    }
            );
            // this.users.clear();
            // this.users.addAll(loadedUsers);
            for (User user : loadedUsers) {
                userDao.save(user);
            }

        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unforgiven error: " + e.getMessage());
        }
    }
}
