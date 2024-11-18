package kz.bitlab.MainServiceProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.MainServiceProject.dto.ChapterDto;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.dto.LessonDto;
import kz.bitlab.MainServiceProject.exception.GlobalExceptionHandler;
import kz.bitlab.MainServiceProject.exception.LessonNotFoundException;
import kz.bitlab.MainServiceProject.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = LessonController.class)
@AutoConfigureMockMvc
public class LessonControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    private ChapterDto chapterDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        CourseDto courseDto = new CourseDto(1L,"Java","Java Basic",LocalDateTime.now(),LocalDateTime.now());

        // Инициализация данных для всех тестов
        chapterDto = new ChapterDto(1L, "Chapter 1", "Introduction to Java", 1, courseDto, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void shouldReturnAllLessons() throws Exception {
        // given
        LessonDto lesson1 = new LessonDto(1L, "Lesson 1", "Video Lesson", "Docker Compose", 1, chapterDto, LocalDateTime.now(), LocalDateTime.now());
        LessonDto lesson2 = new LessonDto(2L, "Lesson 2", "Text Lesson", "Kubernetes Basics", 2, chapterDto, LocalDateTime.now(), LocalDateTime.now());
        List<LessonDto> lessons = Arrays.asList(lesson1, lesson2);

        when(lessonService.getAllLessons()).thenReturn(lessons);

        // when & then
        mockMvc.perform(get("/lesson/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Lesson 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Lesson 2")));

        verify(lessonService, times(1)).getAllLessons();
    }

    @Test
    void shouldReturnLessonById() throws Exception {
        // given
        LessonDto lesson = new LessonDto(1L, "Lesson 1", "Video Lesson", "Docker Compose", 1, chapterDto, LocalDateTime.now(), LocalDateTime.now());
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        // when & then
        mockMvc.perform(get("/lesson/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Lesson 1")));

        verify(lessonService, times(1)).getLessonById(1L);
    }

    @Test
    void shouldReturnNotFoundWhenLessonDoesNotExist() throws Exception {
        // given
        when(lessonService.getLessonById(1L)).thenThrow(new LessonNotFoundException("Lesson not found"));

        // when & then
        mockMvc.perform(get("/lesson/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(lessonService, times(1)).getLessonById(1L);
    }

    @Test
    void createLesson() throws Exception {
        // given
        LessonDto createDto = new LessonDto(1L, "Java Developer", "Java Developer Course", "Java", 1, chapterDto, LocalDateTime.now(), LocalDateTime.now());
        LessonDto responseDto = new LessonDto(1L, "Java Developer", "Java Developer Course", "Java", 1, chapterDto, LocalDateTime.now(), LocalDateTime.now());

        when(lessonService.createLesson(any(LessonDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/lesson/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Java Developer")))
                .andExpect(jsonPath("$.description", is("Java Developer Course")));

        verify(lessonService, times(1)).createLesson(any(LessonDto.class));
    }

    @Test
    void shouldUpdateLesson() throws Exception {
        LessonDto updatedLesson = new LessonDto();
        updatedLesson.setId(1L);
        updatedLesson.setName("Updated Lesson");
        updatedLesson.setDescription("Updated Video Lesson");
        updatedLesson.setContent("Introduction to Spring Boot");
        updatedLesson.setOrder(1);
        updatedLesson.setCreatedTime(LocalDateTime.now());
        updatedLesson.setUpdatedTime(LocalDateTime.now());

        when(lessonService.updateLesson(eq(1L), any(LessonDto.class))).thenReturn(updatedLesson);

        mockMvc.perform(put("/lesson/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedLesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Lesson"));
    }

    @Test
    void shouldHandleLessonNotFoundException() {
        LessonNotFoundException exception = new LessonNotFoundException("Lesson not found with ID: 1");

        ResponseEntity<String> response = new GlobalExceptionHandler()
                .handleLessonNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Урок не найден: Lesson not found with ID: 1", response.getBody());
    }

    @Test
    void shouldDeleteLesson() throws Exception {
        // given
        willDoNothing().given(lessonService).deleteLesson(1L);

        // when & then
        mockMvc.perform(delete("/lesson/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(lessonService, times(1)).deleteLesson(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentLesson() throws Exception {
        // given
        willThrow(new LessonNotFoundException("Lesson not found")).given(lessonService).deleteLesson(1L);

        // when & then
        mockMvc.perform(delete("/lesson/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(lessonService, times(1)).deleteLesson(1L);
    }

    @Test
    void shouldReturnLessonsByChapterId() throws Exception {
        // given
        LessonDto lesson1 = new LessonDto(1L, "Lesson 1", "Video Lesson", "Docker Compose", 1, chapterDto, LocalDateTime.now(), LocalDateTime.now());
        LessonDto lesson2 = new LessonDto(2L, "Lesson 2", "Text Lesson", "Kubernetes Basics", 2, chapterDto, LocalDateTime.now(), LocalDateTime.now());
        List<LessonDto> lessons = Arrays.asList(lesson1, lesson2);
        when(lessonService.getLessonsByChapterId(1L)).thenReturn(lessons);

        // when & then
        mockMvc.perform(get("/lesson/chapter/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Lesson 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Lesson 2")));

        verify(lessonService, times(1)).getLessonsByChapterId(1L);
    }

    @Test
    void shouldReturnOrderedLessonsByChapterId() throws Exception {
        // given
        LessonDto lesson1 = new LessonDto(1L, "Lesson 1", "Video Lesson", "Docker Compose", 1, chapterDto, LocalDateTime.now(), LocalDateTime.now());
        LessonDto lesson2 = new LessonDto(2L, "Lesson 2", "Text Lesson", "Kubernetes Basics", 2, chapterDto, LocalDateTime.now(), LocalDateTime.now());
        List<LessonDto> orderedLessons = Arrays.asList(lesson1, lesson2);
        when(lessonService.getLessonsByChapterIdOrdered(1L)).thenReturn(orderedLessons);

        // when & then
        mockMvc.perform(get("/lesson/chapter/1/ordered")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Lesson 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Lesson 2")));

        verify(lessonService, times(1)).getLessonsByChapterIdOrdered(1L);
    }
}
