package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.entity.ChapterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { CourseMapper.class})
public interface ChapterMapper {

    @Mapping(source = "courseEntity", target = "course")
    ChapterDto entityToDto(ChapterEntity chapterEntity);

    ChapterEntity dtoToEntity(ChapterDto chapterDto);

}
