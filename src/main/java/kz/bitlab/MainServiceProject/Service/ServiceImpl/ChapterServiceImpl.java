package kz.bitlab.MainServiceProject.Service.ServiceImpl;

import kz.bitlab.MainServiceProject.Service.ChapterService;
import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.entities.Chapter;
import kz.bitlab.MainServiceProject.mapper.ChapterMapper;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public List<ChapterDto> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAll();
        return chapterMapper.chaptersToChapterDtos(chapters);
    }

    @Override
    public ChapterDto getChapterById(Long id) {
        Chapter chapter = chapterRepository.findById(id).orElseThrow(() -> new RuntimeException("Chapter not found"));
        return chapterMapper.chapterToChapterDto(chapter);
    }

    @Override
    public ChapterDto createChapter(ChapterDto chapterDto) {
        Chapter chapter = chapterMapper.chapterDtoToChapter(chapterDto);
        return chapterMapper.chapterToChapterDto(chapterRepository.save(chapter));
    }

    @Override
    public ChapterDto updateChapter(ChapterDto chapterDto) {
        Chapter chapter = chapterMapper.chapterDtoToChapter(chapterDto);
        return chapterMapper.chapterToChapterDto(chapterRepository.save(chapter));
    }

    @Override
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
}
