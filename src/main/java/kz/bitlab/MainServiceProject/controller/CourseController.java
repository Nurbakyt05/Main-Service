package kz.bitlab.MainServiceProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kz.bitlab.MainServiceProject.exception.CourseNotFoundException;
import kz.bitlab.MainServiceProject.service.CourseService;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Получить все курсы", description = "Возвращает список всех курсов")
    @ApiResponse(responseCode = "200", description = "Список курсов успешно получен")
    @GetMapping("/all")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @Operation(summary = "Получить курс по ID", description = "Возвращает информацию о курсе по заданному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно найден"),
            @ApiResponse(responseCode = "404", description = "Курс с таким ID не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        try {
            CourseDto course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Создать новый курс", description = "Создает новый курс и сохраняет его в базе данных")
    @ApiResponse(responseCode = "201", description = "Курс успешно создан")
    @PostMapping("/save")
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        CourseDto createdCourse = courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @Operation(summary = "Обновить существующий курс", description = "Обновляет данные курса по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Курс с таким ID не найден")
    })
    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        try {
            CourseDto updatedCourse = courseService.updateCourse(id, courseDto);
            return ResponseEntity.ok(updatedCourse);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Удалить курс", description = "Удаляет курс по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Курс успешно удален"),
            @ApiResponse(responseCode = "404", description = "Курс с таким ID не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
