package kz.bitlab.MainServiceProject.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.entity.ChapterEntity;
import kz.bitlab.MainServiceProject.entity.CourseEntity;
import kz.bitlab.MainServiceProject.mapper.ChapterMapper;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import kz.bitlab.MainServiceProject.repositories.CourseRepository;
import kz.bitlab.MainServiceProject.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChapterServiceImpl implements ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public List<ChapterDto> getAllChapters() {
        List<ChapterEntity> chapterEntities = chapterRepository.findAll();
        return chapterEntities.stream()
                .map(chapterMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChapterDto getChapterById(Long id) {
        ChapterEntity chapterEntity = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));
        return chapterMapper.entityToDto(chapterEntity);
    }

    @Override
    public ChapterDto createChapter(ChapterDto chapter) {
        if (Objects.isNull(chapter.getCourse()) || Objects.isNull(chapter.getCourse().getId())) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        CourseEntity courseEntity = courseRepository.findById(chapter.getCourse().getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        ChapterEntity chapterEntity = chapterMapper.dtoToEntity(chapter);
        chapterEntity.setCourseEntity(courseEntity);
        return chapterMapper.entityToDto(chapterRepository.save(chapterEntity));
    }

    @Override
    public ChapterDto updateChapter(Long id, ChapterDto chapterDto) {
        if (Objects.isNull(chapterDto.getCourse()) || Objects.isNull(chapterDto.getCourse().getId())) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        ChapterEntity existingChapterEntity = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found with ID: " + id));

        chapterMapper.dtoToEntity(chapterDto, existingChapterEntity);

        return chapterMapper.entityToDto(chapterRepository.save(existingChapterEntity));
    }

    @Override
    public void deleteChapter(Long id) {
        if (!chapterRepository.existsById(id)) {
            throw new EntityNotFoundException("Chapter not found");
        }
        chapterRepository.deleteById(id);
    }

    @Override
    public List<ChapterDto> getChaptersByCourseId(Long courseId) {
        if (Objects.isNull(courseId)) {
            throw new IllegalArgumentException("Course id cannot be null");
        }

        CourseEntity courseEntity = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        List<ChapterEntity> chapterEntities = chapterRepository.findByCourseEntity(courseEntity);

        return chapterEntities.stream()
                .map(chapter -> chapterMapper.entityToDto(chapter))
                .toList();
    }

}
