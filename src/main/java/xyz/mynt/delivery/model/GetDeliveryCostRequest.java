package xyz.mynt.delivery.model;

import lombok.*;

@Getter
@Builder
public class GetDeliveryCostRequest {

    private final Double weight;

    private final Double height;

    private final Double width;

    private final Double length;

    private final String voucherCode;
}
