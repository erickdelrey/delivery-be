package xyz.mynt.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.mynt.delivery.enums.DeliveryCostCalculationCondition;
import xyz.mynt.delivery.enums.DeliveryCostCalculationMeasurement;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCostRule {
    private int priority;
    private String ruleName;
    private DeliveryCostCalculationMeasurement measurement;
    private DeliveryCostCalculationCondition condition;
    private Double amount;
    private boolean validForDelivery;
    private Double costMultiplier;
    private DeliveryCostCalculationMeasurement costMeasurement;
}
