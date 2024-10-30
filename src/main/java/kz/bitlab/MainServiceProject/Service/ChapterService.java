package kz.bitlab.MainServiceProject.Service;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import java.util.List;

public interface ChapterService {
    List<ChapterDto> getAllChapters();
    ChapterDto getChapterById(Long id);
    ChapterDto createChapter(ChapterDto chapterDto);
    ChapterDto updateChapter(ChapterDto chapterDto);
    void deleteChapter(Long id);

    List<ChapterDto> getChaptersByCourseId(Long courseId); // Главы по ID курса
    List<ChapterDto> getChaptersByCourseIdOrdered(Long courseId); // Главы по ID курса, отсортированные по порядку
}
