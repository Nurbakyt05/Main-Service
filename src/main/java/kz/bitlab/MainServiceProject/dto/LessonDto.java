package kz.bitlab.MainServiceProject.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;
    private String name;
    private String description;
    private String content;
    private int order;
    private ChapterDto chapter; // Связь с одной главой
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}


