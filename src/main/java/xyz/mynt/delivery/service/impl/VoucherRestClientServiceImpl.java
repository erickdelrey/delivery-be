package xyz.mynt.delivery.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.mynt.delivery.constants.Constants;
import xyz.mynt.delivery.constants.URLConstants;
import xyz.mynt.delivery.enums.StatusCode;
import xyz.mynt.delivery.exception.DeliveryServiceException;
import xyz.mynt.delivery.model.VoucherItem;
import xyz.mynt.delivery.service.VoucherRestClientService;

@Slf4j
@Service
public class VoucherRestClientServiceImpl implements VoucherRestClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mynt.baseUrl}")
    private String myntBaseUrl;

    @Value("${deliveryService.apiKey:apikey}")
    private String apiKey;

    @Override
    public VoucherItem getVoucherDetails(String voucherCode) {
        String getVoucherUrl = myntBaseUrl + URLConstants.GET_VOUCHER.replace("{voucherCode}", voucherCode);
        HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(getVoucherUrl).queryParam(Constants.KEY, apiKey);
        ResponseEntity<VoucherItem> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uriComponentsBuilder.build().toUriString(), HttpMethod.GET, requestEntity,
                VoucherItem.class);
        }
        catch (ResourceAccessException rae) {
            throw new DeliveryServiceException(StatusCode.INTERNAL_SERVER_ERROR, "Exception encountered while retrieving voucher");
        }

        return responseEntity.getBody();
    }

}
