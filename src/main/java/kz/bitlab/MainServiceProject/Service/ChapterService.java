package kz.bitlab.MainServiceProject.Service;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import java.util.List;

public interface ChapterService {
    List<ChapterDto> getAllChapters();
    ChapterDto getChapterById(Long id);
    ChapterDto createChapter(ChapterDto chapterDto);
    ChapterDto updateChapter(ChapterDto chapterDto);
    void deleteChapter(Long id);
}
