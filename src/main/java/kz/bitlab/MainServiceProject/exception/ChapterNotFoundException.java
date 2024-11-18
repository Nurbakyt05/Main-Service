package kz.bitlab.MainServiceProject.exception;

public class ChapterNotFoundException extends RuntimeException {
    public ChapterNotFoundException(String message) {
        super("Chapter not found: " + message);
    }
}
