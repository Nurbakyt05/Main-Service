package kz.bitlab.MainServiceProject.repositories;

import kz.bitlab.MainServiceProject.entities.Lesson;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByChapterId(Long chapterId, Sort order); // Найти уроки по id главы
    List<Lesson> findByChapterIdOrderByOrder(Long chapterId); // Найти уроки и отсортировать по порядку
}

