package com.example.crud_swagge.dto;

import com.example.crud_swagge.models.XepLoai;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    @NotBlank(message = "Tên không được trống nha")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 ký tự")
    private String ten;

    @NotBlank(message = "Thành phố không được trống")
    private String thanhPho;

    @JsonFormat(pattern = "dd-MM-yyyy") // Sử dụng @JsonFormat cho JSON
    @Past(message = "Phải là 1 ngày trong quá khứ")
    private LocalDate ngaySinh;

    @NotNull(message = "Xếp loại không được trống")
    @Enumerated(EnumType.STRING)
    private XepLoai xepLoai;
}
