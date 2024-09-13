package com.example.crud_swagge.repositories;

import com.example.crud_swagge.models.StudentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentImageRepository extends JpaRepository<StudentImage, Long> {
    List<StudentImage> findByStudentId(Long id);
}
