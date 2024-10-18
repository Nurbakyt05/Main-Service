package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.entities.Chapter;
import kz.bitlab.MainServiceProject.entities.Lesson;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import kz.bitlab.MainServiceProject.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/lesson")
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @GetMapping("/all")
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @PostMapping("/save")
    public Lesson saveLesson(@RequestBody Lesson lesson) {
        // Проверяем, что указаны главы, и ищем их в базе данных
        List<Chapter> chapters = lesson.getChapters().stream()
                .map(chapter -> chapterRepository.findById(chapter.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Привязываем найденные главы к уроку
        lesson.setChapters(chapters);

        // Сохраняем урок
        return lessonRepository.save(lesson);
    }

    @PutMapping("/update")
    public Lesson updateLesson(@RequestBody Lesson lesson) {
        // Аналогично для обновления
        List<Chapter> chapters = lesson.getChapters().stream()
                .map(chapter -> chapterRepository.findById(chapter.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        lesson.setChapters(chapters);
        return lessonRepository.save(lesson);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id) {
        lessonRepository.deleteById(id);
    }

    @GetMapping("/chapters/{chapterId}/lessons")
    public List<Lesson> getLessonsByChapterId(@PathVariable Long chapterId) {
        List<Lesson> lessons = lessonRepository.findByChapterId(chapterId);

        // Сортируем уроки по полю order
        lessons.sort(Comparator.comparingInt(Lesson::getOrder));

        return lessons;
    }
}
