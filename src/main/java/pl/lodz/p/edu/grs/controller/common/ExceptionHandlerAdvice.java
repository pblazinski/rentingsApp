package pl.lodz.p.edu.grs.controller.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.lodz.p.edu.grs.exceptions.GameAddRateException;
import pl.lodz.p.edu.grs.exceptions.RateAddException;
import pl.lodz.p.edu.grs.model.game.Rate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({RateAddException.class})
    public ResponseEntity handleRateAddException(final RateAddException rateAddException,
                                                 final HttpServletRequest request) {
        Rate rate = rateAddException.getRate();

        Map<String, Object> response = createResponse(
                HttpStatus.BAD_REQUEST.value(),
                String.format("User <%d> already add rate for this game.", rate.getUserId()),
                request.getRequestURI(),
                rateAddException.getClass().getName()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({GameAddRateException.class})
    public ResponseEntity handleGameAddRateException(final GameAddRateException ex,
                                                     final HttpServletRequest request) {
        Map<String, Object> response = createResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                ex.getClass().getName()
        );

        return ResponseEntity.badRequest().body(response);
    }

    private Map<String, Object> createResponse(final int status,
                                               final String message,
                                               final String path,
                                               final String exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status);
        response.put("message", message);
        response.put("path", path);
        response.put("exception", exception);
        return response;
    }

}