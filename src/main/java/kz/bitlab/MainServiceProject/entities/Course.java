package kz.bitlab.MainServiceProject.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    private LocalDateTime createdTime = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updatedTime = LocalDateTime.now();
}
