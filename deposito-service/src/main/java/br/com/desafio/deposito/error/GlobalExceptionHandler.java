package br.com.desafio.deposito.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
            Exception ex, WebRequest request) {
      return response(BAD_REQUEST, ex.getMessage());
    }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler({NaoAutenticadoException.class})
  public ResponseEntity<ErrorResponseDto> handleNaoAutenticadoException(
      Exception ex, WebRequest request) {
    return response(UNAUTHORIZED, ex.getMessage());
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({StaleObjectStateException.class})
  public ResponseEntity<ErrorResponseDto> handleConcorrenciaSecaoException(
      Exception ex, WebRequest request) {
    return response(BAD_REQUEST, ex.getMessage());
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    private ResponseEntity<ErrorResponseDto> response(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponseDto(message));
    }
}