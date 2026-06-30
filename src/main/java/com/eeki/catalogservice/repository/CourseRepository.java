package com.eeki.catalogservice.repository;

import com.eeki.catalogservice.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByCategory(String category, Pageable pageable);
    List<Course> findByCategory(String category);
}
