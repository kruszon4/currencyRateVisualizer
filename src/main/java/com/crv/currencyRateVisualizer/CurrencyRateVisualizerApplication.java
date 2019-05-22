package com.crv.currencyRateVisualizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrencyRateVisualizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyRateVisualizerApplication.class, args);
	}

}
