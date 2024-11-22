package net.ontopsolutions.controller.advice;

import net.ontopsolutions.exception.ProductNotFoundException;
import net.ontopsolutions.product.api.model.ProblemDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final HttpHeaders httpHeaders = overrideContentType();

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerException(ProductNotFoundException productNotFoundException, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ProblemDetail problemDetail= ProblemDetail.builder()
                .detail(productNotFoundException.getMessage())
                .title("Validation")
                .status(httpStatus.value())
                .type(httpStatus.name())
                .instance(request.getRequestURI())
                .properties(Map.of())
                .build();

        return new ResponseEntity<>(problemDetail, httpHeaders, httpStatus);
    }

    private static HttpHeaders overrideContentType() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, APPLICATION_PROBLEM_JSON_VALUE);
        return httpHeaders;
    }
}
