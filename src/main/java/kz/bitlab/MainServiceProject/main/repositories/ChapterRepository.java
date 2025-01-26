package kz.bitlab.MainServiceProject.main.repositories;

import kz.bitlab.MainServiceProject.main.entity.ChapterEntity;
import kz.bitlab.MainServiceProject.main.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterEntity, Long> {
        List<ChapterEntity> findByCourseEntity(CourseEntity courseEntity);
}
