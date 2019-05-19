package com.crv.currencyRateVisualizer.model.realTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RealTimeValue {


    @SerializedName("Realtime Currency Exchange Rate")
    private CurrencyRealTimeValue currencyRealTimeValue;
}
