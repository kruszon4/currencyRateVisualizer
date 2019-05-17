package com.crv.currencyRateVisualizer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    private String getWordFromDb(Model model) {
        return "index";
    }

}
