package kz.bitlab.MainServiceProject.main.service.impl;

import kz.bitlab.MainServiceProject.main.exception.ChapterNotFoundException;
import kz.bitlab.MainServiceProject.main.exception.LessonNotFoundException;
import kz.bitlab.MainServiceProject.main.entity.ChapterEntity;
import kz.bitlab.MainServiceProject.main.entity.LessonEntity;
import kz.bitlab.MainServiceProject.main.service.LessonService;
import kz.bitlab.MainServiceProject.main.dto.LessonDto;
import kz.bitlab.MainServiceProject.main.mapper.LessonMapper;
import kz.bitlab.MainServiceProject.main.repositories.ChapterRepository;
import kz.bitlab.MainServiceProject.main.repositories.LessonRepository;
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
        log.info("Success GetAllLessons");  // Логируем начало получения списка уроков

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
    public LessonDto createLesson(LessonDto lessonDto) {
        // Логируем попытку создания урока
        log.info("Attempting to create a new lesson: {}", lessonDto);

        // Проверка наличия информации о главе в DTO
        if (lessonDto.getChapter() == null || lessonDto.getChapter().getId() == null) {
            log.error("Chapter information is missing in the lesson DTO");
            throw new LessonNotFoundException("Chapter information is required to create a lesson");
        }

        // Проверяем существование главы в базе данных
        ChapterEntity chapterEntity = chapterRepository.findById(lessonDto.getChapter().getId())
                .orElseThrow(() -> {
                    log.error("Chapter with ID {} not found", lessonDto.getChapter().getId());
                    return new LessonNotFoundException("Chapter not found with ID: " + lessonDto.getChapter().getId());
                });

        log.debug("Found chapter with ID {} for lesson creation", chapterEntity.getId());

        // Преобразуем DTO урока в сущность
        LessonEntity lessonEntity = lessonMapper.dtoToEntity(lessonDto);
        lessonEntity.setChapterEntity(chapterEntity); // Связываем урок с главой

        // Сохраняем урок в базе данных
        LessonEntity savedLessonEntity = lessonRepository.save(lessonEntity);

        log.info("Lesson '{}' created successfully with ID: {}", savedLessonEntity.getName(), savedLessonEntity.getId());

        // Преобразуем сохранённую сущность обратно в DTO и возвращаем результат
        return lessonMapper.entityToDto(savedLessonEntity);
    }

    @Override
    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        log.info("Attempting to update lesson with ID: {}", id);

        // Проверка на совпадение ID
        if (!id.equals(lessonDto.getId())) {
            log.error("ID in the URL and ID in the lessonDto do not match");
            throw new LessonNotFoundException("ID in the URL and ID in the lessonDto must match");
        }

        try {
            // Поиск урока
            LessonEntity lessonEntity = lessonRepository.findById(id)
                    .orElseThrow(() -> new LessonNotFoundException("Lesson not found with ID: " + id));

            log.debug("Existing lesson data before update: {}", lessonEntity);

            // Обновление связанных данных
            if (lessonDto.getChapter() != null && lessonDto.getChapter().getId() != null) {
                ChapterEntity chapterEntity = chapterRepository.findById(lessonDto.getChapter().getId())
                        .orElseThrow(() -> new LessonNotFoundException("Chapter not found with ID: " + lessonDto.getChapter().getId()));
                lessonEntity.setChapterEntity(chapterEntity);
                log.debug("Updated chapter information for lesson with ID: {}", id);
            }

            // Маппинг DTO в сущность
            lessonMapper.dtoToEntity(lessonDto, lessonEntity);

            // Сохранение сущности
            LessonEntity updatedLessonEntity = lessonRepository.save(lessonEntity);

            log.info("Lesson with ID '{}' updated successfully", updatedLessonEntity.getId());

            // Преобразование в DTO и возврат результата
            return lessonMapper.entityToDto(updatedLessonEntity);

        } catch (LessonNotFoundException | ChapterNotFoundException e) {
            log.error("Error occurred: {}", e.getMessage());
            throw e; // Пробрасываем исключение дальше
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating lesson with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Lesson update failed due to unexpected error: " + e.getMessage(), e);
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
                    .orElseThrow(() -> new LessonNotFoundException("Chapter not found with ID: " + chapterId));

            log.debug("Found chapter for lessons: {}", chapterEntity);  // Логируем данные главы для отладки

            // Получаем уроки, связанные с данной главой
            List<LessonEntity> lessonEntities = lessonRepository.findByChapterEntity(chapterEntity);

            log.info("Found {} lessons for chapter with ID: {}", lessonEntities.size(), chapterId);  // Логируем количество найденных уроков

            // Преобразуем уроки в DTO и возвращаем
            return lessonEntities.stream()
                    .map(lessonMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (LessonNotFoundException e) {
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
                    .orElseThrow(() -> new LessonNotFoundException("Chapter not found with ID: " + chapterId));

            log.debug("Found chapter for ordered lessons: {}", chapterEntity);  // Логируем данные главы для отладки

            // Получаем отсортированные уроки, связанные с данной главой
            List<LessonEntity> lessonEntities = lessonRepository.findByChapterEntityOrderByOrderAsc(chapterEntity);

            log.info("Found {} ordered lessons for chapter with ID: {}", lessonEntities.size(), chapterId);  // Логируем количество найденных уроков

            // Преобразуем уроки в DTO и возвращаем
            return lessonEntities.stream()
                    .map(lessonMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (LessonNotFoundException e) {
            // Логирование на уровне ERROR (если глава не найдена)
            log.error("Chapter with ID {} not found, cannot fetch ordered lessons: {}", chapterId, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        }
    }
}
