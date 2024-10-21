package kz.bitlab.MainServiceProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChapterDto {
    private Long id;
    private String name;
    private String description;
    private int order;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<LessonDto> lessons; // Связь с уроками
}
