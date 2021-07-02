package com.utp.prototype.service;


import com.utp.prototype.entities.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User update(Long id, User user);

    void deleteById(Long id);

    User findById(Long id);

    User findByUsername(String username);

    List<User> findAll();

    List<String> getTypes();
}
