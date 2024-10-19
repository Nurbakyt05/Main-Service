package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.entities.Chapter;
import kz.bitlab.MainServiceProject.entities.Lesson;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import kz.bitlab.MainServiceProject.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Lesson> saveLesson(@RequestBody Lesson lesson) {
        List<Chapter> chapters = lesson.getChapters().stream()
                .map(chapter -> chapterRepository.findById(chapter.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (chapters.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Возвращаем 400, если главы не найдены
        }

        // Логика для обновления порядка других уроков в главе
        Chapter chapter = chapters.get(0); // Предполагаем, что все главы для урока из одного курса
        List<Lesson> lessonsInChapter = lessonRepository.findByChapterId(chapter.getId());

        // Увеличиваем порядковые номера у других уроков, если нужно
        lessonsInChapter.stream()
                .filter(l -> l.getOrder() >= lesson.getOrder())
                .forEach(l -> l.setOrder(l.getOrder() + 1));

        lesson.setChapters(chapters);
        return ResponseEntity.ok(lessonRepository.save(lesson));
    }

    @PutMapping("/update")
    public ResponseEntity<Lesson> updateLesson(@RequestBody Lesson lesson) {
        // Проверка, существует ли урок
        if (!lessonRepository.existsById(lesson.getId())) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если урок не найден
        }

        List<Chapter> chapters = lesson.getChapters().stream()
                .map(chapter -> chapterRepository.findById(chapter.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (chapters.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Возвращаем 400, если главы не найдены
        }

        lesson.setChapters(chapters);

        // Получаем текущий урок из базы данных
        Lesson existingLesson = lessonRepository.findById(lesson.getId()).orElseThrow();

        // Логика для обновления порядка уроков
        if (lesson.getOrder() != existingLesson.getOrder()) {
            List<Lesson> lessonsInChapter = lessonRepository.findByChapterId(existingLesson.getChapters().get(0).getId());

            // Если новый порядковый номер меньше текущего, смещаем уроки
            if (lesson.getOrder() < existingLesson.getOrder()) {
                lessonsInChapter.stream()
                        .filter(l -> l.getOrder() >= lesson.getOrder() && l.getOrder() < existingLesson.getOrder())
                        .forEach(l -> l.setOrder(l.getOrder() + 1));
            } else { // Если новый порядковый номер больше текущего, смещаем уроки в обратном направлении
                lessonsInChapter.stream()
                        .filter(l -> l.getOrder() > existingLesson.getOrder() && l.getOrder() <= lesson.getOrder())
                        .forEach(l -> l.setOrder(l.getOrder() - 1));
            }

            // Сохраняем обновленные уроки
            lessonRepository.saveAll(lessonsInChapter);
        }

        // Сохраняем обновленный урок
        return ResponseEntity.ok(lessonRepository.save(lesson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        if (!lessonRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если урок не найден
        }

        Lesson deletedLesson = lessonRepository.findById(id).orElseThrow();
        lessonRepository.deleteById(id);

        // Обновляем порядковые номера остальных уроков в главе
        List<Lesson> lessonsInChapter = lessonRepository.findByChapterId(deletedLesson.getChapters().get(0).getId());
        lessonsInChapter.stream()
                .filter(l -> l.getOrder() > deletedLesson.getOrder())
                .forEach(l -> l.setOrder(l.getOrder() - 1));

        lessonRepository.saveAll(lessonsInChapter);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chapters/{chapterId}/lessons")
    public ResponseEntity<List<Lesson>> getLessonsByChapterId(@PathVariable Long chapterId) {
        if (!chapterRepository.existsById(chapterId)) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если глава не найдена
        }

        List<Lesson> lessons = lessonRepository.findByChapterId(chapterId);
        lessons.sort(Comparator.comparingInt(Lesson::getOrder));
        return ResponseEntity.ok(lessons);
    }
}
