package kz.bitlab.MainServiceProject.controller;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.MainServiceProject.service.ChapterService;
import kz.bitlab.MainServiceProject.dto.ChapterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chapter")
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
        try {
            ChapterDto chapter = chapterService.getChapterById(id);
            return ResponseEntity.ok(chapter);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ChapterDto> createChapter(@RequestBody ChapterDto chapterDto) {
        ChapterDto createdChapter = chapterService.createChapter(chapterDto);
        return ResponseEntity.status(HttpStatus.OK).body(createdChapter);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ChapterDto> updateChapter(@PathVariable Long id, @Validated @RequestBody ChapterDto chapterDto) {
        try {
            ChapterDto updatedChapter = chapterService.updateChapter(id,chapterDto);
            return ResponseEntity.ok(updatedChapter);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        try {
            chapterService.deleteChapter(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ChapterDto>> getChaptersByCourseId(@PathVariable Long courseId) {
        List<ChapterDto> chapters = chapterService.getChaptersByCourseId(courseId);
        return ResponseEntity.ok(chapters);
    }
}
