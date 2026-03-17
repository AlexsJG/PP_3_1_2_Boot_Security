package ru.kata.spring.boot_security.demo.services;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private  final RoleService roleServiceImpl;
    private final PasswordEncoder passwordEncoder;



    public UserServiceImpl(UserRepository userRepository, RoleService roleServiceImpl, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleServiceImpl = roleServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User c id: " + id + " не найден"));
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()
                && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void dropTable() {
        userRepository.deleteAll();
    }

    @Override
    public boolean isAdmin(User user) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        return user.getRoles().stream()
                .anyMatch(role -> role.getRole().equals("ROLE_ADMIN"));
    }
    @Override
    @Transactional
    public void saveUserWithRoles(User user, Integer[] roleIds) {
        if (user.getRoles() != null) {
            user.getRoles().clear();
        }
        if (roleIds != null) {
            for (Integer roleId : roleIds) {
                Role role = roleServiceImpl.findById(roleId);
                user.addRoleToUser(role);
                System.out.println("Added role: " + role.getRole());
            }
        }
        this.save(user);
        System.out.println("User saved successfully with " +
                (roleIds != null ? roleIds.length : 0) + " roles");
    }

    @Override
    @Transactional
    public void updateUserWithRoles(Integer id, User updatedUser, Integer[] roleIds) {
        System.out.println("Updating user with id: " + id);

        User existingUser = this.findById(id);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        existingUser.getRoles().clear();
        if (roleIds != null) {
            for (Integer roleId : roleIds) {
                Role role = roleServiceImpl.findById(roleId);
                existingUser.addRoleToUser(role);
                System.out.println("Added role: " + role.getRole());
            }
        }
        this.update(existingUser);
        System.out.println("User updated successfully with " +
                (roleIds != null ? roleIds.length : 0) + " roles");
    }
}


