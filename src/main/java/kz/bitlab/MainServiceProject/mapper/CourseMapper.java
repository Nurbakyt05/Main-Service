package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.entity.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = {ChapterMapper.class})
public interface CourseMapper {
    CourseDto entityToDto(CourseEntity courseEntity);

    CourseEntity dtoToEntity(CourseDto courseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void dtoToEntity(CourseDto courseDto, @MappingTarget CourseEntity courseEntity);

}
