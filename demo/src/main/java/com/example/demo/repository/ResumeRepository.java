package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Resume;
import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findAllByOrderByScoreDesc();
    Resume findTopByOrderByScoreDesc();

    List<Resume> findByFileNameContainingIgnoreCase(String keyword);
}