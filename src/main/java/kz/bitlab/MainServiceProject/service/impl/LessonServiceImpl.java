package kz.bitlab.MainServiceProject.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.entity.ChapterEntity;
import kz.bitlab.MainServiceProject.entity.LessonEntity;
import kz.bitlab.MainServiceProject.service.LessonService;
import kz.bitlab.MainServiceProject.dto.LessonDto;
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
        List<LessonEntity> lessonEntities = lessonRepository.findAll(Sort.by(Sort.Order.asc("order"))); // Сортировка по полю order
        return lessonMapper.lessonsToLessonDtos(lessonEntities);
    }

    @Override
    public LessonDto getLessonById(Long id) {
        LessonEntity lessonEntity = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        return lessonMapper.toDto(lessonEntity);
    }

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {
        LessonEntity lessonEntity = lessonMapper.toEntity(lessonDto);
        lessonEntity.setOrder(lessonDto.getOrder()); // Устанавливаем порядок
        LessonEntity savedLessonEntity = lessonRepository.save(lessonEntity);
        return lessonMapper.toDto(savedLessonEntity);
    }

    @Override
    public LessonDto updateLesson(LessonDto lessonDto) {
        LessonEntity lessonEntity = lessonRepository.findById(lessonDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        // Обновляем поля урока
        lessonEntity.setName(lessonDto.getName());  // Используем name вместо title
        lessonEntity.setContent(lessonDto.getContent());
        lessonEntity.setOrder(lessonDto.getOrder());

        // Устанавливаем главу, если необходимо
        if (lessonDto.getChapter() != null && lessonDto.getChapter().getId() != null) {
            ChapterEntity chapterEntity = chapterRepository.findById(lessonDto.getChapter().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));
            lessonEntity.setChapterEntity(chapterEntity);
        }

        LessonEntity updatedLessonEntity = lessonRepository.save(lessonEntity);
        return lessonMapper.toDto(updatedLessonEntity);
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
//        List<LessonEntity> lessonEntities = lessonRepository.findByChapterId(chapterId, Sort.by(Sort.Order.asc("order")));
        return lessonMapper.lessonsToLessonDtos(null);
    }

    @Override
    public List<LessonDto> getLessonsByChapterIdOrdered(Long chapterId) {
//        List<LessonEntity> lessonEntities = lessonRepository.findByChapterId(chapterId, Sort.by(Sort.Order.asc("order")));
        return lessonMapper.lessonsToLessonDtos(null);
    }
}
