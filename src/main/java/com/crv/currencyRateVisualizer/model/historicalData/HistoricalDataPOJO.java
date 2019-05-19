package com.crv.currencyRateVisualizer.model.historicalData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoricalDataPOJO {

    @SerializedName("3. low")
    private String low;
    @SerializedName("1. open")
    private String open;
    @SerializedName("2. high")
    private String high;
    @SerializedName("4. close")
    private String close;
    private String createDate;
}
