package com.sivalabs.blog.webapp;

import com.sivalabs.blog.domain.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackageClasses = WebAppExceptionHandler.class)
class WebAppExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ModelAndView handle(ResourceNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("exception", e);
        modelAndView.setViewName("error/404");
        return modelAndView;
    }
}
