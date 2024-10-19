package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.entities.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface ChapterMapper {
    ChapterDto chapterToChapterDto(Chapter chapter);
    Chapter chapterDtoToChapter(ChapterDto chapterDto);

    @Mapping(target = "lessons", source = "chapter.lessons")
    ChapterDto chapterToChapterDtoWithLessons(Chapter chapter);
}
