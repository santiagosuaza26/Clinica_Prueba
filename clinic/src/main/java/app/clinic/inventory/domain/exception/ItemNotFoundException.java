package app.clinic.inventory.domain.exception;

import app.clinic.shared.domain.exception.NotFoundException;

/**
 * Excepción lanzada cuando un ítem de inventario no se encuentra.
 */
public class ItemNotFoundException extends NotFoundException {

    public ItemNotFoundException(String message) {
        super("ITEM_NOT_FOUND", message, "InventoryItem", null);
    }

    public ItemNotFoundException(String itemType, Long itemId) {
        super("ITEM_NOT_FOUND",
              String.format("%s con ID %d no encontrado", itemType, itemId),
              itemType,
              itemId);
    }

    public ItemNotFoundException(String itemType, Long itemId, String additionalContext) {
        super("ITEM_NOT_FOUND",
              String.format("%s con ID %d no encontrado: %s", itemType, itemId, additionalContext),
              itemType,
              itemId);
        addDetail("additionalContext", additionalContext);
    }
}
