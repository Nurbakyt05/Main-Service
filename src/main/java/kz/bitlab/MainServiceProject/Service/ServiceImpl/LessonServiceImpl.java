package kz.bitlab.MainServiceProject.Service.ServiceImpl;

import kz.bitlab.MainServiceProject.Service.LessonService;
import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.entities.Lesson;
import kz.bitlab.MainServiceProject.mapper.LessonMapper;
import kz.bitlab.MainServiceProject.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonMapper lessonMapper;

    @Override
    public List<LessonDto> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessonMapper.lessonsToLessonDtos(lessons);
    }

    @Override
    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found"));
        return lessonMapper.toDto(lesson);
    }

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        return lessonMapper.toDto(lessonRepository.save(lesson));
    }

    @Override
    public LessonDto updateLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        return lessonMapper.toDto(lessonRepository.save(lesson));
    }

    @Override
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
