package kz.bitlab.MainServiceProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chapter")
@NoArgsConstructor
@AllArgsConstructor
public class ChapterEntity extends AbstractEntity {

    private String name;
    private String description;
    @Column(name = "order_index")
    private int order;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity courseEntity;

}
