package kz.bitlab.MainServiceProject.Service.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
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
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return courseMapper.courseToCourseDto(course);
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = courseMapper.courseDtoToCourse(courseDto);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.courseToCourseDto(savedCourse);
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        Course course = courseRepository.findById(courseDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.courseToCourseDto(updatedCourse);
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
        Course course = courseRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Course with name " + name + " not found"));
        return courseMapper.courseToCourseDto(course);
    }
}
