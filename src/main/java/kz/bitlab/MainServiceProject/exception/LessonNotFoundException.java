package kz.bitlab.MainServiceProject.exception;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException() {
        super("Lesson not found");
    }
    public LessonNotFoundException(String message) {
        super(message);
    }
}
