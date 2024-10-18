package kz.bitlab.MainServiceProject.controllers;

import kz.bitlab.MainServiceProject.entities.Chapter;
import kz.bitlab.MainServiceProject.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/save")
    public Chapter saveChapter(@RequestBody Chapter chapter) {
        return chapterRepository.save(chapter);
    }
    @PutMapping("/update")
    public Chapter updateChapter(@RequestBody Chapter chapter) {
        return chapterRepository.save(chapter);
    }
    @DeleteMapping("/{id}")
    public void deleteChapter(@PathVariable Long id) {
        chapterRepository.deleteById(id);
    }
}
