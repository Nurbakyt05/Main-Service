package kz.bitlab.MainServiceProject.main.service;

import kz.bitlab.MainServiceProject.main.dto.CourseDto;
import java.util.List;

public interface CourseService {

    List<CourseDto> getAllCourses();

    CourseDto getCourseById(Long id);

    CourseDto createCourse(CourseDto courseDto);

    CourseDto updateCourse(Long id,CourseDto courseDto);

    void deleteCourse(Long id);
}
