package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.entities.Course;
import kz.bitlab.MainServiceProject.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/course")
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;
    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    @PostMapping("/save")
    public Course saveCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }
    @PutMapping("/update")
    public Course updateCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
    }
}
