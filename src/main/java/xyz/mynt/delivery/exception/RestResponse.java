package xyz.mynt.delivery.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.Data;


@Data
@JsonInclude(Include.NON_EMPTY)
public class RestResponse implements Serializable {

    private static final long serialVersionUID = 5488891567339848537L;

    private int code;
    private String message;

    public RestResponse(int httpStatusCode, String message) {
        this.code = httpStatusCode;
        this.message = message;
    }
}