package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.entity.LessonEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonDto toDto(LessonEntity lessonEntity);

    LessonEntity toEntity(LessonDto lessonDto);

    List<LessonDto> lessonsToLessonDtos(List<LessonEntity> lessonEntities);
    List<LessonEntity> lessonDtosToLessons(List<LessonDto> lessonDtos);
}
