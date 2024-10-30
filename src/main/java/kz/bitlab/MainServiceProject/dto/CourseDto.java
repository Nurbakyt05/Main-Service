package kz.bitlab.MainServiceProject.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString // temporary
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<ChapterDto> chapters; // Связь с главами
}
