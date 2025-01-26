package kz.bitlab.MainServiceProject.main.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.main.dto.ChapterDto;
import kz.bitlab.MainServiceProject.main.entity.ChapterEntity;
import kz.bitlab.MainServiceProject.main.entity.CourseEntity;
import kz.bitlab.MainServiceProject.main.exception.ChapterNotFoundException;
import kz.bitlab.MainServiceProject.main.mapper.ChapterMapper;
import kz.bitlab.MainServiceProject.main.repositories.ChapterRepository;
import kz.bitlab.MainServiceProject.main.repositories.CourseRepository;
import kz.bitlab.MainServiceProject.main.service.ChapterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;


    @Override
    public List<ChapterDto> getAllChapters() {
        log.info("Fetching all chapters from the database");  // Логируем начало получения списка глав
        try {
            List<ChapterEntity> chapterEntities = chapterRepository.findAll();

            // Логирование на уровне DEBUG (если нужно вывести данные всех глав для отладки)
            log.debug("Chapter data retrieved: {}", chapterEntities);

            // Преобразуем сущности в DTO и возвращаем список
            return chapterEntities.stream()
                    .map(chapterMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Логирование на уровне ERROR (если произошла ошибка при получении глав)
            log.error("Error occurred while fetching chapters: {}", e.getMessage(), e);

            // Выбрасываем кастомное исключение ChapterNotFoundException
            throw new ChapterNotFoundException("Error occurred while fetching chapters: " + e.getMessage());
        }
    }

    @Override
    public ChapterDto getChapterById(Long id) {
        log.info("Fetching chapter with ID: {}", id);  // Логируем попытку получения главы по ID

        try {
            // Ищем главу по ID
            ChapterEntity chapterEntity = chapterRepository.findById(id)
                    .orElseThrow(() -> new ChapterNotFoundException("Chapter not found with ID: " + id));  // Используем кастомное исключение

            // Логирование на уровне DEBUG (для отладки, если нужно вывести данные главы)
            log.debug("Found chapter data: {}", chapterEntity);

            // Преобразуем сущность в DTO и возвращаем
            return chapterMapper.entityToDto(chapterEntity);

        } catch (ChapterNotFoundException e) {
            // Логирование на уровне ERROR (если глава не найдена)
            log.error("Chapter with ID {} not found: {}", id, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        }
    }

    @Override
    public ChapterDto createChapter(ChapterDto chapter) {
        log.info("Attempting to create a new chapter");  // Логируем начало процесса создания новой главы

        // Проверка, что курс не равен null
        if (Objects.isNull(chapter.getCourse()) || Objects.isNull(chapter.getCourse().getId())) {
            log.error("Course information is missing, cannot create chapter");  // Логируем ошибку
            throw new IllegalArgumentException("Course cannot be null");
        }
        try {
            // Ищем курс по ID
            CourseEntity courseEntity = courseRepository.findById(chapter.getCourse().getId())
                    .orElseThrow(() -> new ChapterNotFoundException("Course not found with ID: " + chapter.getCourse().getId()));  // Используем кастомное исключение

            log.debug("Found course for chapter: {}", courseEntity);  // Логируем данные курса для отладки

            // Преобразуем DTO главы в сущность и связываем с найденным курсом
            ChapterEntity chapterEntity = chapterMapper.dtoToEntity(chapter);
            chapterEntity.setCourseEntity(courseEntity);

            // Сохраняем главу в базе данных
            ChapterEntity savedChapterEntity = chapterRepository.save(chapterEntity);

            log.info("Chapter '{}' created successfully with ID: {}", savedChapterEntity.getName(), savedChapterEntity.getId());  // Логируем успешное создание

            // Преобразуем сохранённую главу в DTO и возвращаем
            return chapterMapper.entityToDto(savedChapterEntity);

        } catch (ChapterNotFoundException e) {
            // Логирование на уровне ERROR (если курс не найден)
            log.error("Course with ID {} not found, cannot create chapter: {}", chapter.getCourse().getId(), e.getMessage());
            throw e;  // Пробрасываем кастомное исключение
        } catch (IllegalArgumentException e) {
            // Логируем ошибку, если курс не указан
            log.error("Illegal argument exception: {}", e.getMessage());
            throw e;  // Пробрасываем ошибку дальше
        }
    }

    @Override
    public ChapterDto updateChapter(Long id, ChapterDto chapterDto) {
        log.info("Updating chapter with ID: {}", id);  // Логируем начало процесса обновления главы

        // Проверка, что информация о курсе не пустая
        if (Objects.isNull(chapterDto.getCourse()) || Objects.isNull(chapterDto.getCourse().getId())) {
            log.error("Course information is missing, cannot update chapter");  // Логируем ошибку
            throw new IllegalArgumentException("Course cannot be null");
        }

        try {
            // Ищем существующую главу по ID
            ChapterEntity existingChapterEntity = chapterRepository.findById(id)
                    .orElseThrow(() -> new ChapterNotFoundException("Chapter not found with ID: " + id));  // Используем кастомное исключение

            log.debug("Existing chapter data before update: {}", existingChapterEntity);  // Логируем данные существующей главы для отладки

            // Преобразуем DTO в сущность и обновляем существующую главу
            chapterMapper.dtoToEntity(chapterDto, existingChapterEntity);

            // Сохраняем обновлённую главу
            ChapterEntity updatedChapterEntity = chapterRepository.save(existingChapterEntity);

            log.info("Chapter with ID '{}' updated successfully", updatedChapterEntity.getId());  // Логируем успешное обновление

            // Возвращаем обновлённую главу в виде DTO
            return chapterMapper.entityToDto(updatedChapterEntity);

        } catch (ChapterNotFoundException e) {
            // Логирование на уровне ERROR (если глава не найдена)
            log.error("Chapter with ID {} not found, cannot update: {}", id, e.getMessage());
            throw e;  // Пробрасываем кастомное исключение
        } catch (IllegalArgumentException e) {
            // Логируем ошибку, если курс не указан
            log.error("Illegal argument exception: {}", e.getMessage());
            throw e;  // Пробрасываем ошибку дальше
        }
    }

    @Override
    public void deleteChapter(Long id) {
        log.info("Attempting to delete chapter with ID: {}", id);  // Логируем начало процесса удаления главы

        try {
            // Проверка существования главы по ID
            if (!chapterRepository.existsById(id)) {
                log.error("Chapter with ID {} not found, cannot delete", id);  // Логируем ошибку, если главы нет
                throw new ChapterNotFoundException("Chapter not found with ID: " + id);  // Используем кастомное исключение
            }

            // Удаление главы из базы данных
            chapterRepository.deleteById(id);

            log.info("Chapter with ID {} successfully deleted", id);  // Логируем успешное удаление главы

        } catch (ChapterNotFoundException e) {
            // Логирование на уровне ERROR, если глава не найдена
            log.error("Error deleting chapter with ID {}: {}", id, e.getMessage());
            throw e;  // Пробрасываем кастомное исключение дальше
        }
    }

    @Override
    public List<ChapterDto> getChaptersByCourseId(Long courseId) {
        log.info("Fetching chapters for course with ID: {}", courseId);  // Логируем начало процесса получения глав для курса

        // Проверка, что ID курса не равен null
        if (Objects.isNull(courseId)) {
            log.error("Course ID is null, cannot fetch chapters");  // Логируем ошибку, если ID курса не указан
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        try {
            // Ищем курс по ID
            CourseEntity courseEntity = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

            log.debug("Found course: {}", courseEntity);  // Логируем данные курса для отладки

            // Получаем главы, связанные с данным курсом
            List<ChapterEntity> chapterEntities = chapterRepository.findByCourseEntity(courseEntity);

            log.info("Found {} chapters for course with ID: {}", chapterEntities.size(), courseId);  // Логируем количество найденных глав

            // Преобразуем главы в DTO и возвращаем
            return chapterEntities.stream()
                    .map(chapterMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            // Логирование на уровне ERROR (если курс не найден)
            log.error("Course with ID {} not found, cannot fetch chapters: {}", courseId, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        } catch (Exception e) {
            // Логирование на уровне ERROR для других непредвиденных ошибок
            log.error("An error occurred while fetching chapters for course with ID {}: {}", courseId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch chapters", e);
        }
    }
}
