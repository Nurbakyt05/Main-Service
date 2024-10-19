package kz.bitlab.MainServiceProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
