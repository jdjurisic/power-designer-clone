package com.broker.prototype.repository;

import com.broker.prototype.entities.Method;
import com.broker.prototype.entities.SService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodRepository extends JpaRepository<Method, String> {

}
