package kz.bitlab.MainServiceProject.mapper;

import kz.bitlab.MainServiceProject.dto.CourseDto;
import kz.bitlab.MainServiceProject.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ChapterMapper.class})
public interface CourseMapper {
    CourseDto courseToCourseDto(Course course);
    Course courseDtoToCourse(CourseDto courseDto);

    List<CourseDto> coursesToCourseDtos(List<Course> courses);
    List<Course> courseDtosToCourses(List<CourseDto> courseDtos);

    @Mapping(target = "chapters", source = "course.chapters")
    CourseDto courseToCourseDtoWithChapters(Course course);
}
