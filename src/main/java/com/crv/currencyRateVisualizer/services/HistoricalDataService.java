package com.crv.currencyRateVisualizer.services;

import com.crv.currencyRateVisualizer.model.historicalData.HistoricalDataPOJO;
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

import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
public class HistoricalDataService {

    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();
    private List<HistoricalDataPOJO> historicalDefaultData = new ArrayList<>();


    @Transactional
    @Scheduled(cron = "0 10 12 * * ?")
    @EventListener(ApplicationReadyEvent.class)
    public void setDefaultCurrencyHistoricalData() {
        this.historicalDefaultData.clear();
        List<HistoricalDataPOJO> historicalDataList = getHistoricalDataList("Monthly", "USD", "EUR");

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
        Collections.reverse(historicalDataList);

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

    public List<String> jsDataGenerator(List<HistoricalDataPOJO> source) {

        return source.stream().map(this::mapper).collect(Collectors.toList());

    }

    public String mapper(HistoricalDataPOJO source) {

        JsonObject object = new JsonObject();
        object.addProperty("x", source.getCreateDate());
        object.addProperty("y", source.getClose());
        return object.toString();
    }

    public List<List> trendCalculator(List<HistoricalDataPOJO> source) {

        List<List> trendList = new ArrayList<>();
        List<HistoricalDataPOJO> maxList = new ArrayList<>();
        List<HistoricalDataPOJO> minList = new ArrayList<>();

        int listSize = source.size() / 5;
        int indexOf = 0;

        List<HistoricalDataPOJO> tmpList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < listSize; j++) {
                tmpList.add(source.get(indexOf + j));
            }

            Comparator<HistoricalDataPOJO> compareByCompare = Comparator.comparing(HistoricalDataPOJO::getClose);
            tmpList.sort(compareByCompare);
            Collections.reverse(tmpList);

            maxList.add(tmpList.get(0));
            minList.add(tmpList.get(listSize - 1));

            tmpList.clear();
            indexOf += listSize;
        }


        for (int i = 0; i < maxList.size()-1; i++) {

            double max = Double.parseDouble(maxList.get(i).getClose());
            double max2 = Double.parseDouble(maxList.get(i + 1).getClose());

            double min = Double.parseDouble(minList.get(i).getClose());
            double min2 = Double.parseDouble(minList.get(i + 1).getClose());

            if (max2 < max) {
                List<HistoricalDataPOJO> tmp = new ArrayList<>();
                tmp.add(maxList.get(i));
                tmp.add(maxList.get(i + 1));

                trendList.add(tmp);
            }

            if (min2 > min) {

                List<HistoricalDataPOJO> tmp = new ArrayList<>();
                tmp.add(minList.get(i));
                tmp.add(minList.get(i + 1));

                trendList.add(tmp);
            }
        }
        return trendList;
    }

}