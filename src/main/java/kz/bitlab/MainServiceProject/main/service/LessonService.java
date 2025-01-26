package kz.bitlab.MainServiceProject.main.service;

import kz.bitlab.MainServiceProject.main.dto.LessonDto;
import java.util.List;

public interface LessonService {

    List<LessonDto> getAllLessons();

    LessonDto getLessonById(Long id);

    LessonDto createLesson(LessonDto lessonDto);

    LessonDto updateLesson(Long id,LessonDto lessonDto);

    void deleteLesson(Long id);

    List<LessonDto> getLessonsByChapterId(Long chapterId); // Уроки по ID главы

    List<LessonDto> getLessonsByChapterIdOrdered(Long chapterId); // Уроки по ID главы, отсортированные по порядку
}
