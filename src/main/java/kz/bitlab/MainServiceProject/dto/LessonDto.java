package kz.bitlab.MainServiceProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonDto {
    private Long id;
    private String name;
    private String description;
    private String content;
    private int order;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
