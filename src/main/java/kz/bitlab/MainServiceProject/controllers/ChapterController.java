package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.entities.Chapter;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chapter")
public class ChapterController {
    @Autowired
    private ChapterRepository chapterRepository;

    @GetMapping("/all")
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chapter> getChapterById(@PathVariable Long id) {
        return chapterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Chapter> saveChapter(@RequestBody Chapter chapter) {
        Chapter savedChapter = chapterRepository.save(chapter);
        return ResponseEntity.ok(savedChapter);
    }

    @PutMapping("/update")
    public ResponseEntity<Chapter> updateChapter(@RequestBody Chapter chapter) {
        if (!chapterRepository.existsById(chapter.getId())) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если глава не найдена
        }
        Chapter updatedChapter = chapterRepository.save(chapter);
        return ResponseEntity.ok(updatedChapter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        if (!chapterRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Возвращаем 404, если глава не найдена
        }
        chapterRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
