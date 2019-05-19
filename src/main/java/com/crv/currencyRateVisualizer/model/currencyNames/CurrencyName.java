package com.crv.currencyRateVisualizer.model.currencyNames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyName {

    private String code;
    private String name;

}
