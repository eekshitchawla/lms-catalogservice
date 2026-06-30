package com.eeki.catalogservice.controller;

import com.eeki.catalogservice.dto.CourseDTO;
import com.eeki.catalogservice.dto.EnrollRequest;
import com.eeki.catalogservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    private final CourseService courseService;

    /**
     * Get all courses with pagination
     * @param pageable Pagination parameters (default page=0, size=20)
     * @return Page of CourseDTO
     */
    @GetMapping
    public ResponseEntity<Page<CourseDTO>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseDTO> courses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    /**
     * Get a specific course by ID with all its modules
     * @param id Course ID
     * @return CourseDTO with modules
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    /**
     * Get courses filtered by category with pagination
     * @param category Course category
     * @param pageable Pagination parameters
     * @return Page of CourseDTO
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<CourseDTO>> getCoursesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseDTO> courses = courseService.getCoursesByCategory(category, pageable);
        return ResponseEntity.ok(courses);
    }

    /**
     * Enroll a user in a course
     * Creates a learning task for the user
     * @param courseId Course ID
     * @param enrollRequest Request containing userId
     * @return 204 No Content on success
     */
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<Void> enrollUser(
            @PathVariable Long courseId,
            @RequestBody EnrollRequest enrollRequest) {
        courseService.enrollUser(courseId, enrollRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
