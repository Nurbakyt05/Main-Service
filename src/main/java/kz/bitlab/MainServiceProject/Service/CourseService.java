package kz.bitlab.MainServiceProject.Service;

import kz.bitlab.MainServiceProject.dto.CourseDto;
import java.util.List;

public interface CourseService {
    List<CourseDto> getAllCourses();
    CourseDto getCourseById(Long id);
    CourseDto createCourse(CourseDto courseDto);
    CourseDto updateCourse(CourseDto courseDto);
    void deleteCourse(Long id);

    CourseDto getCourseByName(String name); // Новый метод для поиска курса по имени
}
