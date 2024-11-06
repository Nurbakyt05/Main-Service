package kz.bitlab.MainServiceProject.repositories;

import kz.bitlab.MainServiceProject.entity.ChapterEntity;
import kz.bitlab.MainServiceProject.entity.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
    List<LessonEntity> findByChapterEntity(ChapterEntity chapterEntity);
    List<LessonEntity> findByChapterEntityOrderByOrderAsc(ChapterEntity chapterEntity);
}

