package app.service;

import app.model.User;

import java.util.List;

public interface UserService {
    List<User> showAllUsers();

    List<User> showUsersWithUserRole();

    User showUser(Integer id);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(Integer id);

    User findUserByName(String name);

    User findUserById(Integer id);

    String getCurrentUsername();

    User findByEmail(String email);
}
