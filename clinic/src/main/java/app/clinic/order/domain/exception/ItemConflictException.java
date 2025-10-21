package app.clinic.order.domain.exception;

/**
 * Se lanza cuando hay conflicto o duplicado entre ítems.
 */
public class ItemConflictException extends RuntimeException {

    private final String errorCode;

    public ItemConflictException(String message) {
        super(message);
        this.errorCode = "ITEM_CONFLICT";
    }

    public ItemConflictException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
