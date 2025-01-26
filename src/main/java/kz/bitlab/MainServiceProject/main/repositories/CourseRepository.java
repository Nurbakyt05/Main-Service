package kz.bitlab.MainServiceProject.main.repositories;

import kz.bitlab.MainServiceProject.main.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}
