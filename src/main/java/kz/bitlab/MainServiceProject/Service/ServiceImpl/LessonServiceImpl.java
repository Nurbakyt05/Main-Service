package kz.bitlab.MainServiceProject.Service.ServiceImpl;

import kz.bitlab.MainServiceProject.Service.LessonService;
import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.entities.Lesson;
import kz.bitlab.MainServiceProject.repositories.LessonRepository;
import kz.bitlab.MainServiceProject.mapper.LessonMapper; // Импортируем ваш маппер
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonMapper lessonMapper; // Ваш маппер, созданный с помощью MapStruct

    @Override
    public List<LessonDto> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessonMapper.lessonsToLessonDtos(lessons); // Преобразование всех уроков в DTO
    }

    @Override
    public LessonDto getLessonById(Long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.map(lessonMapper::toDto).orElse(null); // Преобразование сущности в DTO или null, если не найден
    }

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toEntity(lessonDto); // Преобразование DTO в сущность
        Lesson savedLesson = lessonRepository.save(lesson); // Сохранение сущности
        return lessonMapper.toDto(savedLesson); // Возврат сохраненного DTO
    }

    @Override
    public LessonDto updateLesson(LessonDto lessonDto) {
        if (!lessonRepository.existsById(lessonDto.getId())) {
            return null; // Если урок не найден, возврат null
        }
        Lesson lesson = lessonMapper.toEntity(lessonDto); // Преобразование DTO в сущность
        Lesson updatedLesson = lessonRepository.save(lesson); // Сохранение обновленной сущности
        return lessonMapper.toDto(updatedLesson); // Возврат обновленного DTO
    }

    @Override
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id); // Удаление урока по ID
    }

    @Override
    public List<LessonDto> getLessonsByChapterId(Long chapterId) {
        List<Lesson> lessons = lessonRepository.findByChapterId(chapterId); // Получение уроков по ID главы
        return lessonMapper.lessonsToLessonDtos(lessons); // Преобразование списка уроков в DTO
    }
}
