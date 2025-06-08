package br.com.fiap.ThermaPlus.controller;

import br.com.fiap.ThermaPlus.model.Localizacao;
import br.com.fiap.ThermaPlus.model.Usuario;
import br.com.fiap.ThermaPlus.model.dto.WeatherResponse;
import br.com.fiap.ThermaPlus.repository.LocalizacaoRepository;
import br.com.fiap.ThermaPlus.service.WeatherService;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/localizacoes")
public class LocalizacaoController {

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    @Autowired
    private WeatherService weatherService;

    @PostMapping
    public Localizacao registrarLocalizacao(@RequestBody @Valid Localizacao localizacao,
                                            @AuthenticationPrincipal Usuario usuario) {

        WeatherResponse clima = weatherService.consultarClima(localizacao);

        double tempAtualC = fahrenheitToCelsius(clima.getTemperature().getDegrees());
        double tempAparenteC = fahrenheitToCelsius(clima.getFeelsLikeTemperature().getDegrees());

        localizacao.setUsuario(usuario);
        localizacao.setDataHora(LocalDateTime.now());
        localizacao.setTemperaturaAtual(tempAtualC);
        localizacao.setTemperaturaAparente(tempAparenteC);
        localizacao.setUmidade(clima.getRelativeHumidity());
        localizacao.setIndiceUv(clima.getUvIndex());
        localizacao.setChanceDeChuva(clima.getPrecipitation().getProbability().getPercent());

        return localizacaoRepository.save(localizacao);
    }

    @GetMapping
    public Page<Localizacao> listarMinhasLocalizacoes(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataHora") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sortOrder = direction.equalsIgnoreCase("asc")
                ? Sort.by(sort).ascending()
                : Sort.by(sort).descending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return localizacaoRepository.findByUsuario(usuario, pageable);
    }

    @GetMapping("/recomendacoes")
    public String obterRecomendacoes(@AuthenticationPrincipal Usuario usuario) {
        Pageable ultimaPagina = PageRequest.of(0, 1, Sort.by("dataRegistro").descending());
        Page<Localizacao> pagina = localizacaoRepository.findByUsuario(usuario, ultimaPagina);

        if (pagina.isEmpty()) {
            return "Nenhuma localização registrada para o usuário.";
        }

        Localizacao ultimaLocalizacao = pagina.getContent().get(0);
        WeatherResponse clima = weatherService.consultarClima(ultimaLocalizacao);

        return weatherService.gerarRecomendacao(clima);
    }

    private double fahrenheitToCelsius(double f) {
        return (f - 32) * 5 / 9;
    }
    @PutMapping("/{id}")
    public Localizacao atualizarLocalizacao(
            @PathVariable Long id,
            @RequestBody @Valid Localizacao localizacaoAtualizada,
            @AuthenticationPrincipal Usuario usuario) {

        Localizacao localizacaoExistente = localizacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada"));

        if (!localizacaoExistente.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode alterar essa localização");
        }

        localizacaoExistente.setLatitude(localizacaoAtualizada.getLatitude());
        localizacaoExistente.setLongitude(localizacaoAtualizada.getLongitude());

        WeatherResponse clima = weatherService.consultarClima(localizacaoExistente);

        double tempAtualC = fahrenheitToCelsius(clima.getTemperature().getDegrees());
        double tempAparenteC = fahrenheitToCelsius(clima.getFeelsLikeTemperature().getDegrees());

        localizacaoExistente.setTemperaturaAtual(tempAtualC);
        localizacaoExistente.setDataHora(LocalDateTime.now());
        localizacaoExistente.setTemperaturaAparente(tempAparenteC);
        localizacaoExistente.setUmidade(clima.getRelativeHumidity());
        localizacaoExistente.setIndiceUv(clima.getUvIndex());
        localizacaoExistente.setChanceDeChuva(clima.getPrecipitation().getProbability().getPercent());

        return localizacaoRepository.save(localizacaoExistente);
    }
    @DeleteMapping("/{id}")
    public void deletarLocalizacao(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        Localizacao localizacaoExistente = localizacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada"));

        if (!localizacaoExistente.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode deletar essa localização");
        }

        localizacaoRepository.delete(localizacaoExistente);
    }

}
