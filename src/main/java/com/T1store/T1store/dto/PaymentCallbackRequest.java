package com.T1store.T1store.dto;

import lombok.Data;

@Data
public class PaymentCallbackRequest {
    private String paymentUid;
    private String orderUid;

}
