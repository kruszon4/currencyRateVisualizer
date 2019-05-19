package com.crv.currencyRateVisualizer.model.realTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRealTimeValue {

    @SerializedName("1. From_Currency Code")
    private String currencyCode;
    @SerializedName("2. From_Currency Name")
    private String fromCurrencyName;
    @SerializedName("3. To_Currency Code")
    private String toCurrencyCode;
    @SerializedName("4. To_Currency Name")
    private String toCurrencyName;
    @SerializedName("5. Exchange Rate")
    private String exchangeRate;
    @SerializedName("6. Last Refreshed")
    private String lastRefreshed;
    @SerializedName("7. Time Zone")
    private String timeZone;
    @SerializedName("8. Bid Price")
    private String bidPrice;
    @SerializedName("9. Ask Price")
    private String askPrice;

}
