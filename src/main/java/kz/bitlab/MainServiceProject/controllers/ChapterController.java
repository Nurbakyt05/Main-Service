package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.Service.ChapterService;
import kz.bitlab.MainServiceProject.dto.ChapterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chapters")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;

    @GetMapping("/all")
    public ResponseEntity<List<ChapterDto>> getAllChapters() {
        List<ChapterDto> chapters = chapterService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getChapterById(@PathVariable Long id) {
        ChapterDto chapter = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapter);
    }

    @PostMapping
    public ResponseEntity<ChapterDto> createChapter(@RequestBody ChapterDto chapterDto) {
        ChapterDto createdChapter = chapterService.createChapter(chapterDto);
        return ResponseEntity.status(201).body(createdChapter);
    }

    @PutMapping
    public ResponseEntity<ChapterDto> updateChapter(@RequestBody ChapterDto chapterDto) {
        ChapterDto updatedChapter = chapterService.updateChapter(chapterDto);
        return ResponseEntity.ok(updatedChapter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }
}
