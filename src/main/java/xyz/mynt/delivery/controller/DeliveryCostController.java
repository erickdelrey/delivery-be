package xyz.mynt.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.mynt.delivery.model.GetDeliveryCostRequest;
import xyz.mynt.delivery.model.GetDeliveryCostResponse;
import xyz.mynt.delivery.service.DeliveryCostService;

@RestController
@RequestMapping(value = "/delivery/cost")
public class DeliveryCostController {

    @Autowired
    private DeliveryCostService deliveryCostService;

    @GetMapping
    public GetDeliveryCostResponse getDeliveryCost(@RequestParam Double weight,
        @RequestParam Double height, @RequestParam Double width,
        @RequestParam Double length, @RequestParam(required = false) String voucherCode) {

        return deliveryCostService.calculateDeliveryCost(
            GetDeliveryCostRequest.builder()
                .weight(weight)
                .height(height)
                .width(width)
                .length(length)
                .voucherCode(voucherCode)
                .build());
    }
}
