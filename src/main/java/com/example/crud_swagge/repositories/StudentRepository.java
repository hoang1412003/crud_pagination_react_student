package com.example.crud_swagge.repositories;

import com.example.crud_swagge.models.Student;
import com.example.crud_swagge.models.XepLoai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findAll(Pageable pageable);
    List<Student> findByTenContainingIgnoreCase(String ten);
    List<Student> findByXepLoai(XepLoai xepLoai);
    @Query("SELECT s FROM Student s where s.thanhPho like LOWER(CONCAT('%',:name,'%'))")
    List<Student> findByThanhPho(String name);

    @Query("SELECT s FROM Student s where s.thanhPho like LOWER(CONCAT('%',:name,'%')) OR s.ten like LOWER(CONCAT('%',:name,'%'))")
    List<Student> findByThanhPhoAndTen(String name);

    @Query("SELECT s FROM Student s where year(s.ngaySinh) BETWEEN :startYear AND :endYear")
    List<Student> findByNgaySinhBetween(int startYear, int endYear);

    @Query("SELECT s FROM Student s WHERE " +
        "(:xepLoai IS NULL OR s.xepLoai = :xepLoai) AND " +
        "(:ten IS NULL OR s.ten LIKE %:ten%) AND " +
        "(:startYear IS NULL OR year(s.ngaySinh) >= :startYear) AND " +
        "(:endYear IS NULL OR year(s.ngaySinh) <= :endYear)"
    )
    List<Student> search(
            @Param("xepLoai") XepLoai xepLoai,
            @Param("ten") String ten,
            @Param("startYear") int startYear,
            @Param("endYear") int endYear
    );
}
