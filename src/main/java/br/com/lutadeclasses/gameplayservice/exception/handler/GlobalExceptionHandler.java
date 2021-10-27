package br.com.lutadeclasses.gameplayservice.exception.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.lutadeclasses.gameplayservice.exception.notfound.RegistroNaoEncontradoException;
import br.com.lutadeclasses.gameplayservice.exception.validation.ValidacaoException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MGS_ERRO = "Erro => {}";

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<ErrorHandler> objectNotFound(RegistroNaoEncontradoException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.NOT_FOUND.value())
                               .error("REGISTRO NAO ENCONTRADO")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErrorHandler> objectNotFound(ValidacaoException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.BAD_REQUEST.value())
                               .error("ERRO DE VALIDACAO")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorHandler> jsonProcessingException(JsonProcessingException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                               .error("JSON PROCESSING EXCEPTION")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(JsonPatchException.class)
    public ResponseEntity<ErrorHandler> jsonPatchException(JsonPatchException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                               .error("JSON PATCH EXCEPTION")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorHandler> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
         .getAllErrors()
         .forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.EXPECTATION_FAILED.value())
                               .error("ERRO DE VALIDACAO")
                               .message(errors.toString())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();        
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorHandler> dataIntegrityException(DataIntegrityViolationException e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                               .error("DATA INTEGRITY VIOLATION")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorHandler> exception(Exception e, HttpServletRequest request) {
        var erro = ErrorHandler.builder()
                               .timestamp(System.currentTimeMillis())
                               .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                               .error("INTERNAL SERVER ERROR")
                               .message(e.getMessage())
                               .path(request.getRequestURI())
                               .method(request.getMethod())
                               .build();
        logger.error(MGS_ERRO, erro);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

}
