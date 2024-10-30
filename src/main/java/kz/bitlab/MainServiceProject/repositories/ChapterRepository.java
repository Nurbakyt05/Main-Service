package kz.bitlab.MainServiceProject.repositories;

import kz.bitlab.MainServiceProject.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByCourseId(Long courseId); // Найти главы по id курса
    List<Chapter> findByCourseIdOrderByOrder(Long courseId); // Найти главы и отсортировать по порядку
}
