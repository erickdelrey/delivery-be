package xyz.mynt.delivery.config;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import xyz.mynt.delivery.enums.StatusCode;
import xyz.mynt.delivery.exception.DeliveryServiceException;
import xyz.mynt.delivery.utils.JsonObjectMapperHelper;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
            .errorHandler(new RestClientResponseErrorHandler())
            .setConnectTimeout(Duration.ofMillis(10000))
            .setReadTimeout(Duration.ofMillis(10000))
            .build();
    }

    private static class RestClientResponseErrorHandler implements ResponseErrorHandler {

        private final ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return errorHandler.hasError(response);
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            String resStr = IOUtils.toString(response.getBody());
            JsonNode jsonNode = JsonObjectMapperHelper.toJsonNode(resStr);
            log.error("error for api call : {}", resStr);
            String message = JsonObjectMapperHelper.findValue(jsonNode, "error");
            throw new DeliveryServiceException(StatusCode.BAD_REQUEST, message);
        }
    }
}
