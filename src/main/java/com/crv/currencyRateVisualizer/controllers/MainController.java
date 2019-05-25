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

@Controller
public class MainController {

    @Autowired
    CurrencyListService currencyListService;

    @Autowired
    HistoricalDataService historicalDataService;


    @GetMapping("/")
    private String getCurrencyApp(Model model) {

        List<HistoricalDataPOJO> hdd = historicalDataService.getHistoricalDefaultData();
        List<List> lists = historicalDataService.trendCalculator(hdd);
        List m0 = lists.get(0);
        List m1 = lists.get(1);
        List m2 = lists.get(2);
        List m3 = lists.get(3);

        model.addAttribute("m0", historicalDataService.jsDataGenerator(m0));
        model.addAttribute("m1", historicalDataService.jsDataGenerator(m1));
        model.addAttribute("m2", historicalDataService.jsDataGenerator(m2));
        model.addAttribute("m3", historicalDataService.jsDataGenerator(m3));

        model.addAttribute("defaultExchangeRate", currencyListService.getRealTimeDefaultValue());
        model.addAttribute("defaultHistoricalData", historicalDataService.jsDataGenerator(hdd));
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

    @GetMapping("/cdata/{time}/{from}/{to}")
    private String getHhd(@PathVariable(value = "time") String time,
                          @PathVariable(value = "from") String from,
                          @PathVariable(value = "to") String to, Model model) {
        try {
            List<HistoricalDataPOJO> historicalDataList = historicalDataService.getHistoricalDataList(time, from, to);
            model.addAttribute("historicalData", historicalDataService.jsDataGenerator(historicalDataList));
            model.addAttribute("error", 0);


        } catch (NullPointerException e) {
            System.out.println("Too many request");
            model.addAttribute("error", 1);
            List<HistoricalDataPOJO> hdd = historicalDataService.getHistoricalDefaultData();
            model.addAttribute("historicalData", historicalDataService.jsDataGenerator(hdd));
        }

        return "chart";
    }


}
