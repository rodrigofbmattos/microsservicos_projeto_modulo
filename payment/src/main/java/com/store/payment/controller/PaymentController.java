package com.store.payment.controller;

import com.store.payment.domain.Payment;
import com.store.payment.service.PaymentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/payment")
public class PaymentController extends GenericController<Payment> {
    public PaymentController(PaymentService service) {
        super(service);
    }
}
