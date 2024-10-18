package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.entities.Lesson;
import kz.bitlab.MainServiceProject.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/lesson")
public class LessonController {
    @Autowired
    private LessonRepository lessonRepository;
    @GetMapping("/all")
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }
    @PostMapping("/save")
    public Lesson saveLesson(@RequestBody Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    @PutMapping("/update")
    public Lesson updateLesson(@RequestBody Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id) {
        lessonRepository.deleteById(id);
    }

}
