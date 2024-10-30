package kz.bitlab.MainServiceProject.controllers;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.Service.CourseService;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/all")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        try {
            CourseDto course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<CourseDto> createCourse(@Validated @RequestBody CourseDto courseDto) {
        CourseDto createdCourse = courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Validated @RequestBody CourseDto courseDto) {
        try {
            CourseDto updatedCourse = courseService.updateCourse(courseDto);
            return ResponseEntity.ok(updatedCourse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Новый метод для поиска курса по имени
    @GetMapping("/name")
    public ResponseEntity<CourseDto> getCourseByName(@RequestParam String name) {
        try {
            CourseDto course = courseService.getCourseByName(name);
            return ResponseEntity.ok(course);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

