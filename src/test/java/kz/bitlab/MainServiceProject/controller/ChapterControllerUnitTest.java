package kz.bitlab.MainServiceProject.controller;

import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.exception.ChapterNotFoundException;
import kz.bitlab.MainServiceProject.service.ChapterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChapterControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChapterService chapterService;

    private CourseDto courseDto;
    private ChapterDto chapterDto;
    private ChapterDto createdChapter;

    @BeforeEach
    void setUp() {
        courseDto = new CourseDto(1L,"Java Course","Java Course Basic",LocalDateTime.now(),LocalDateTime.now());
        chapterDto = new ChapterDto(1L, "Chapter 1", "Description of chapter 1",1,courseDto, LocalDateTime.now(),LocalDateTime.now());
        createdChapter = new ChapterDto(1L, "Chapter 1", "Description of chapter 1",1,courseDto,LocalDateTime.now(),LocalDateTime.now());
    }

    @Test
    void shouldReturnAllChapters() throws Exception {
        when(chapterService.getAllChapters()).thenReturn(Arrays.asList(chapterDto));

        mockMvc.perform(get("/chapter/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(chapterDto.getId()))
                .andExpect(jsonPath("$[0].name").value(chapterDto.getName()))
                .andExpect(jsonPath("$[0].description").value(chapterDto.getDescription()));
    }

    @Test
    void shouldReturnChapterById() throws Exception {
        when(chapterService.getChapterById(anyLong())).thenReturn(chapterDto);

        mockMvc.perform(get("/chapter/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chapterDto.getId()))
                .andExpect(jsonPath("$.name").value(chapterDto.getName()))
                .andExpect(jsonPath("$.description").value(chapterDto.getDescription()));
    }

    @Test
    void shouldReturnNotFoundWhenChapterDoesNotExist() throws Exception {
        when(chapterService.getChapterById(anyLong())).thenThrow(new ChapterNotFoundException("Chapter not found"));

        mockMvc.perform(get("/chapter/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateChapter() throws Exception {
        when(chapterService.createChapter(any(ChapterDto.class))).thenReturn(createdChapter);

        mockMvc.perform(post("/chapter/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Chapter 1\", \"description\": \"Description of chapter 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdChapter.getId()))
                .andExpect(jsonPath("$.name").value(createdChapter.getName()))
                .andExpect(jsonPath("$.description").value(createdChapter.getDescription()));
    }

    @Test
    void shouldUpdateChapter() throws Exception {
        when(chapterService.updateChapter(anyLong(), any(ChapterDto.class))).thenReturn(createdChapter);

        mockMvc.perform(put("/chapter/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Chapter\", \"description\": \"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdChapter.getId()))
                .andExpect(jsonPath("$.name").value(createdChapter.getName()))
                .andExpect(jsonPath("$.description").value(createdChapter.getDescription()));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentChapter() throws Exception {
        when(chapterService.updateChapter(anyLong(), any(ChapterDto.class))).thenThrow(new ChapterNotFoundException("Chapter not found"));

        mockMvc.perform(put("/chapter/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Chapter\", \"description\": \"Updated description\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteChapter() throws Exception {
        mockMvc.perform(delete("/chapter/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentChapter() throws Exception {
        doThrow(new ChapterNotFoundException("Chapter not found"))
                .when(chapterService).deleteChapter(anyLong());

        mockMvc.perform(delete("/chapter/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnChaptersByCourseId() throws Exception {
        when(chapterService.getChaptersByCourseId(anyLong())).thenReturn(Arrays.asList(chapterDto));

        mockMvc.perform(get("/chapter/course/{courseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(chapterDto.getId()))
                .andExpect(jsonPath("$[0].name").value(chapterDto.getName()))
                .andExpect(jsonPath("$[0].description").value(chapterDto.getDescription()));
    }
}
