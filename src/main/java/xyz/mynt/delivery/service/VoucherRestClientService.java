package xyz.mynt.delivery.service;

import xyz.mynt.delivery.model.VoucherItem;

public interface VoucherRestClientService {

    VoucherItem getVoucherDetails(String voucherCode);
}
