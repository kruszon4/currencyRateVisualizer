package com.crv.currencyRateVisualizer.services;

import com.crv.currencyRateVisualizer.model.currencyNames.CurrencyName;
import com.crv.currencyRateVisualizer.model.realTime.RealTimeValue;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class CurrencyListService {


    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();
    private List<CurrencyName> currencyNameList = new ArrayList<>();


    @EventListener(ApplicationReadyEvent.class)
    public List getCurrencyList() {

        String currencyListUrl = "https://openexchangerates.org/api/currencies.json";
        String currencyJsonString = restTemplate.getForObject(currencyListUrl, String.class);
        String substring = currencyJsonString.substring(1, currencyJsonString.length() - 1);

        for (String str : substring.split(",")) {
            CurrencyName currencyName = new CurrencyName();
            currencyName.setCode(str.substring(4, 7));
            currencyName.setName(str.substring(11, str.length() - 1));
            currencyNameList.add(currencyName);
        }

        return this.currencyNameList;
    }

    public RealTimeValue getExchangeRate(String fromCurrency, String toCurrency) {

        String stringBuilder = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=" +
                fromCurrency +
                "&to_currency=" +
                toCurrency +
                "&apikey= Q3KX70Q8IC3XP091";
        String json = restTemplate.getForObject(stringBuilder, String.class);

        return gson.fromJson(json, RealTimeValue.class);
    }


}
