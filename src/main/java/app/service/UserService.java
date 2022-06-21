package app.service;

import app.model.User;

import java.util.List;

public interface UserService {
    List<User> showAllUsers();

    User showUser(Integer id);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(Integer id);

    User findUserByName(String name);

    String getCurrentUsername();

    User findByEmail(String email);
}
