package kz.bitlab.MainServiceProject.main.mapper;

import kz.bitlab.MainServiceProject.main.dto.LessonDto;
import kz.bitlab.MainServiceProject.main.entity.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { ChapterMapper.class }) // Добавляем ChapterMapper для маппинга chapter
public interface LessonMapper {

    @Mapping(source = "chapterEntity", target = "chapter") // Маппинг для chapterEntity в chapter
    LessonDto entityToDto(LessonEntity lessonEntity);

    @Mapping(source = "chapter", target = "chapterEntity") // Маппинг для chapter в chapterEntity
    LessonEntity dtoToEntity(LessonDto lessonDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(source = "chapter", target = "chapterEntity") // Маппинг для chapter в chapterEntity при обновлении
    void dtoToEntity(LessonDto lessonDto, @MappingTarget LessonEntity lessonEntity);
}
