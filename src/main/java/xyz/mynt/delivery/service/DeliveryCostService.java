package xyz.mynt.delivery.service;

import xyz.mynt.delivery.model.GetDeliveryCostRequest;
import xyz.mynt.delivery.model.GetDeliveryCostResponse;

public interface DeliveryCostService {
    GetDeliveryCostResponse calculateDeliveryCost(GetDeliveryCostRequest request);
}
