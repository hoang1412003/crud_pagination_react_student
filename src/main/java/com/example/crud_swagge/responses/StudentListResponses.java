package com.example.crud_swagge.responses;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentListResponses {
    private List<StudentResponse> studentsResponseList;
    private int totalPages;
}
