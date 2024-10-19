package kz.bitlab.MainServiceProject.repositories;

import kz.bitlab.MainServiceProject.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findAllById(Long id);
}
