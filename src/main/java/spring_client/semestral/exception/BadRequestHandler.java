package spring_client.semestral.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class BadRequestHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle400(IllegalArgumentException e){
        log.info("400 error: {}", e.getMessage());
        return "/error";
    }

    @ExceptionHandler(NullPointerException.class)
    public String handle400(NullPointerException e){
        log.info("400 error: {}", e.getMessage());
        return "/error";
    }

    @ExceptionHandler(IllegalAccessException.class)
    public String handle400(IllegalAccessException e){
        log.info("400 error: {}", e.getMessage());
        return "/error";
    }

}
