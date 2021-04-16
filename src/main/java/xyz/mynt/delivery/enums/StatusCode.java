package xyz.mynt.delivery.enums;

public enum StatusCode {
    BAD_REQUEST(400_000, "Bad Request"),
    DELIVERY_NOT_VALID(400_001, "Not valid for delivery"),
    VOUCHER_EXPIRED(400_002, "Voucher code expired"),
    INTERNAL_SERVER_ERROR(500_000, "An unknown internal server error has occurred.");

    private int httpStatusCode;
    private String message;

    StatusCode(int httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getMessage() {
        return this.message;
    }
}
