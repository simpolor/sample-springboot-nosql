package io.simpolor.mongo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WelcomeController {

    @RequestMapping({"/", "/index", "/welcome"})
    @ResponseBody
    public String home() {
        return "Springboot Sample JPA";
    }
}
