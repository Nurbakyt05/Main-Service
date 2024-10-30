package kz.bitlab.MainServiceProject.dto;

import lombok.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDto {
    private Long id;
    private String name;
    private String description;
    private int order;
    private CourseDto course; // Связь с курсом
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
