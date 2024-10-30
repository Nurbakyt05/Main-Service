package kz.bitlab.MainServiceProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lesson")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonEntity extends AbstractEntity {

    private String name;
    private String description;
    private String content;
    @Column(name = "order_index")
    private int order;

    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false)
    private ChapterEntity chapterEntity;

}
