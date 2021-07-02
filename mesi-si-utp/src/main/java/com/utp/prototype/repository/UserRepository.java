package com.utp.prototype.repository;

import com.utp.prototype.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT DISTINCT user_type from user WHERE user_type LIKE 'ADMIN'  OR user_type LIKE 'USER' ", nativeQuery = true)
    List<String> getTypes();

}
