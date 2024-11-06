package kz.bitlab.MainServiceProject.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Обработка EntityNotFoundException для отсутствующего курса
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        String errorMessage = ex.getMessage();  // Используем сообщение из исключения
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    // Обработка исключения IllegalArgumentException для курса или главы
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = ex.getMessage().contains("курс") ? "Курс не найден" : "Глава не найдена";
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    // Обработка исключения, связанного с отсутствием урока (например, если урок не найден)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleLessonNotFoundException(EntityNotFoundException ex) {
        String errorMessage = "Урок не найден: " + ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    // Обработка исключения для главы
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleChapterNotFoundException(EntityNotFoundException ex) {
        String errorMessage = "Глава не найдена: " + ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    // Обработка любых других исключений, возвращаем INTERNAL_SERVER_ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        String errorMessage = "Произошла ошибка на сервере";
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
