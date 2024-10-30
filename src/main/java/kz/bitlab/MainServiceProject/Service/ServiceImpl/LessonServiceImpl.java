package kz.bitlab.MainServiceProject.Service.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.Service.LessonService;
import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.entities.Chapter;
import kz.bitlab.MainServiceProject.entities.Lesson;
import kz.bitlab.MainServiceProject.mapper.LessonMapper;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import kz.bitlab.MainServiceProject.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonMapper lessonMapper;

    @Override
    public List<LessonDto> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll(Sort.by(Sort.Order.asc("order"))); // Сортировка по полю order
        return lessonMapper.lessonsToLessonDtos(lessons);
    }

    @Override
    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        return lessonMapper.toDto(lesson);
    }

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        lesson.setOrder(lessonDto.getOrder()); // Устанавливаем порядок
        Lesson savedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(savedLesson);
    }

    @Override
    public LessonDto updateLesson(LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findById(lessonDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        // Обновляем поля урока
        lesson.setName(lessonDto.getName());  // Используем name вместо title
        lesson.setContent(lessonDto.getContent());
        lesson.setOrder(lessonDto.getOrder());

        // Устанавливаем главу, если необходимо
        if (lessonDto.getChapter() != null && lessonDto.getChapter().getId() != null) {
            Chapter chapter = chapterRepository.findById(lessonDto.getChapter().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));
            lesson.setChapter(chapter);
        }

        Lesson updatedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(updatedLesson);
    }

    @Override
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new EntityNotFoundException("Lesson not found");
        }
        lessonRepository.deleteById(id);
    }

    @Override
    public List<LessonDto> getLessonsByChapterId(Long chapterId) {
        List<Lesson> lessons = lessonRepository.findByChapterId(chapterId, Sort.by(Sort.Order.asc("order")));
        return lessonMapper.lessonsToLessonDtos(lessons);
    }

    @Override
    public List<LessonDto> getLessonsByChapterIdOrdered(Long chapterId) {
        List<Lesson> lessons = lessonRepository.findByChapterId(chapterId, Sort.by(Sort.Order.asc("order")));
        return lessonMapper.lessonsToLessonDtos(lessons);
    }
}
