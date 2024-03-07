package com.store.order.service.implement;

import com.store.order.domain.OrderItem;
import com.store.order.repository.OrderItemRepository;
import com.store.order.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImplements extends GenericServiceImplements<OrderItem, Long, OrderItemRepository>  implements OrderItemService {
    public OrderItemServiceImplements(OrderItemRepository repository) {
        super(repository);
    }
}
