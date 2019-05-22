package com.crv.currencyRateVisualizer.services;

import com.crv.currencyRateVisualizer.model.currencyNames.CurrencyName;
import com.crv.currencyRateVisualizer.model.realTime.RealTimeValue;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Data
public class CurrencyListService {


    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();
    private List<CurrencyName> currencyNameList = new ArrayList<>();
    private RealTimeValue realTimeDefaultValue;


    @EventListener(ApplicationReadyEvent.class)
    public void getCurrencyList() {

        String currencyNameUrl = "https://openexchangerates.org/api/currencies.json";
        String forObject = restTemplate.getForObject(currencyNameUrl, String.class);
        JsonElement source = new JsonParser().parse(forObject);
        JsonObject object = source.getAsJsonObject().getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {

            this.currencyNameList.add(CurrencyName.builder()
                    .code(entry.getKey())
                    .name(entry.getValue().toString().replace("\"", ""))
                    .build());
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 * ? * *")
    @EventListener(ApplicationReadyEvent.class)
    public void setDefaultCurrencyExchangeRateData() {
        this.realTimeDefaultValue = null;
        this.realTimeDefaultValue = getExchangeRate("USD", "EUR");
        System.out.println("Data set");
    }


    public RealTimeValue getExchangeRate(String fromCurrency, String toCurrency) {

        String stringBuilder = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=" +
                fromCurrency +
                "&to_currency=" +
                toCurrency +
                "&apikey=LBYUC6QOGGLODMNM";

        String json = restTemplate.getForObject(stringBuilder, String.class);

        return gson.fromJson(json, RealTimeValue.class);
    }

}
