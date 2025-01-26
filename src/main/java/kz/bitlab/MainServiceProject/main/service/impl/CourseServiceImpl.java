package kz.bitlab.MainServiceProject.main.service.impl;

import kz.bitlab.MainServiceProject.main.entity.CourseEntity;
import kz.bitlab.MainServiceProject.main.service.CourseService;
import kz.bitlab.MainServiceProject.main.dto.CourseDto;
import kz.bitlab.MainServiceProject.main.exception.CourseNotFoundException;
import kz.bitlab.MainServiceProject.main.mapper.CourseMapper;
import kz.bitlab.MainServiceProject.main.repositories.CourseRepository;
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

        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + id));

        return courseMapper.entityToDto(courseEntity);
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        CourseEntity courseEntity = courseMapper.dtoToEntity(courseDto);
        CourseEntity savedCourseEntity = courseRepository.save(courseEntity);
        log.info("Course with name '{}' created successfully", savedCourseEntity.getName());
        return courseMapper.entityToDto(savedCourseEntity);
    }

    @Override
    public CourseDto updateCourse(Long id, CourseDto courseDto) {

        CourseEntity existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + id));

        courseMapper.dtoToEntity(courseDto, existingCourse);
        CourseEntity updatedCourseEntity = courseRepository.save(existingCourse);
        log.info("Course with ID '{}' updated successfully", updatedCourseEntity.getId());
        return courseMapper.entityToDto(updatedCourseEntity);
    }

    @Override
    public void deleteCourse(Long id) {
        // todo поменять
        if (!courseRepository.existsById(id)) {
            log.error("Course with ID {} not found, cannot delete", id);
            throw new CourseNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
        log.info("Course with ID {} successfully deleted", id);
    }
}
