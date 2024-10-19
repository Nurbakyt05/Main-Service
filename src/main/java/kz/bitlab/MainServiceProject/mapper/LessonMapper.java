package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.entities.Lesson;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonDto toDto(Lesson lesson);
    Lesson toEntity(LessonDto lessonDto);

    List<LessonDto> lessonsToLessonDtos(List<Lesson> lessons);
    List<Lesson> lessonDtosToLessons(List<LessonDto> lessonDtos);
}
