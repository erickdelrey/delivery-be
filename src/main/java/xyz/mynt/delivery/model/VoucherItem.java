package xyz.mynt.delivery.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherItem {

    private String code;
    private Double discount;
    private Date expiry;
}
