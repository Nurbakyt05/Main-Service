package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.Service.ChapterService;
import kz.bitlab.MainServiceProject.dto.ChapterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/all")
    public List<ChapterDto> getAllChapters() {
        return chapterService.getAllChapters();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getChapterById(@PathVariable Long id) {
        return chapterService.getChapterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<ChapterDto> saveChapter(@RequestBody ChapterDto chapterDto) {
        ChapterDto savedChapter = chapterService.createChapter(chapterDto);
        return ResponseEntity.ok(savedChapter);
    }

    @PutMapping("/update")
    public ResponseEntity<ChapterDto> updateChapter(@RequestBody ChapterDto chapterDto) {
        if (!chapterService.existsById(chapterDto.getId())) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если глава не найдена
        }
        ChapterDto updatedChapter = chapterService.updateChapter(chapterDto);
        return ResponseEntity.ok(updatedChapter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        if (!chapterService.existsById(id)) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если глава не найдена
        }
        chapterService.deleteChapter(id);
        return ResponseEntity.ok().build();
    }
}
