package com.crv.currencyRateVisualizer.controllers;

import com.crv.currencyRateVisualizer.model.historicalData.HistoricalDataPOJO;
import com.crv.currencyRateVisualizer.model.realTime.RealTimeValue;
import com.crv.currencyRateVisualizer.services.CurrencyListService;
import com.crv.currencyRateVisualizer.services.HistoricalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    CurrencyListService currencyListService;

    @Autowired
    HistoricalDataService historicalDataService;


    @GetMapping("/")
    private String getCurrencyApp(Model model) {

        List<HistoricalDataPOJO> historicalDataList = historicalDataService.getHistoricalDataList("Weekly", "USD", "PLN");
        List<String> currencyRateDate = historicalDataList.stream().map(HistoricalDataPOJO::getCreateDate).collect(Collectors.toList());
        List<String> currencyRate = historicalDataList.stream().map(HistoricalDataPOJO::getClose).collect(Collectors.toList());
        model.addAttribute("historicalCurrencyDate", currencyRateDate);
        model.addAttribute("historicalData", currencyRate);


        model.addAttribute("currencyList", currencyListService.getCurrencyNameList());

        return "index";
    }

    @GetMapping("/rate/{from}/{to}")
    private String getExchangeRate(@PathVariable(value = "from") String fromCurrency,
                                   @PathVariable(value = "to") String toCurrency, Model model) {

        try {

            RealTimeValue exchangeRate = currencyListService.getExchangeRate(fromCurrency, toCurrency);
            model.addAttribute("exchangeRate", exchangeRate);


        } catch (Exception e) {
            System.out.println("Too many request");
        }


        return "realTimeRate";
    }

    @GetMapping("/hdata/{time}/{from}/{to}")
    private String getHistoricalData(@PathVariable(value = "time") String time,
                                     @PathVariable(value = "from") String from,
                                     @PathVariable(value = "to") String to, Model model) {
        try {
            List<HistoricalDataPOJO> historicalDataList = historicalDataService.getHistoricalDataList(time, from, to);
            List<String> currencyRateDate = historicalDataList.stream().map(HistoricalDataPOJO::getCreateDate).collect(Collectors.toList());
            List<String> currencyRate = historicalDataList.stream().map(HistoricalDataPOJO::getClose).collect(Collectors.toList());
            model.addAttribute("historicalCurrencyDate", currencyRateDate);
            model.addAttribute("historicalData", currencyRate);
            model.addAttribute("currencyList", currencyListService.getCurrencyNameList());

        } catch (NullPointerException e) {
            System.out.println("Too many request");
            model.addAttribute("error", 1);
        }

        return "chart";
    }

}
