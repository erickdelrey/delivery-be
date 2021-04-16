package xyz.mynt.delivery.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDeliveryCostResponse {
    private Double weight;
    private Double height;
    private Double width;
    private Double length;
    private Double volume;
    private String voucherCode;
    private Double voucherDiscount;
    private Double initialCost;
    private Double finalCost;
    private String ruleName;
}
