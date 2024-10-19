package kz.bitlab.MainServiceProject.repositories;

import kz.bitlab.MainServiceProject.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}
