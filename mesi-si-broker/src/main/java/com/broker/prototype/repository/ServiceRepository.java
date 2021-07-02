package com.broker.prototype.repository;

import com.broker.prototype.entities.SService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<SService, String> {

}
