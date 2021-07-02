package com.utp.prototype.service;

import com.utp.prototype.entities.User;
import com.utp.prototype.model.MyUserDetails;
import com.utp.prototype.model.UserType;
import com.utp.prototype.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collection;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User name: " + username + " not found");

        }
        MyUserDetails myUserDetails = new MyUserDetails(user.getUsername(), user.getPassword(), getGrantedAuthorities(user));
        myUserDetails.setUser(user);
        return myUserDetails;


    }

    private Collection<GrantedAuthority> getGrantedAuthorities(User user){
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(user.getUserType().equals(UserType.ADMIN)){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));


        }
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return grantedAuthorities;
    }







}
