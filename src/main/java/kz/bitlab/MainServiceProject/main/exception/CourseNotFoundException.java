package kz.bitlab.MainServiceProject.main.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super("Course not found: " + message);
    }
}
