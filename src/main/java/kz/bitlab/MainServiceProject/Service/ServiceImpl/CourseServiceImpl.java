package kz.bitlab.MainServiceProject.Service.ServiceImpl;

import kz.bitlab.MainServiceProject.Service.CourseService;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.entities.Course;
import kz.bitlab.MainServiceProject.mapper.CourseMapper;
import kz.bitlab.MainServiceProject.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.coursesToCourseDtos(courses);
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        return courseMapper.courseToCourseDto(course);
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        return courseMapper.courseToCourseDto(courseRepository.save(course));
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        return courseMapper.courseToCourseDto(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
