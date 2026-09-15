package com.example.lab1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @RequestMapping(value = {
          "/",
          "/tickets",
          "/references",
          "/operations"
    })
    public String index() {
        return "forward:index.html";
    }
}
