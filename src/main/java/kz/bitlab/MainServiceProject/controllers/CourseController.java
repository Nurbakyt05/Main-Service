package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.entities.Course;
import kz.bitlab.MainServiceProject.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Course> saveCourse(@RequestBody Course course) {
        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.ok(savedCourse);
    }

    @PutMapping("/update")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        if (!courseRepository.existsById(course.getId())) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если курс не найден
        }
        Course updatedCourse = courseRepository.save(course);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если курс не найден
        }
        courseRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
