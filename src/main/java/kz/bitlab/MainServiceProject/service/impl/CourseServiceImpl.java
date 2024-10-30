package kz.bitlab.MainServiceProject.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.entity.CourseEntity;
import kz.bitlab.MainServiceProject.service.CourseService;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.mapper.CourseMapper;
import kz.bitlab.MainServiceProject.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(entity -> courseMapper.entityToDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(Long id) {
        CourseEntity courseEntity = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return courseMapper.entityToDto(courseEntity);
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        CourseEntity courseEntity = courseMapper.dtoToEntity(courseDto);
        return courseMapper.entityToDto(courseRepository.save(courseEntity));
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        CourseEntity courseEntity = courseRepository.findById(courseDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        courseMapper.dtoToEntity(courseDto, courseEntity);
        return courseMapper.entityToDto(courseRepository.save(courseEntity));
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    @Override
    public CourseDto getCourseByName(String name) {
        CourseEntity courseEntity = courseRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Course with name " + name + " not found"));
        return courseMapper.entityToDto(courseEntity);
    }
}
