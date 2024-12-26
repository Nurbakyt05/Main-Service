package kz.bitlab.MainServiceProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.exception.LessonNotFoundException;
import kz.bitlab.MainServiceProject.service.LessonService;
import kz.bitlab.MainServiceProject.dto.LessonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Operation(summary = "Получить все уроки", description = "Возвращает список всех уроков")
    @ApiResponse(responseCode = "200", description = "Список уроков успешно получен")
    @GetMapping("/all")
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        List<LessonDto> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @Operation(summary = "Получить урок по ID", description = "Возвращает информацию об уроке по заданному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Урок успешно найден"),
            @ApiResponse(responseCode = "404", description = "Урок с таким ID не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        try {
            LessonDto lesson = lessonService.getLessonById(id);
            return ResponseEntity.ok(lesson);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Создать новый урок", description = "Создает новый урок с указанным ID и сохраняет его в базе данных")
    @ApiResponse(responseCode = "201", description = "Урок успешно создан")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto) {
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @Operation(summary = "Обновить существующий урок", description = "Обновляет данные урока по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Урок успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Урок с таким ID не найден")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateLesson(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        try {
            LessonDto updatedLesson = lessonService.updateLesson(id, lessonDto);
            return ResponseEntity.ok(updatedLesson);
        } catch (LessonNotFoundException ex) {
            // Возвращаем 404 и тело с сообщением об ошибке
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new LessonNotFoundException("Урок с ID " + id + " не найден: " + ex.getMessage())
            );
        }
    }


    @Operation(summary = "Удалить урок", description = "Удаляет урок по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Урок успешно удален"),
            @ApiResponse(responseCode = "404", description = "Урок с таким ID не найден")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.noContent().build();
        } catch (LessonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Получить уроки по ID главы", description = "Возвращает список уроков, принадлежащих главе по её ID")
    @ApiResponse(responseCode = "200", description = "Список уроков главы успешно получен")
    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<LessonDto>> getLessonsByChapterId(@PathVariable Long chapterId) {
        List<LessonDto> lessons = lessonService.getLessonsByChapterId(chapterId);
        return ResponseEntity.ok(lessons);
    }

    @Operation(summary = "Получить уроки по ID главы в порядке", description = "Возвращает список уроков, принадлежащих главе, в определенном порядке")
    @ApiResponse(responseCode = "200", description = "Список уроков главы в порядке успешно получен")
    @GetMapping("/chapter/{chapterId}/ordered")
    public ResponseEntity<List<LessonDto>> getLessonsByChapterIdOrdered(@PathVariable Long chapterId) {
        List<LessonDto> orderedLessons = lessonService.getLessonsByChapterIdOrdered(chapterId);
        return ResponseEntity.ok(orderedLessons);
    }
}
