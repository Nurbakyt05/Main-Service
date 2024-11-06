package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.entity.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(source = "chapterEntity",target = "chapter")
    LessonDto entityToDto(LessonEntity lessonEntity);

    LessonEntity dtoToEntity(LessonDto lessonDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void dtoToEntity(LessonDto dto,
                     @MappingTarget LessonEntity lessonEntity);

}
