package kz.bitlab.MainServiceProject.minio.entity;

import jakarta.persistence.*;
import kz.bitlab.MainServiceProject.main.entity.LessonEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private LessonEntity lessonEntity;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
