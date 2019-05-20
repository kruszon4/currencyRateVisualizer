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
import org.springframework.stereotype.Service;
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
