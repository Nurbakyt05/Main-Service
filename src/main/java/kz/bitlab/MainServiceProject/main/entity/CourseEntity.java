package kz.bitlab.MainServiceProject.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString // temporary
@Entity
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity extends AbstractEntity {

    private String name;
    private String description;
}
