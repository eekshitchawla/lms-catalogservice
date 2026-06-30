package com.eeki.catalogservice.service;

import com.eeki.catalogservice.dto.CourseDTO;
import com.eeki.catalogservice.dto.EnrollRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CourseService {
    Page<CourseDTO> getAllCourses(Pageable pageable);
    CourseDTO getCourseById(Long id);
    Page<CourseDTO> getCoursesByCategory(String category, Pageable pageable);
    void enrollUser(Long courseId, EnrollRequest enrollRequest);
}
