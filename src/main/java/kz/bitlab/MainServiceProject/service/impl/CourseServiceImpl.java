package kz.bitlab.MainServiceProject.service.impl;

import kz.bitlab.MainServiceProject.entity.CourseEntity;
import kz.bitlab.MainServiceProject.service.CourseService;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.exception.CourseNotFoundException;
import kz.bitlab.MainServiceProject.mapper.CourseMapper;
import kz.bitlab.MainServiceProject.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<CourseDto> getAllCourses() {
        log.info("Fetching all courses from the database");

        try {
            log.debug("Course data: {}", courseRepository.findAll());
            return courseRepository.findAll()
                    .stream()
                    .map(courseMapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching courses from the database: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public CourseDto getCourseById(Long id) {
        log.info("Fetching course with ID: {}", id);

        try {
            CourseEntity courseEntity = courseRepository.findById(id)
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + id));
            log.debug("Course data: {}", courseEntity);
            return courseMapper.entityToDto(courseEntity);
        } catch (CourseNotFoundException e) {
            log.error("Course with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        log.info("Creating new course with name: {}", courseDto.getName());
        log.debug("Course data to be saved: {}", courseDto);
        CourseEntity courseEntity = courseMapper.dtoToEntity(courseDto);
        CourseEntity savedCourseEntity = courseRepository.save(courseEntity);
        log.info("Course with name '{}' created successfully", savedCourseEntity.getName());
        return courseMapper.entityToDto(savedCourseEntity);
    }

    @Override
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        log.info("Updating course with ID: {}", id);

        try {
            CourseEntity existingCourse = courseRepository.findById(id)
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + id));
            log.debug("Existing course data: {}", existingCourse);
            courseMapper.dtoToEntity(courseDto, existingCourse);
            CourseEntity updatedCourseEntity = courseRepository.save(existingCourse);
            log.info("Course with ID '{}' updated successfully", updatedCourseEntity.getId());
            return courseMapper.entityToDto(updatedCourseEntity);
        } catch (CourseNotFoundException e) {
            log.error("Course with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteCourse(Long id) {
        log.info("Attempting to delete course with ID: {}", id);

        if (!courseRepository.existsById(id)) {
            log.error("Course with ID {} not found, cannot delete", id);
            throw new CourseNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
        log.info("Course with ID {} successfully deleted", id);
    }

    @Override
    public CourseDto getCourseByName(String name) {
        log.info("Attempting to retrieve course with name: {}", name);

        try {
            CourseEntity courseEntity = courseRepository.findByName(name)
                    .orElseThrow(() -> new CourseNotFoundException("Course with name '" + name + "' not found"));
            log.debug("Found course: {}", courseEntity);
            return courseMapper.entityToDto(courseEntity);
        } catch (CourseNotFoundException e) {
            log.error("Course with name '{}' not found: {}", name, e.getMessage());
            throw e;
        }
    }
}
