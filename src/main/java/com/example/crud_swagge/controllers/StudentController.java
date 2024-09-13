package com.example.crud_swagge.controllers;

import com.example.crud_swagge.dto.StudentDTO;
import com.example.crud_swagge.dto.StudentImageDTO;
import com.example.crud_swagge.exceptions.ResourceNotFoundException;
import com.example.crud_swagge.models.Student;
import com.example.crud_swagge.models.StudentImage;
import com.example.crud_swagge.models.XepLoai;
import com.example.crud_swagge.responses.ApiResponse;
import com.example.crud_swagge.responses.StudentListResponses;
import com.example.crud_swagge.responses.StudentResponse;
import com.example.crud_swagge.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.qos.logback.core.util.StringUtil;
@RestController
@CrossOrigin(origins = "http://localhost:3000") // allow CORS for this controller
@RequestMapping("/api/v1/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentDTO studentDTO, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(errors)
                    .message("Validation Failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }
        StudentResponse studentResponse = studentService.createStudent(studentDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentResponse)
                .message("Insert successfully")
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(
                page, size,
                Sort.by("createdAt").descending()
        );
        Page<StudentResponse> studentResponses = studentService.getAllStudents(pageable);
        int totalPages = studentResponses.getTotalPages();
        List<StudentResponse> responseList = studentResponses.getContent();
        StudentListResponses studentListResponses = StudentListResponses.builder()
                .studentResponseList(responseList)
                .totalPages(totalPages)
                .build();
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentListResponses)
                .message("List successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping
    public ResponseEntity<ApiResponse> getAllStudents() {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.getAllStudents())
                .status(HttpStatus.OK.value())
                .message("OK")
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") Long id,@Valid @RequestBody StudentDTO studentDTO, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(errors)
                    .message("Validation Failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }
        StudentResponse studentResponse = studentService.updateStudent(id, studentDTO);
        if(studentResponse == null) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentResponse)
                .message("Update successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id) {
        Student student = studentService.getStudentById(id);
        if(student == null) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        studentService.deleteStudent(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .data(id)
                .message("Delete successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search1")
    public ResponseEntity<ApiResponse> search1(@RequestParam String name) {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.findByName(name))
                .message("Search successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search2")
    public ResponseEntity<ApiResponse> search2(@RequestParam String name) {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.findByThanhPho(name))
                .message("Search successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search3")
    public ResponseEntity<ApiResponse> search3(@RequestParam String name) {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.findByThanhPhoAndTen(name))
                .message("Search successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search4")
    public ResponseEntity<ApiResponse> search4(@RequestParam int startYear, @RequestParam int endYear) {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.findByNgaySinhBetween(startYear, endYear))
                .message("Search successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/searchXepLoai")
    public ResponseEntity<ApiResponse> searchXepLoai(@RequestParam("xepLoai") String xepLoaiStr) {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.findByXepLoai(XepLoai.fromValue(xepLoaiStr)))
                .message("Search successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchStudents(
            @RequestParam(value = "xepLoai", required = false) String xepLoai,
            @RequestParam(value = "ten", required = false) String ten,
            @RequestParam(value = "thanhPho", required = false) String thanhPho,
            @RequestParam(value = "startYear", required = false) int startYear,
            @RequestParam(value = "endYear", required = false) int endYear) {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.searchStudents(XepLoai.fromValue(xepLoai), ten, startYear, endYear))
                .message("Search successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getAllImage/{id}")
    public ResponseEntity<ApiResponse> getAllImage(@PathVariable Long id) {
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.getAllStudentImages(id))
                .message("Get successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value="/uploads/{id}")
    public ResponseEntity<ApiResponse> uploads(@PathVariable Long id, @ModelAttribute("files") List<MultipartFile> files) throws IOException {
        List<StudentImage> studentImages = new ArrayList<>();
        int count = 0;
        for(MultipartFile file : files) {
            if(file != null) {
                if(file.getSize() == 0) {
                    count++;
                    continue;
                }
                String fileName =  storeFile(file);
                StudentImageDTO studentImageDTO = StudentImageDTO.builder()
                        .imageUrl(fileName)
                        .build();
                StudentImage studentImage = studentService.saveStudentImage(id, studentImageDTO);
                studentImages.add(studentImage);
            }
        }
        if(count == 1) {
            throw new IllegalArgumentException("Files chưa chọn");
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Insert successfully")
                .data(studentImages)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private  String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString()+"_"+fileName;
        java.nio.file.Path uploadDir = Paths.get("upload");
        if(!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
}
