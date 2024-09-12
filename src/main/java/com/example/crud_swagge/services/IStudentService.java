package com.example.crud_swagge.services;

import com.example.crud_swagge.dto.StudentDTO;
import com.example.crud_swagge.models.Student;
import com.example.crud_swagge.responses.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IStudentService {
    List<StudentResponse> getAllStudents();
    Page<StudentResponse> getAllStudents(Pageable pageable);
    Student getStudentById(Long id);
    StudentResponse createStudent(StudentDTO studentDTO);
    StudentResponse updateStudent(Long id, StudentDTO studentDTO);
    void deleteStudent(Long id);
    List<Student> findByName(String name);
    List<Student> findByThanhPho(String name);
    List<Student> findByThanhPhoAndTen(String name);
    List<Student> findByNgaySinhBetween(int startYear, int endYear);
}
