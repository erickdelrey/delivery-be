package xyz.mynt.delivery.exception;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DefaultExceptionAdvice {

    @ExceptionHandler(DeliveryServiceException.class)
    public ResponseEntity<RestResponse> handleForbiddenException(DeliveryServiceException deliveryServiceException,
        HttpServletRequest httpServletRequest) {

        log.error("class {}, exception {}, request {}", deliveryServiceException.getClass().getSimpleName(), deliveryServiceException,
            httpServletRequest);
        RestResponse restResponse = new RestResponse(deliveryServiceException.getHttpStatusCode(), deliveryServiceException.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        int httpStatusCode = Integer.parseInt(Integer.toString(deliveryServiceException.getHttpStatusCode()).substring(0, 3));

        return new ResponseEntity<>(restResponse, responseHeaders, HttpStatus.valueOf(httpStatusCode));
    }
}
