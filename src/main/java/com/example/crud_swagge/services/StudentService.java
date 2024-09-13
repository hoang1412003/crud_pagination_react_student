package com.example.crud_swagge.services;

import com.example.crud_swagge.dto.StudentDTO;
import com.example.crud_swagge.dto.StudentImageDTO;
import com.example.crud_swagge.models.Student;
import com.example.crud_swagge.models.StudentImage;
import com.example.crud_swagge.models.XepLoai;
import com.example.crud_swagge.repositories.StudentImageRepository;
import com.example.crud_swagge.repositories.StudentRepository;
import com.example.crud_swagge.responses.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService implements IStudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentImageRepository studentImageRepository;

    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();

        // Chuyển đổi danh sách Student thành danh sách StudentResponse
        return students.stream()
                .map(StudentResponse::fromStudent)
                .collect(Collectors.toList());
    }

    @Override
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable).map(category -> {
            return StudentResponse.fromStudent(category);
        });
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public StudentResponse createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setTen(studentDTO.getTen());
        student.setThanhPho(studentDTO.getThanhPho());
        student.setNgaySinh(studentDTO.getNgaySinh());
        student.setXepLoai(studentDTO.getXepLoai());
        Student newStudent = studentRepository.save(student);
        return StudentResponse.fromStudent(newStudent);
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentDTO studentDTO) {
        if(studentRepository.existsById(id)) {
            Student student = studentRepository.findById(id).get();
            student.setTen(studentDTO.getTen());
            student.setThanhPho(studentDTO.getThanhPho());
            student.setNgaySinh(studentDTO.getNgaySinh());
            student.setXepLoai(studentDTO.getXepLoai());
            Student updatedStudent = studentRepository.save(student);
            return StudentResponse.fromStudent(updatedStudent);
        } else {
            return null;
        }
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findByName(String name) {
        return studentRepository.findByTenContainingIgnoreCase(name);
    }

    @Override
    public List<Student> findByThanhPho(String name) {
        return studentRepository.findByThanhPho(name);
    }

    @Override
    public List<Student> findByThanhPhoAndTen(String name) {
        return studentRepository.findByThanhPhoAndTen(name);
    }

    @Override
    public List<Student> findByNgaySinhBetween(int startYear, int endYear) {
        return  studentRepository.findByNgaySinhBetween(startYear, endYear);
    }

    @Override
    public List<Student> findByXepLoai(XepLoai xepLoai) {
        return studentRepository.findByXepLoai(xepLoai);
    }

    @Override
    public List<Student> searchStudents(XepLoai xepLoai, String ten, int startYear, int endYear) {
        return studentRepository.search(xepLoai, ten, startYear, endYear);
    }

    @Override
    public StudentImage saveStudentImage(Long studentId, StudentImageDTO studentImageDTO) {
        Student student = getStudentById(studentId);
        StudentImage studentImage = StudentImage.builder()
                .student(student)
                .imageUrl(studentImageDTO.getImageUrl())
                .build();
        int size = studentImageRepository.findByStudentId(studentId).size();
        if(size >=4) {
            throw new InvalidParameterException("Mỗi sinh viên ch tối da 4 hình");
        }
        return studentImageRepository.save(studentImage);
    }

    @Override
    public List<StudentImage> getAllStudentImages(Long studentId) {
        return studentImageRepository.findByStudentId(studentId);
    }


}
