package kz.bitlab.MainServiceProject.controller;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.service.LessonService;
import kz.bitlab.MainServiceProject.dto.LessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/all")
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        List<LessonDto> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        try {
            LessonDto lesson = lessonService.getLessonById(id);
            return ResponseEntity.ok(lesson);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto) {
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDto> updateLesson(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        try {
            LessonDto updatedLesson = lessonService.updateLesson(id,lessonDto);
            return ResponseEntity.ok(updatedLesson);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<LessonDto>> getLessonsByChapterId(@PathVariable Long chapterId) {
        List<LessonDto> lessons = lessonService.getLessonsByChapterId(chapterId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/chapter/{chapterId}/ordered")
    public ResponseEntity<List<LessonDto>> getLessonsByChapterIdOrdered(@PathVariable Long chapterId) {
        List<LessonDto> orderedLessons = lessonService.getLessonsByChapterIdOrdered(chapterId);
        return ResponseEntity.ok(orderedLessons);
    }
}

