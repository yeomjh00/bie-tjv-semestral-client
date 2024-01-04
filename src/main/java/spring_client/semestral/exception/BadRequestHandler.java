package spring_client.semestral.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class BadRequestHandler {

    @ExceptionHandler(value = HttpClientErrorException.NotFound.class)
    public String NotFound(Model model, HttpClientErrorException.NotFound e){
        log.info("Custom 404 error with HttpClientErrorException.NotFound : {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "/error";
    }

    @ExceptionHandler(value = HttpClientErrorException.BadRequest.class)
    public String BadRequest(Model model, HttpClientErrorException.BadRequest e){
        log.info("Custom 400 error with HttpClientErrorException.BadRequest : {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "/error";
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public String IllegalArgument(Model model, IllegalArgumentException e){
        log.info("400 error with IllegalArgument : {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "/error";
    }

    @ExceptionHandler(value = NullPointerException.class)
    public String NullPointer(Model model, NullPointerException e){
        log.error("400 error with Null Pointer Problem: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "/error";
    }

    @ExceptionHandler(value = IllegalAccessException.class)
    public String IllegalAccess(Model model, IllegalAccessException e){
        log.error("400 error with Illegal Access Problem: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "/error";
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public String pictureInfoStringParsingError(Model model, NumberFormatException e){
        log.error("Error occurred during parsing picture information string.: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "/error";
    }

}
