package com.crv.currencyRateVisualizer.controllers;

import com.crv.currencyRateVisualizer.model.realTime.RealTimeValue;
import com.crv.currencyRateVisualizer.services.CurrencyListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @Autowired
    CurrencyListService currencyListService;


    @GetMapping("/")
    private String getCurrencyApp(Model model) {

        model.addAttribute("currencyList", currencyListService.getCurrencyNameList());

        return "index";
    }

    @GetMapping("/rate/{from}/{to}")
    private String getExchangeRate(@PathVariable(value = "from") String fromCurrency,
                                   @PathVariable(value = "to") String toCurrency, Model model) {

        RealTimeValue exchangeRate = currencyListService.getExchangeRate(fromCurrency, toCurrency);
        model.addAttribute("exchangeRate", exchangeRate);

        return "realTimeRate";
    }


}
