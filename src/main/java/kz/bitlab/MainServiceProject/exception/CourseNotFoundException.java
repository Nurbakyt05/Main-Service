package kz.bitlab.MainServiceProject.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super("Course not found: " + message);
    }
}
