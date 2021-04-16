package xyz.mynt.delivery.service.impl;

import java.util.Date;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.mynt.delivery.enums.DeliveryCostCalculationCondition;
import xyz.mynt.delivery.enums.StatusCode;
import xyz.mynt.delivery.exception.DeliveryServiceException;
import xyz.mynt.delivery.model.DeliveryCostRule;
import xyz.mynt.delivery.config.DeliveryCostRuleConfig;
import xyz.mynt.delivery.model.GetDeliveryCostRequest;
import xyz.mynt.delivery.model.GetDeliveryCostResponse;
import xyz.mynt.delivery.model.VoucherItem;
import xyz.mynt.delivery.service.DeliveryCostService;
import xyz.mynt.delivery.service.VoucherRestClientService;

@Service
public class DeliveryCostServiceImpl implements DeliveryCostService {

    @Autowired
    private DeliveryCostRuleConfig deliveryCostRuleConfig;

    @Autowired
    private VoucherRestClientService voucherRestService;

    @Override
    public GetDeliveryCostResponse calculateDeliveryCost(GetDeliveryCostRequest request) {

        if (MapUtils.isEmpty(deliveryCostRuleConfig.getDeliveryCostRuleMap())) {
            throw new DeliveryServiceException(StatusCode.INTERNAL_SERVER_ERROR, "Failed to load delivery cost rule map");
        }

        double volume = request.getHeight() * request.getLength() * request.getWidth();
        DeliveryCostRule ruleToFollow = null;
        for (Map.Entry<Integer, DeliveryCostRule> ruleMap : deliveryCostRuleConfig.getDeliveryCostRuleMap().entrySet()) {
            DeliveryCostRule rule = ruleMap.getValue();
            if (rule.getCondition() == DeliveryCostCalculationCondition.NONE) {
                ruleToFollow = rule;
                break;
            }

            double valueToCompare = 0;
            switch (rule.getMeasurement()) {
                case WEIGHT:
                    valueToCompare = request.getWeight();
                    break;
                case VOLUME:
                    valueToCompare = volume;
                    break;
            }

            boolean isSatisfied = false;
            switch (rule.getCondition()) {
                case GREATER_THAN:
                    isSatisfied = valueToCompare > rule.getAmount();
                    break;
                case LESS_THAN:
                    isSatisfied = valueToCompare < rule.getAmount();
                    break;
                case EQUAL:
                    isSatisfied = valueToCompare == rule.getAmount();
                    break;
                case GREATER_THAN_OR_EQUAL:
                    isSatisfied = valueToCompare >= rule.getAmount();
                    break;
                case LESS_THAN_OR_EQUAL:
                    isSatisfied = valueToCompare <= rule.getAmount();
                    break;
            }

            if (isSatisfied) {
                ruleToFollow = rule;
                break;
            }
        }

        if (ruleToFollow == null) {
            throw new DeliveryServiceException(StatusCode.DELIVERY_NOT_VALID);
        }

        Double initialCost = null;
        if (ruleToFollow.isValidForDelivery()) {
            switch (ruleToFollow.getCostMeasurement()) {
                case WEIGHT:
                    initialCost = request.getWeight() * ruleToFollow.getCostMultiplier();
                    break;
                case VOLUME:
                    initialCost = volume * ruleToFollow.getCostMultiplier();
                    break;
            }
        } else {
            throw new DeliveryServiceException(StatusCode.DELIVERY_NOT_VALID);
        }

        Double discount = null;
        if (StringUtils.isNotBlank(request.getVoucherCode())) {
            VoucherItem voucherItem = voucherRestService.getVoucherDetails(request.getVoucherCode());

            if (voucherItem == null || voucherItem.getExpiry() == null || voucherItem.getDiscount() == null) {
                throw new DeliveryServiceException(StatusCode.INTERNAL_SERVER_ERROR, "Exception encountered while retrieving voucher");
            }

            if (new Date().before(voucherItem.getExpiry())) {
                throw new DeliveryServiceException(StatusCode.VOUCHER_EXPIRED);
            }

            discount = voucherItem.getDiscount();
        }

        return GetDeliveryCostResponse.builder()
            .height(request.getHeight())
            .weight(request.getWeight())
            .width(request.getWidth())
            .length(request.getLength())
            .volume(volume)
            .voucherCode(request.getVoucherCode())
            .voucherDiscount(discount)
            .initialCost(initialCost)
            .finalCost(discount == null ? initialCost : initialCost - discount)
            .ruleName(ruleToFollow.getRuleName())
            .build();
    }
}
