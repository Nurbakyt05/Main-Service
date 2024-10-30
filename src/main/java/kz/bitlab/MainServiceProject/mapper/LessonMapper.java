package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(source = "chapter", target = "chapter")
    LessonDto toDto(Lesson lesson);

    @Mapping(source = "chapter", target = "chapter")
    Lesson toEntity(LessonDto lessonDto);

    List<LessonDto> lessonsToLessonDtos(List<Lesson> lessons);
    List<Lesson> lessonDtosToLessons(List<LessonDto> lessonDtos);
}
