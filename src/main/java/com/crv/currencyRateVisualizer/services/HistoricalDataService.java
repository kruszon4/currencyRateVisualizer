package com.crv.currencyRateVisualizer.services;

import com.crv.currencyRateVisualizer.model.historicalData.HistoricalDataPOJO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Data
@Service
public class HistoricalDataService {

    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();
    private List<HistoricalDataPOJO> historicalDefaultData = new ArrayList<>();


    @EventListener(ApplicationReadyEvent.class)
    public void getDefaultCurrencyHistoricalData() {

        List<HistoricalDataPOJO> historicalDataList = getHistoricalDataList("Monthly", "USD", "EUR");
        Collections.reverse(historicalDataList);
        this.historicalDefaultData.addAll(historicalDataList);
        System.out.println("Data loaded");
    }

    public List<HistoricalDataPOJO> getHistoricalDataList(String time, String from, String to) {

        List<HistoricalDataPOJO> historicalDataList = new ArrayList<>();
        String historicalDataUrl = "https://www.alphavantage.co/query?function=FX_" +
                time + "&from_symbol=" +
                from +
                "&to_symbol=" +
                to +
                "&apikey=GEGLEP3CDWNQQ4KE";

        String forObject = restTemplate.getForObject(historicalDataUrl, String.class);
        JsonElement root = new JsonParser().parse(forObject);

        JsonObject object = root.getAsJsonObject().get("Time Series FX (" + time + ")").getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            HistoricalDataPOJO historicalDataObject = gson.fromJson(entry.getValue(), HistoricalDataPOJO.class);
            historicalDataObject.setCreateDate(entry.getKey());

            historicalDataList.add(historicalDataObject);
        }
        return historicalDataList;
    }
}