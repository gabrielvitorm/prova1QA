package br.com.prova1qa.api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(EntidadeNaoEncontradaException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, "RECURSO_NAO_ENCONTRADO", exception.getMessage(), Map.of());
    }

    @ExceptionHandler(ViolacaoRegraDeNegocioException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(ViolacaoRegraDeNegocioException exception) {
        Map<String, String> fieldErrors = exception.getCampo() == null
                ? Map.of()
                : Map.of(exception.getCampo(), exception.getMessage());

        return buildResponse(HttpStatus.BAD_REQUEST, exception.getCodigo(), exception.getMessage(), fieldErrors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        return buildResponse(HttpStatus.BAD_REQUEST, "DADOS_INVALIDOS", "Dados inválidos para o cadastro do aluno.",
                fieldErrors);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String code, String message,
            Map<String, String> fieldErrors) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                fieldErrors);

        return ResponseEntity.status(status).body(response);
    }
}
