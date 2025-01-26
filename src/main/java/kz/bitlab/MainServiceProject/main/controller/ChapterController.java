package kz.bitlab.MainServiceProject.main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kz.bitlab.MainServiceProject.main.exception.ChapterNotFoundException;
import kz.bitlab.MainServiceProject.main.service.ChapterService;
import kz.bitlab.MainServiceProject.main.dto.ChapterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Operation(summary = "Получить все главы", description = "Возвращает список всех глав")
    @ApiResponse(responseCode = "200", description = "Список глав успешно получен")
    @GetMapping("/all")
    public ResponseEntity<List<ChapterDto>> getAllChapters() {
        List<ChapterDto> chapters = chapterService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }

    @Operation(summary = "Получить главу по ID", description = "Возвращает информацию о главе по заданному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Глава успешно найдена"),
            @ApiResponse(responseCode = "404", description = "Глава с таким ID не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getChapterById(@PathVariable Long id) {
        try {
            ChapterDto chapter = chapterService.getChapterById(id);
            return ResponseEntity.ok(chapter);
        } catch (ChapterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Создать новую главу", description = "Создает новую главу и сохраняет её в базе данных")
    @ApiResponse(responseCode = "200", description = "Глава успешно создана")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<ChapterDto> createChapter(@RequestBody ChapterDto chapterDto) {
        ChapterDto createdChapter = chapterService.createChapter(chapterDto);
        return ResponseEntity.status(HttpStatus.OK).body(createdChapter);
    }

    @Operation(summary = "Обновить существующую главу", description = "Обновляет данные главы по её ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Глава успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Глава с таким ID не найдена")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ChapterDto> updateChapter(@PathVariable Long id, @Validated @RequestBody ChapterDto chapterDto) {
        try {
            ChapterDto updatedChapter = chapterService.updateChapter(id, chapterDto);
            return ResponseEntity.ok(updatedChapter);
        } catch (ChapterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Удалить главу", description = "Удаляет главу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Глава успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Глава с таким ID не найдена")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        try {
            chapterService.deleteChapter(id);
            return ResponseEntity.noContent().build();
        } catch (ChapterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Получить главы курса", description = "Возвращает список глав, принадлежащих курсу по его ID")
    @ApiResponse(responseCode = "200", description = "Список глав курса успешно получен")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ChapterDto>> getChaptersByCourseId(@PathVariable Long courseId) {
        List<ChapterDto> chapters = chapterService.getChaptersByCourseId(courseId);
        return ResponseEntity.ok(chapters);
    }
}
