package kz.bitlab.MainServiceProject.controllers;
import kz.bitlab.MainServiceProject.Service.CourseService;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/all")
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        CourseDto courseDto = courseService.getCourseById(id);
        return courseDto != null ? ResponseEntity.ok(courseDto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/save")
    public ResponseEntity<CourseDto> saveCourse(@RequestBody CourseDto courseDto) {
        CourseDto savedCourseDto = courseService.createCourse(courseDto);
        return ResponseEntity.ok(savedCourseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<CourseDto> updateCourse(@RequestBody CourseDto courseDto) {
        CourseDto updatedCourseDto = courseService.updateCourse(courseDto);
        return updatedCourseDto != null ? ResponseEntity.ok(updatedCourseDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
}
