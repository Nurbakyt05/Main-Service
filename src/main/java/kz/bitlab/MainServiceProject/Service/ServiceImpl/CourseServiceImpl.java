package kz.bitlab.MainServiceProject.Service.impl;

import kz.bitlab.MainServiceProject.Service.CourseService;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.entities.Course;
import kz.bitlab.MainServiceProject.repositories.CourseRepository;
import kz.bitlab.MainServiceProject.mapper.CourseMapper; // Импортируем ваш маппер
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.coursesToCourseDtos(courses); // Используем маппер для преобразования
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(courseMapper::courseToCourseDto).orElse(null); // Используем маппер для преобразования
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = courseMapper.courseDtoToCourse(courseDto); // Преобразование DTO в сущность
        Course savedCourse = courseRepository.save(course); // Сохранение сущности
        return courseMapper.courseToCourseDto(savedCourse); // Возврат сохраненного DTO
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        if (!courseRepository.existsById(courseDto.getId())) {
            return null; // Если курс не найден, возврат null
        }
        Course course = courseMapper.courseDtoToCourse(courseDto); // Преобразование DTO в сущность
        Course updatedCourse = courseRepository.save(course); // Сохранение обновленной сущности
        return courseMapper.courseToCourseDto(updatedCourse); // Возврат обновленного DTO
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id); // Удаление курса по ID
    }
}
