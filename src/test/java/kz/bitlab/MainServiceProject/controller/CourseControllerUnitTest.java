package kz.bitlab.MainServiceProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.exception.CourseNotFoundException;
import kz.bitlab.MainServiceProject.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private ObjectMapper objectMapper;
    private CourseDto courseDto;
    private CourseDto createdCourse;
    private CourseDto updatedCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        // Инициализация тестовых данных
        courseDto = new CourseDto(1L, "New Course", "Test Description", LocalDateTime.now(), LocalDateTime.now());
        createdCourse = new CourseDto(1L, "New Course", "Test Description", LocalDateTime.now(), LocalDateTime.now());
        updatedCourse = new CourseDto(1L, "Updated Course", "Updated Description", LocalDateTime.now(), LocalDateTime.now());
    }


    @Test
    public void shouldReturnAllCourses() throws Exception {
        // Given
        CourseDto course1 = new CourseDto(1L, "Course 1","Test Description",courseDto.getCreatedTime(),courseDto.getUpdatedTime());
        CourseDto course2 = new CourseDto(2L, "Course 2","Test Description",courseDto.getCreatedTime(),courseDto.getUpdatedTime());
        List<CourseDto> courses = Arrays.asList(course1, course2);
        BDDMockito.given(courseService.getAllCourses()).willReturn(courses);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Course 1")))
                .andExpect(jsonPath("$[1].name", is("Course 2")));
    }

    @Test
    public void shouldReturnCourseById() throws Exception {
        // Given
        BDDMockito.given(courseService.getCourseById(1L)).willReturn(createdCourse);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Course")));
    }

    @Test
    public void shouldReturnNotFoundForNonExistingCourseById() throws Exception {
        // Given
        BDDMockito.given(courseService.getCourseById(1L)).willThrow(new CourseNotFoundException("Course not found"));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/course/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNewCourse() throws Exception {
        // Given
        BDDMockito.given(courseService.createCourse(BDDMockito.any(CourseDto.class))).willReturn(createdCourse);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/course/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Course")));
    }

    @Test
    public void shouldUpdateExistingCourse() throws Exception {
        // Given
        BDDMockito.given(courseService.updateCourse(BDDMockito.eq(1L), BDDMockito.any(CourseDto.class))).willReturn(updatedCourse);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/course/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Course")));
    }

    @Test
    public void shouldReturnNotFoundForNonExistingCourseOnUpdate() throws Exception {
        // Given
        BDDMockito.given(courseService.updateCourse(BDDMockito.eq(1L), BDDMockito.any(CourseDto.class)))
                .willThrow(new CourseNotFoundException("Course not found"));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/course/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteCourse() throws Exception {
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/course/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundForNonExistingCourseOnDelete() throws Exception {
        // Given
        BDDMockito.willThrow(new CourseNotFoundException("Course not found")).given(courseService).deleteCourse(1L);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/course/1"))
                .andExpect(status().isNotFound());
    }
}
