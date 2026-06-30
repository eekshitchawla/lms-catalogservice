package com.eeki.catalogservice.service;

import com.eeki.catalogservice.dto.CourseDTO;
import com.eeki.catalogservice.dto.CreateTaskRequest;
import com.eeki.catalogservice.dto.EnrollRequest;
import com.eeki.catalogservice.dto.ModuleDTO;
import com.eeki.catalogservice.entity.Course;
import com.eeki.catalogservice.entity.Module;
import com.eeki.catalogservice.exception.CourseNotFoundException;
import com.eeki.catalogservice.repository.CourseRepository;
import com.eeki.catalogservice.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final RestTemplate restTemplate;
    private static final String TASK_SERVICE_URL = "http://localhost:8080/api/v1/tasks";

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDTO> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
        return convertToDTO(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDTO> getCoursesByCategory(String category, Pageable pageable) {
        return courseRepository.findByCategory(category, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public void enrollUser(Long courseId, EnrollRequest enrollRequest) {
        // Fetch course details
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + courseId));

        // Create task request payload
        CreateTaskRequest taskRequest = CreateTaskRequest.builder()
                .title("Complete: " + course.getTitle())
                .description("Enrolled in " + course.getCategory())
                .userId(enrollRequest.userId())
                .courseId(courseId)
                .build();

        try {
            // Call Task Service to create a new task
            restTemplate.postForObject(TASK_SERVICE_URL, taskRequest, Object.class);
            log.info("User {} successfully enrolled in course {}", enrollRequest.userId(), courseId);
        } catch (RestClientException e) {
            log.error("Failed to create task for user enrollment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to complete enrollment. Task service unavailable.", e);
        }
    }

    private CourseDTO convertToDTO(Course course) {
        List<Module> modules = moduleRepository.findByCourseIdOrderByOrderIndex(course.getId());
        List<ModuleDTO> moduleDTOs = modules.stream()
                .map(this::convertModuleToDTO)
                .collect(Collectors.toList());

        return CourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .difficulty(course.getDifficulty())
                .estimatedHours(course.getEstimatedHours())
                .instructorName(course.getInstructorName())
                .modules(moduleDTOs)
                .build();
    }

    private ModuleDTO convertModuleToDTO(Module module) {
        return ModuleDTO.builder()
                .id(module.getId())
                .title(module.getTitle())
                .contentUrl(module.getContentUrl())
                .orderIndex(module.getOrderIndex())
                .build();
    }
}
