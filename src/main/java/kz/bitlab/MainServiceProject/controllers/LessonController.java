package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.Service.LessonService;
import kz.bitlab.MainServiceProject.dto.LessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/lesson")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping("/all")
    public List<LessonDto> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        LessonDto lessonDto = lessonService.getLessonById(id);
        return lessonDto != null ? ResponseEntity.ok(lessonDto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/save")
    public ResponseEntity<LessonDto> saveLesson(@RequestBody LessonDto lessonDto) {
        LessonDto savedLessonDto = lessonService.createLesson(lessonDto);
        return ResponseEntity.ok(savedLessonDto);
    }

    @PutMapping("/update")
    public ResponseEntity<LessonDto> updateLesson(@RequestBody LessonDto lessonDto) {
        LessonDto updatedLessonDto = lessonService.updateLesson(lessonDto);
        return updatedLessonDto != null ? ResponseEntity.ok(updatedLessonDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chapters/{chapterId}/lessons")
    public ResponseEntity<List<LessonDto>> getLessonsByChapterId(@PathVariable Long chapterId) {
        List<LessonDto> lessons = lessonService.getLessonsByChapterId(chapterId);
        return ResponseEntity.ok(lessons);
    }
}
