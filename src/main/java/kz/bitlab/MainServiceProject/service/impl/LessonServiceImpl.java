package kz.bitlab.MainServiceProject.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.entity.ChapterEntity;
import kz.bitlab.MainServiceProject.entity.LessonEntity;
import kz.bitlab.MainServiceProject.exception.LessonNotFoundException;
import kz.bitlab.MainServiceProject.service.LessonService;
import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.mapper.LessonMapper;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import kz.bitlab.MainServiceProject.repositories.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("Fetching all lessons from the database");  // Логируем начало получения списка уроков

        try {
            // Получаем все уроки из базы данных
            List<LessonEntity> lessonEntities = lessonRepository.findAll();

            // Логирование на уровне DEBUG (если нужно вывести данные всех уроков для отладки)
            log.debug("Lesson data retrieved: {}", lessonEntities);

            // Преобразуем сущности в DTO и возвращаем список
            return lessonEntities.stream()
                    .map(lessonMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Логирование на уровне ERROR (если произошла ошибка при получении уроков)
            log.error("Error occurred while fetching lessons: {}", e.getMessage(), e);
            throw new LessonNotFoundException("Error fetching lessons from the database");  // Бросаем собственное исключение
        }
    }

    @Override
    public LessonDto getLessonById(Long id) {
        log.info("Fetching lesson with ID: {}", id);  // Логируем попытку получения урока по ID

        try {
            // Ищем урок по ID
            LessonEntity lessonEntity = lessonRepository.findById(id)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with ID: " + id));

            // Логирование на уровне DEBUG (если нужно вывести данные урока для отладки)
            log.debug("Found lesson data: {}", lessonEntity);

            // Преобразуем сущность в DTO и возвращаем
            return lessonMapper.entityToDto(lessonEntity);

        } catch (LessonNotFoundException e) {
            // Логирование на уровне ERROR (если урок не найден)
            log.error("Lesson with ID {} not found: {}", id, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        }
    }

    @Override
    public LessonDto createLesson(LessonDto lesson) {
        log.info("Attempting to create a new lesson");  // Логируем начало процесса создания нового урока

        // Проверка, что информация о главе не пустая
        if (Objects.isNull(lesson.getChapter()) || Objects.isNull(lesson.getChapter().getId())) {
            log.error("Chapter information is missing, cannot create lesson");  // Логируем ошибку
            throw new EntityNotFoundException("Chapter cannot be null");
        }

        try {
            // Ищем главу по ID
            ChapterEntity chapterEntity = chapterRepository.findById(lesson.getChapter().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Chapter not found with ID: " + lesson.getChapter().getId()));

            log.debug("Found chapter for lesson: {}", chapterEntity);  // Логируем данные главы для отладки

            // Преобразуем DTO урока в сущность и связываем с найденной главой
            LessonEntity lessonEntity = lessonMapper.dtoToEntity(lesson);
            lessonEntity.setChapterEntity(chapterEntity);

            // Сохраняем урок в базе данных
            LessonEntity savedLessonEntity = lessonRepository.save(lessonEntity);

            log.info("Lesson '{}' created successfully with ID: {}", savedLessonEntity.getName(), savedLessonEntity.getId());  // Логируем успешное создание

            // Преобразуем сохранённый урок в DTO и возвращаем
            return lessonMapper.entityToDto(savedLessonEntity);

        } catch (EntityNotFoundException e) {
            // Логирование на уровне ERROR (если глава не найдена)
            log.error("Chapter with ID {} not found, cannot create lesson: {}", lesson.getChapter().getId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            // Логирование на уровне ERROR (если другая ошибка при создании урока)
            log.error("Unexpected error occurred while creating lesson: {}", e.getMessage());
            throw new LessonNotFoundException("Lesson creation failed due to unexpected error");
        }
    }

    @Override
    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        log.info("Attempting to update lesson with ID: {}", id);  // Логируем начало процесса обновления

        // Проверка на совпадение ID в URL и в объекте lessonDto
        if (!id.equals(lessonDto.getId())) {
            log.error("ID in the URL and ID in the lessonDto do not match");  // Логируем ошибку, если ID не совпадают
            throw new IllegalArgumentException("ID in the URL and ID in the lessonDto must match");
        }

        try {
            // Поиск существующего урока по ID
            LessonEntity lessonEntity = lessonRepository.findById(id)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with ID: " + id));

            log.debug("Existing lesson data before update: {}", lessonEntity);  // Логируем данные существующего урока

            // Обновление связи с главой, если она задана
            if (lessonDto.getChapter() != null && lessonDto.getChapter().getId() != null) {
                ChapterEntity chapterEntity = chapterRepository.findById(lessonDto.getChapter().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Chapter not found with ID: " + lessonDto.getChapter().getId()));
                lessonEntity.setChapterEntity(chapterEntity);
                log.debug("Updated chapter information for lesson with ID: {}", id);  // Логируем обновление главы
            }

            // Преобразуем DTO в сущность и сохраняем обновлённый урок
            lessonMapper.dtoToEntity(lessonDto, lessonEntity);
            LessonEntity updatedLessonEntity = lessonRepository.save(lessonEntity);

            log.info("Lesson with ID '{}' updated successfully", updatedLessonEntity.getId());  // Логируем успешное обновление

            // Преобразуем сохранённый урок в DTO и возвращаем
            return lessonMapper.entityToDto(updatedLessonEntity);

        } catch (LessonNotFoundException e) {
            // Логирование на уровне ERROR (если урок не найден)
            log.error("Lesson with ID {} not found, cannot update: {}", id, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        } catch (EntityNotFoundException e) {
            // Логирование на уровне ERROR (если глава не найдена)
            log.error("Chapter with ID {} not found, cannot update lesson: {}", lessonDto.getChapter().getId(), e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        } catch (Exception e) {
            // Логирование на уровне ERROR для других ошибок
            log.error("Unexpected error occurred while updating lesson with ID {}: {}", id, e.getMessage());
            throw new LessonNotFoundException("Lesson update failed due to unexpected error");
        }
    }

    @Override
    public void deleteLesson(Long id) {
        log.info("Attempting to delete lesson with ID: {}", id);  // Логируем начало процесса удаления

        try {
            // Проверка существования урока по ID
            if (!lessonRepository.existsById(id)) {
                log.error("Lesson with ID {} not found, cannot delete", id);  // Логируем ошибку, если урок не найден
                throw new LessonNotFoundException("Lesson not found with ID: " + id);
            }

            // Удаление урока из базы данных
            lessonRepository.deleteById(id);

            log.info("Lesson with ID {} successfully deleted", id);  // Логируем успешное удаление

        } catch (LessonNotFoundException e) {
            // Логирование на уровне ERROR (если урок не найден)
            log.error("Lesson with ID {} not found, cannot delete: {}", id, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        } catch (Exception e) {
            // Логирование на уровне ERROR для других ошибок
            log.error("Unexpected error occurred while deleting lesson with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Lesson deletion failed due to unexpected error");
        }
    }

    @Override
    public List<LessonDto> getLessonsByChapterId(Long chapterId) {
        log.info("Fetching lessons for chapter with ID: {}", chapterId);  // Логируем начало процесса получения уроков

        if (Objects.isNull(chapterId)) {
            log.error("Chapter ID is null, cannot fetch lessons");  // Логируем ошибку, если chapterId равен null
            throw new IllegalArgumentException("Chapter ID cannot be null");
        }

        try {
            // Ищем главу по ID
            ChapterEntity chapterEntity = chapterRepository.findById(chapterId)
                    .orElseThrow(() -> new EntityNotFoundException("Chapter not found with ID: " + chapterId));

            log.debug("Found chapter for lessons: {}", chapterEntity);  // Логируем данные главы для отладки

            // Получаем уроки, связанные с данной главой
            List<LessonEntity> lessonEntities = lessonRepository.findByChapterEntity(chapterEntity);

            log.info("Found {} lessons for chapter with ID: {}", lessonEntities.size(), chapterId);  // Логируем количество найденных уроков

            // Преобразуем уроки в DTO и возвращаем
            return lessonEntities.stream()
                    .map(lessonMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            // Логирование на уровне ERROR (если глава не найдена)
            log.error("Chapter with ID {} not found, cannot fetch lessons: {}", chapterId, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        }
    }

    @Override
    public List<LessonDto> getLessonsByChapterIdOrdered(Long chapterId) {
        log.info("Fetching ordered lessons for chapter with ID: {}", chapterId);  // Логируем начало процесса получения отсортированных уроков для главы

        if (Objects.isNull(chapterId)) {
            log.error("Chapter ID is null, cannot fetch ordered lessons");  // Логируем ошибку, если chapterId равен null
            throw new IllegalArgumentException("Chapter ID cannot be null");
        }

        try {
            // Ищем главу по ID
            ChapterEntity chapterEntity = chapterRepository.findById(chapterId)
                    .orElseThrow(() -> new EntityNotFoundException("Chapter not found with ID: " + chapterId));

            log.debug("Found chapter for ordered lessons: {}", chapterEntity);  // Логируем данные главы для отладки

            // Получаем отсортированные уроки, связанные с данной главой
            List<LessonEntity> lessonEntities = lessonRepository.findByChapterEntityOrderByOrderAsc(chapterEntity);

            log.info("Found {} ordered lessons for chapter with ID: {}", lessonEntities.size(), chapterId);  // Логируем количество найденных уроков

            // Преобразуем уроки в DTO и возвращаем
            return lessonEntities.stream()
                    .map(lessonMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            // Логирование на уровне ERROR (если глава не найдена)
            log.error("Chapter with ID {} not found, cannot fetch ordered lessons: {}", chapterId, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        }
    }
}
