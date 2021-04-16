package xyz.mynt.delivery.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.mynt.delivery.enums.StatusCode;

@Getter
@NoArgsConstructor
public class DeliveryServiceException extends RuntimeException {
    private int httpStatusCode;
    private String message;

    public DeliveryServiceException(StatusCode errorCode) {
        this.httpStatusCode = errorCode.getHttpStatusCode();
        this.message = errorCode.getMessage();
    }

    public DeliveryServiceException(StatusCode errorCode, String message) {
        this.httpStatusCode = errorCode.getHttpStatusCode();
        this.message = message;
    }
}
