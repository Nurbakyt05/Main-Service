package kz.bitlab.MainServiceProject.repositories;

import kz.bitlab.MainServiceProject.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}
