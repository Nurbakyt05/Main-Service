package kz.bitlab.MainServiceProject.main.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    @JsonIgnore
    private LocalDateTime createdTime;
    @JsonIgnore
    private LocalDateTime updatedTime;
}
