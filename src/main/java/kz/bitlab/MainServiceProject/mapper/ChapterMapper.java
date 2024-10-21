package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.entities.Chapter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface ChapterMapper {
    ChapterDto chapterToChapterDto(Chapter chapter);
    Chapter chapterDtoToChapter(ChapterDto chapterDto);

    List<ChapterDto> chaptersToChapterDtos(List<Chapter> chapters);
    List<Chapter> chapterDtosToChapters(List<ChapterDto> chapterDtos);
}
