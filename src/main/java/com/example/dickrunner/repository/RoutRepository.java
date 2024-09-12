package com.example.dickrunner.repository;

import com.example.dickrunner.entity.Rout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutRepository extends JpaRepository<Rout, Long> {

    List<Rout> findByUserId(Long userId);


}
