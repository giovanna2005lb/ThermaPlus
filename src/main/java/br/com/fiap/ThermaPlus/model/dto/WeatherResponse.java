package br.com.fiap.ThermaPlus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private Temperature temperature;
    private Temperature feelsLikeTemperature;
    private int relativeHumidity;
    private int uvIndex;
    private Precipitation precipitation;

    @Data
    public static class Temperature {
        private double degrees;
        private String unit;
    }

    @Data
    public static class Precipitation {
        private Probability probability;

        @Data
        public static class Probability {
            private int percent;
            private String type;
        }
    }
}