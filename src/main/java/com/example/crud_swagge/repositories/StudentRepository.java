package com.example.crud_swagge.repositories;

import com.example.crud_swagge.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findAll(Pageable pageable);
    List<Student> findByTenContainingIgnoreCase(String ten);
    @Query("SELECT s FROM Student s where s.thanhPho like LOWER(CONCAT('%',:name,'%'))")
    List<Student> findByThanhPho(String name);

    @Query("SELECT s FROM Student s where s.thanhPho like LOWER(CONCAT('%',:name,'%')) OR s.ten like LOWER(CONCAT('%',:name,'%'))")
    List<Student> findByThanhPhoAndTen(String name);
}
