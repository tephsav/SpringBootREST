package app.service;

import app.model.User;

import java.util.List;

public interface UserService {
    void createUser(User user);

    void deleteUser(Integer id);

    void updateUser(User user);

    User findUserById(Integer id);

    User findUserByName(String name);

    User findByEmail(String email);

    List<User> getUsers();

    List<User> getOnlyAdmins();

    String getCurrentUsername();
}