package kz.bitlab.MainServiceProject.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.entity.CourseEntity;
import kz.bitlab.MainServiceProject.service.CourseService;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.mapper.CourseMapper;
import kz.bitlab.MainServiceProject.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<CourseDto> getAllCourses() {
        // Логирование на уровне INFO (Информативное сообщение)
        log.info("Fetching all courses from the database");

        try {
            // Логирование на уровне DEBUG (Технические данные)
            log.debug("Course data: {}", courseRepository.findAll());

            // Возвращаем все курсы, преобразованные в DTO
            return courseRepository.findAll()
                    .stream()
                    .map(courseMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Логирование на уровне ERROR (При возникновении ошибки)
            log.error("Error fetching courses from the database: {}", e.getMessage(), e);
            throw e;  // Пробрасываем ошибку дальше, если нужно
        }
    }

    @Override
    public CourseDto getCourseById(Long id) {
        // Логирование на уровне INFO (информируем о поиске курса по ID)
        log.info("Fetching course with ID: {}", id);

        try {
            // Пытаемся найти курс по ID
            CourseEntity courseEntity = courseRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));

            // Логирование на уровне DEBUG (если нужно вывести данные курса)
            log.debug("Course data: {}", courseEntity);

            // Преобразуем сущность в DTO и возвращаем
            return courseMapper.entityToDto(courseEntity);
        } catch (EntityNotFoundException e) {
            // Логирование на уровне ERROR (ошибка, если курс не найден)
            log.error("Course with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        // Логирование на уровне INFO (информируем о создании нового курса)
        log.info("Creating new course with name: {}", courseDto.getName());

        // Логирование на уровне DEBUG (выводим все данные курса для отладки)
        log.debug("Course data to be saved: {}", courseDto);

        // Преобразуем DTO в сущность
        CourseEntity courseEntity = courseMapper.dtoToEntity(courseDto);

        // Сохраняем курс в базе данных и возвращаем его в виде DTO
        CourseEntity savedCourseEntity = courseRepository.save(courseEntity);

        // Логирование на уровне INFO (успешно создали курс)
        log.info("Course with name '{}' created successfully", savedCourseEntity.getName());

        // Возвращаем сохранённый курс в виде DTO
        return courseMapper.entityToDto(savedCourseEntity);
    }


    @Override
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        log.info("Updating course with ID: {}", id);  // Логируем начало обновления курса

        try {
            // Ищем курс по ID
            CourseEntity existingCourse = courseRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));

            // Логирование на уровне DEBUG (выводим данные перед обновлением)
            log.debug("Existing course data: {}", existingCourse);

            // Обновляем данные сущности на основе переданного DTO
            courseMapper.dtoToEntity(courseDto, existingCourse);

            // Сохраняем обновлённый курс и преобразуем его в DTO
            CourseEntity updatedCourseEntity = courseRepository.save(existingCourse);

            // Логирование на уровне INFO (успешно обновили курс)
            log.info("Course with ID '{}' updated successfully", updatedCourseEntity.getId());

            // Возвращаем обновлённый курс в виде DTO
            return courseMapper.entityToDto(updatedCourseEntity);

        } catch (EntityNotFoundException e) {
            // Логирование на уровне ERROR (если курс не найден)
            log.error("Course with ID {} not found: {}", id, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        }
    }

    @Override
    public void deleteCourse(Long id) {
        log.info("Attempting to delete course with ID: {}", id);  // Логируем начало удаления курса

        if (!courseRepository.existsById(id)) {
            // Логирование на уровне ERROR (если курс с таким ID не найден)
            log.error("Course with ID {} not found, cannot delete", id);
            throw new EntityNotFoundException("Course not found");
        }
        // Удаляем курс
        courseRepository.deleteById(id);

        // Логирование на уровне INFO (если курс успешно удалён)
        log.info("Course with ID {} successfully deleted", id);
    }

    @Override
    public CourseDto getCourseByName(String name) {
        log.info("Attempting to retrieve course with name: {}", name);  // Логируем попытку получить курс по имени

        try {
            // Ищем курс по имени
            CourseEntity courseEntity = courseRepository.findByName(name)
                    .orElseThrow(() -> new EntityNotFoundException("Course with name " + name + " not found"));

            // Логирование на уровне DEBUG (если нужно вывести данные курса)
            log.debug("Found course: {}", courseEntity);

            // Возвращаем курс в виде DTO
            return courseMapper.entityToDto(courseEntity);

        } catch (EntityNotFoundException e) {
            // Логирование на уровне ERROR (если курс не найден)
            log.error("Course with name '{}' not found: {}", name, e.getMessage());
            throw e;  // Пробрасываем исключение дальше
        }
    }
}
