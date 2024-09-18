package com.example.dickrunner.repository;

import com.example.dickrunner.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByRoutId(Long routId);
}
