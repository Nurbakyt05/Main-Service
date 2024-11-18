package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.entity.ChapterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { CourseMapper.class }) // CourseMapper для маппинга courseEntity
public interface ChapterMapper {

    @Mapping(source = "courseEntity", target = "course") // Маппинг для courseEntity
    ChapterDto entityToDto(ChapterEntity chapterEntity);

    @Mapping(source = "course", target = "courseEntity") // Маппинг для course
    ChapterEntity dtoToEntity(ChapterDto chapterDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(source = "course", target = "courseEntity") // Маппинг для courseEntity при обновлении
    void dtoToEntity(ChapterDto chapterDto, @MappingTarget ChapterEntity chapterEntity);
}
