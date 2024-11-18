package kz.bitlab.MainServiceProject.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class LessonDto {
    private Long id;
    private String name;
    private String description;
    private String content;
    private int order;
    private ChapterDto chapter;
    @JsonIgnore
    private LocalDateTime createdTime;
    @JsonIgnore
    private LocalDateTime updatedTime;
}


