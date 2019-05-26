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

        model.addAttribute("m0", historicalDataService.jsDataGenerator(lists.get(0)));
        model.addAttribute("m1", historicalDataService.jsDataGenerator(lists.get(1)));
        model.addAttribute("m2", historicalDataService.jsDataGenerator(lists.get(2)));
        model.addAttribute("color", historicalDataService.getTrendLineColor());

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
    private String getChartWithTrend(@PathVariable(value = "time") String time,
                                     @PathVariable(value = "from") String from,
                                     @PathVariable(value = "to") String to, Model model) {

        try {
            List<HistoricalDataPOJO> historicalDataList = historicalDataService.getHistoricalDataList(time, from, to);
            model.addAttribute("historicalData", historicalDataService.jsDataGenerator(historicalDataList));
            model.addAttribute("error", 0);

            List<List> lists = historicalDataService.trendCalculator(historicalDataList);

            model.addAttribute("m0", historicalDataService.jsDataGenerator(lists.get(0)));
            model.addAttribute("m1", historicalDataService.jsDataGenerator(lists.get(1)));
            model.addAttribute("m2", historicalDataService.jsDataGenerator(lists.get(2)));
            model.addAttribute("color", historicalDataService.getTrendLineColor());


        } catch (NullPointerException e) {
            exceptionData(model);
        }

        return "chart";
    }

    @GetMapping("/tdata/{time}/{from}/{to}")
    private String getChartWithoutTrend(@PathVariable(value = "time") String time,
                                        @PathVariable(value = "from") String from,
                                        @PathVariable(value = "to") String to, Model model) {
        try {
            List<HistoricalDataPOJO> historicalDataList = historicalDataService.getHistoricalDataList(time, from, to);
            model.addAttribute("historicalData", historicalDataService.jsDataGenerator(historicalDataList));
            model.addAttribute("error", 0);


            model.addAttribute("m0", "[]");
            model.addAttribute("m1", "[]");
            model.addAttribute("m2", "[]");
            model.addAttribute("color", "[]");


        } catch (NullPointerException e) {
            exceptionData(model);
        }

        return "chart";
    }

    private void exceptionData(Model model) {
        System.out.println("Too many request");
        model.addAttribute("error", 1);

        List<HistoricalDataPOJO> hdd = historicalDataService.getHistoricalDefaultData();
        List<List> lists = historicalDataService.trendCalculator(hdd);

        model.addAttribute("historicalData", historicalDataService.jsDataGenerator(hdd));
        model.addAttribute("m0", historicalDataService.jsDataGenerator(lists.get(0)));
        model.addAttribute("m1", historicalDataService.jsDataGenerator(lists.get(1)));
        model.addAttribute("m2", historicalDataService.jsDataGenerator(lists.get(2)));
        model.addAttribute("color", historicalDataService.getTrendLineColor());
    }

}
