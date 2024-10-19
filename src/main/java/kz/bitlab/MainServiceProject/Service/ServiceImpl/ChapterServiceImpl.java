package kz.bitlab.MainServiceProject.Service.ServiceImpl;

import kz.bitlab.MainServiceProject.Service.ChapterService;
import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.entities.Chapter;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import kz.bitlab.MainServiceProject.mapper.ChapterMapper; // Импортируем ваш маппер
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public List<ChapterDto> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAll();
        return chapters.stream()
                .map(chapterMapper::chapterToChapterDto) // Преобразование сущности в DTO
                .toList(); // Используем метод toList() для преобразования в список
    }

    @Override
    public Optional<ChapterDto> getChapterById(Long id) {
        Optional<Chapter> chapter = chapterRepository.findById(id);
        return chapter.map(chapterMapper::chapterToChapterDto); // Преобразование сущности в DTO или пустой Optional
    }

    @Override
    public ChapterDto createChapter(ChapterDto chapterDto) {
        Chapter chapter = chapterMapper.chapterDtoToChapter(chapterDto); // Преобразование DTO в сущность
        Chapter savedChapter = chapterRepository.save(chapter); // Сохранение сущности
        return chapterMapper.chapterToChapterDto(savedChapter); // Возврат сохраненного DTO
    }

    @Override
    public ChapterDto updateChapter(ChapterDto chapterDto) {
        if (!chapterRepository.existsById(chapterDto.getId())) {
            return null; // Если глава не найдена, возврат null
        }
        Chapter chapter = chapterMapper.chapterDtoToChapter(chapterDto); // Преобразование DTO в сущность
        Chapter updatedChapter = chapterRepository.save(chapter); // Сохранение обновленной сущности
        return chapterMapper.chapterToChapterDto(updatedChapter); // Возврат обновленного DTO
    }

    @Override
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id); // Удаление главы по ID
    }

    @Override
    public boolean existsById(Long id) {
        return chapterRepository.existsById(id); // Проверка существования главы
    }
}
