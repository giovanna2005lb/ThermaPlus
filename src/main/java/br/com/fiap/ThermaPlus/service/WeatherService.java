package br.com.fiap.ThermaPlus.service;

import br.com.fiap.ThermaPlus.model.Localizacao;
import br.com.fiap.ThermaPlus.model.dto.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final String API_KEY = "AIzaSyAhKHWT3BOk7F14_oYdFQ6YeDrY-KsEN2I";
    private static final String BASE_URL = "https://weather.googleapis.com/v1";

    public WeatherResponse consultarClima(Localizacao localizacao) {
        String url = String.format(
                "%s/weather:lookup?key=%s&location.latitude=%.6f&location.longitude=%.6f",
                BASE_URL, API_KEY, localizacao.getLatitude(), localizacao.getLongitude());

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, WeatherResponse.class);
    }

    public String gerarRecomendacao(WeatherResponse clima) {
        StringBuilder recomendacao = new StringBuilder("Recomendações baseadas nas condições climáticas:\n");

        double temp = fahrenheitToCelsius(clima.getTemperature().getDegrees());
        double aparente = fahrenheitToCelsius(clima.getFeelsLikeTemperature().getDegrees());
        int umidade = clima.getRelativeHumidity();
        int uv = clima.getUvIndex();
        int chuva = clima.getPrecipitation().getProbability().getPercent();

        if (temp >= 35) {
            recomendacao.append("- Muito quente! Beba água e evite o sol.\n");
        } else if (temp <= 10) {
            recomendacao.append("- Frio intenso! Agasalhe-se bem.\n");
        } else {
            recomendacao.append("- Temperatura agradável.\n");
        }

        if (Math.abs(aparente - temp) >= 5) {
            recomendacao.append("- A sensação térmica difere da temperatura real. Vista-se com cautela.\n");
        }

        if (umidade >= 80) {
            recomendacao.append("- Alta umidade. Evite atividades físicas pesadas.\n");
        } else if (umidade <= 30) {
            recomendacao.append("- Umidade baixa! Hidrate-se e proteja a pele.\n");
        }

        if (uv >= 8) {
            recomendacao.append("- Índice UV muito alto! Use protetor solar e evite o sol.\n");
        } else if (uv >= 5) {
            recomendacao.append("- UV moderado. Evite exposição prolongada.\n");
        }

        if (chuva >= 60) {
            recomendacao.append("- Alta chance de chuva! Leve guarda-chuva.\n");
        } else if (chuva >= 30) {
            recomendacao.append("- Possibilidade de chuva. Fique atento.\n");
        }

        return recomendacao.toString();
    }

    private double fahrenheitToCelsius(double f) {
        return (f - 32) * 5 / 9;
    }
}
