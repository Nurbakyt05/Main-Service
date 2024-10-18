package kz.bitlab.MainServiceProject.repositories;

import kz.bitlab.MainServiceProject.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("SELECT l FROM Lesson l JOIN l.chapters c WHERE c.id = :chapterId")
    List<Lesson> findByChapterId(Long chapterId);
}
