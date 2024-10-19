package kz.bitlab.MainServiceProject.Service;

import kz.bitlab.MainServiceProject.dto.LessonDto;
import java.util.List;

public interface LessonService {
    List<LessonDto> getAllLessons();
    LessonDto getLessonById(Long id);
    LessonDto createLesson(LessonDto lessonDto);
    LessonDto updateLesson(LessonDto lessonDto);
    void deleteLesson(Long id);
    List<LessonDto> getLessonsByChapterId(Long chapterId);
}
