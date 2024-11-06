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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        List<LessonEntity> lessonEntities = lessonRepository.findAll();
        return lessonEntities.stream()
                .map(lessonMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LessonDto getLessonById(Long id) {
        LessonEntity lessonEntity = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        return lessonMapper.entityToDto(lessonEntity);
    }

    @Override
    public LessonDto createLesson(LessonDto lesson) {
        if (Objects.isNull(lesson.getChapter()) || Objects.isNull(lesson.getChapter().getId())) {
            throw new EntityNotFoundException("Chapter cannot be null");
        }
        ChapterEntity chapterEntity = chapterRepository.findById(lesson.getChapter().getId())
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));

        LessonEntity lessonEntity = lessonMapper.dtoToEntity(lesson);
        lessonEntity.setChapterEntity(chapterEntity);
        return lessonMapper.entityToDto(lessonRepository.save(lessonEntity));
    }

    @Override
    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        // Проверка на совпадение ID
        if (!id.equals(lessonDto.getId())) {
            throw new IllegalArgumentException("ID in the URL and ID in the lessonDto must match");
        }

        // Поиск существующего урока
        LessonEntity lessonEntity = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        // Обновление связи с главой, если она задана
        if (lessonDto.getChapter() != null && lessonDto.getChapter().getId() != null) {
            ChapterEntity chapterEntity = chapterRepository.findById(lessonDto.getChapter().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));
            lessonEntity.setChapterEntity(chapterEntity);
        }

        lessonMapper.dtoToEntity(lessonDto, lessonEntity);

        LessonEntity updatedLessonEntity = lessonRepository.save(lessonEntity);
        return lessonMapper.entityToDto(updatedLessonEntity);
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
        if (Objects.isNull(chapterId)) {
            throw new IllegalArgumentException("Chapter ID cannot be null");
        }

        ChapterEntity chapterEntity = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));

        List<LessonEntity> lessonEntities = lessonRepository.findByChapterEntity(chapterEntity);

        return lessonEntities.stream()
                .map(lessonMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> getLessonsByChapterIdOrdered(Long chapterId) {
        if (Objects.isNull(chapterId)) {
            throw new IllegalArgumentException("Chapter ID cannot be null");
        }
        ChapterEntity chapterEntity = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));

        List<LessonEntity> lessonEntities = lessonRepository.findByChapterEntityOrderByOrderAsc(chapterEntity);

        return lessonEntities.stream()
                .map(lessonMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
