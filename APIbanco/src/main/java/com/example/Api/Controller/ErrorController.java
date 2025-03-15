package com.example.Api.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ErrorController {
    
    @GetMapping
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleError() {
        return "Página no encontrada. Verifica la ruta del endpoint.";
    }
}
