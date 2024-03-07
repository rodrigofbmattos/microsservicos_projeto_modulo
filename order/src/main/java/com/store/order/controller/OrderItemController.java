package com.store.order.controller;

import com.store.order.domain.OrderItem;
import com.store.order.service.OrderItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order_item")
public class OrderItemController extends GenericController<OrderItem> {
    public OrderItemController(OrderItemService service) { super(service); }
}
