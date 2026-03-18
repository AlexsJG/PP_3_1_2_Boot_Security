package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> allUsers();

    User findById(Integer id);

    void save(User user);

    void update(User user);

    void deleteById(Integer id);

    void dropTable();

    boolean isAdmin(User user);

    void saveUserWithRoles(User user);

    void updateUserWithRoles(Integer id, User updatedUser);
}
