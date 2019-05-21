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

        String url = urlCreator(time, from, to);
        String forObject = restTemplate.getForObject(url, String.class);
        JsonElement root = new JsonParser().parse(forObject);

        JsonObject object = root.getAsJsonObject().get("Time Series FX (" + time + ")").getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            HistoricalDataPOJO historicalDataObject = gson.fromJson(entry.getValue(), HistoricalDataPOJO.class);
            historicalDataObject.setCreateDate(entry.getKey());

            historicalDataList.add(historicalDataObject);
        }
        return historicalDataList;
    }


    public String urlCreator(String time, String from, String to) {

        String main = "https://www.alphavantage.co/query?function=FX_";
        String fromString = "&from_symbol=";
        String toString = "&to_symbol=";
        String key = "&apikey=GEGLEP3CDWNQQ4KE";
        String intraday = "INTRADAY";
        String interval = "&interval=";

        if (time.equals("Monthly") || time.equals("Daily") || time.equals("Weekly")) {

            return main + time + fromString + from + toString + to + key;
        }

        return main + intraday + fromString + from + toString + to + interval + time + key;

    }

}