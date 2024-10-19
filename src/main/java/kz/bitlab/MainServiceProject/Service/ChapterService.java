package kz.bitlab.MainServiceProject.Service;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import java.util.List;
import java.util.Optional;

public interface ChapterService {
    List<ChapterDto> getAllChapters();
    Optional<ChapterDto> getChapterById(Long id);
    ChapterDto createChapter(ChapterDto chapterDto);
    ChapterDto updateChapter(ChapterDto chapterDto);
    void deleteChapter(Long id);
    boolean existsById(Long id);
}
