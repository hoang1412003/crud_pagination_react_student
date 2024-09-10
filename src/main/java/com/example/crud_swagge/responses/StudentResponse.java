package com.example.crud_swagge.responses;

import com.example.crud_swagge.models.Student;
import com.example.crud_swagge.models.XepLoai;

import jakarta.validation.constraints.Past;
import lombok.*;


import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentResponse extends BaseResponses {
    private Long id;
    private String ten;
    private String thanhPho;
    private LocalDate ngaySinh;
    private XepLoai xepLoai;

    public static StudentResponse fromStudent(Student student) {
        StudentResponse studentResponse = StudentResponse.builder()
                .id(student.getId())
                .ten(student.getTen())
                .thanhPho(student.getThanhPho())
                .ngaySinh(student.getNgaySinh())
                .xepLoai(student.getXepLoai())
                .build();
        studentResponse.setCreatedAt(student.getCreatedAt());
        studentResponse.setUpdatedAt(student.getUpdatedAt());
        return studentResponse;
    }
}
