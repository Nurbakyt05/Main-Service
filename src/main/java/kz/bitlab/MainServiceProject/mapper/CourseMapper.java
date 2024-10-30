package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ChapterMapper.class})
public interface CourseMapper {
    @Mapping(source = "chapters", target = "chapters")
    CourseDto courseToCourseDto(Course course);

    @Mapping(source = "chapters", target = "chapters")
    Course courseDtoToCourse(CourseDto courseDto);

    List<CourseDto> coursesToCourseDtos(List<Course> courses);
    List<Course> courseDtosToCourses(List<CourseDto> courseDtos);
}
