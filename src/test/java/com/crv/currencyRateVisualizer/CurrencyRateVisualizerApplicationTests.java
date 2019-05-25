package com.crv.currencyRateVisualizer;

import com.crv.currencyRateVisualizer.model.historicalData.HistoricalDataPOJO;
import com.crv.currencyRateVisualizer.services.CurrencyListService;
import com.crv.currencyRateVisualizer.services.HistoricalDataService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyRateVisualizerApplicationTests {

    @Autowired
    CurrencyListService currencyListService;
    @Autowired
    HistoricalDataService historicalDataService;

    @Test
    public void contextLoads() {

    }

    @Test
    public void urlCreator_checkMonthValue_StringAreEqual() {

        String properUrl = "https://www.alphavantage.co/query?function=FX_Monthly&from_symbol=EUR&to_symbol=USD&apikey=GEGLEP3CDWNQQ4KE";
        Assert.assertEquals(properUrl,historicalDataService.urlCreator("Monthly", "EUR", "USD"));

    }

    @Test
    public void urlCreator_check15minValue_StringAreEqual() {

        String properUrl = "https://www.alphavantage.co/query?function=FX_INTRADAY&from_symbol=PLN&to_symbol=USD&interval=15min&apikey=GEGLEP3CDWNQQ4KE";
        Assert.assertEquals(properUrl, historicalDataService.urlCreator("15min", "PLN", "USD"));

    }

    @Test
    public void getHistoricalDataList_checkList_NotEmpty() {

        List<HistoricalDataPOJO> historicalDataList = historicalDataService.getHistoricalDataList("Monthly", "GBP", "EUR");
        Assert.assertFalse(historicalDataList.isEmpty());

    }

    @Test
    public void mapper_HistoricalDataPOJO_createJsData() {

           HistoricalDataPOJO buildOne = HistoricalDataPOJO.builder()
                .createDate("2019-01-15")
                .close("0,87")
                .build();

        String result = "{\"x\":\"2019-01-15\",\"y\":\"0,87\"}";
        Assert.assertEquals(result, historicalDataService.mapper(buildOne));

    }

}
