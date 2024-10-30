package kz.bitlab.MainServiceProject.Service.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
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
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));
        return chapterMapper.chapterToChapterDto(chapter);
    }

    @Override
    public ChapterDto createChapter(ChapterDto chapterDto) {
        Chapter chapter = chapterMapper.chapterDtoToChapter(chapterDto);
        Chapter savedChapter = chapterRepository.save(chapter);
        return chapterMapper.chapterToChapterDto(savedChapter);
    }

    @Override
    public ChapterDto updateChapter(ChapterDto chapterDto) {
        Chapter chapter = chapterRepository.findById(chapterDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Chapter not found"));
        chapter.setName(chapterDto.getName());
        chapter.setDescription(chapterDto.getDescription());
        chapter.setOrder(chapterDto.getOrder());
        Chapter updatedChapter = chapterRepository.save(chapter);
        return chapterMapper.chapterToChapterDto(updatedChapter);
    }

    @Override
    public void deleteChapter(Long id) {
        if (!chapterRepository.existsById(id)) {
            throw new EntityNotFoundException("Chapter not found");
        }
        chapterRepository.deleteById(id);
    }

    @Override
    public List<ChapterDto> getChaptersByCourseId(Long courseId) {
        List<Chapter> chapters = chapterRepository.findByCourseId(courseId);
        return chapterMapper.chaptersToChapterDtos(chapters);
    }

    @Override
    public List<ChapterDto> getChaptersByCourseIdOrdered(Long courseId) {
        List<Chapter> chapters = chapterRepository.findByCourseIdOrderByOrder(courseId);
        return chapterMapper.chaptersToChapterDtos(chapters);
    }
}
