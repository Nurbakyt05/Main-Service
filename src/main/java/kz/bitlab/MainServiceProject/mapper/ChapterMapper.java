package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.entities.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface ChapterMapper {
    @Mapping(source = "course", target = "course")
    @Mapping(source = "lessons", target = "lessons") // Преобразование уроков через LessonMapper
    ChapterDto chapterToChapterDto(Chapter chapter);

    @Mapping(source = "course", target = "course")
    @Mapping(source = "lessons", target = "lessons")
    Chapter chapterDtoToChapter(ChapterDto chapterDto);

    List<ChapterDto> chaptersToChapterDtos(List<Chapter> chapters);
    List<Chapter> chapterDtosToChapters(List<ChapterDto> chapterDtos);
}
