package com.utp.prototype.service.impl;

import com.utp.prototype.configuration.UserAdmin;
import com.utp.prototype.entities.User;
import com.utp.prototype.repository.UserRepository;
import com.utp.prototype.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;

    }


    @Override
    public User save(User user) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (UserAdmin.isRole_admin(userDetails)){
            String pass = user.getPassword();
            if(pass.length() < 6 || !pass.matches("[A-Za-z0-9 ]+")){
                throw new RuntimeException("Invalid properties");
            }
            String encryptedPass = passwordEncoder.encode(pass);
            user.setPassword(encryptedPass);
            return repository.save(user);
        }
        return null;
    }
    @Override
    public User update(Long id, User user) {
        // Proveriti da li je pass plaintext, ako jeste hesiraj ga
        if(findById(id)==null)
            return null;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (UserAdmin.isRole_admin(userDetails)){
            String pass = user.getPassword();
            System.out.println("New password: " + pass);
            System.out.println("Old password: " + repository.findById(id).get().getPassword());

            if(!pass.equals(repository.findById(id).get().getPassword())) {
                String encryptedPass = passwordEncoder.encode(pass);
                user.setPassword(encryptedPass);
                if(pass.length() < 6 || !pass.matches("[A-Za-z0-9 ]+")){
                    throw new RuntimeException("Password not written correctly!");
                }
            }

            return repository.save(user);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (UserAdmin.isRole_admin(userDetails)){
            repository.deleteById(id);
        }
        // Vrati poruku da nema autorizaciju
    }

    @Override
    public User findById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (UserAdmin.isRole_admin(userDetails)) {
            return repository.findById(id).orElse(null);
        }
        return null;
        // Vrati poruku da nema autorizaciju
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();

        // Vrati poruku da nema autorizaciju
    }

    @Override
    public List<String> getTypes(){
        return repository.getTypes();
    }

}
