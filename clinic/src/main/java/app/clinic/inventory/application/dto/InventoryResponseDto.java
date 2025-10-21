package app.clinic.inventory.application.dto;

import app.clinic.inventory.domain.model.InventoryType;

public record InventoryResponseDto(
    Long id,
    InventoryType type,
    String name,
    double cost
) {}
