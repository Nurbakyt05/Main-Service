package kz.bitlab.MainServiceProject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDto {
    private Long id;
    private String name;
    private String description;
    private int order;
    private CourseDto course;
    @JsonIgnore
    private LocalDateTime createdTime;
    @JsonIgnore
    private LocalDateTime updatedTime;
}
