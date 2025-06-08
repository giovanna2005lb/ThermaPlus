package br.com.fiap.ThermaPlus.model.dto;

import jakarta.validation.constraints.NotNull;

public record LocalizacaoRequest(
    @NotNull Double latitude,
    @NotNull Double longitude
) {}
