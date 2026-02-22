package io.orqueio.bpm.exemple.dmn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import io.orqueio.bpm.exemple.dmn.service.OrderService;

@Component("payAtDeliveryDelegate")
public class PayAtDeliveryDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(PayAtDeliveryDelegate.class);

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");
        log.info("Marking order #{} for cash-on-delivery...", orderId);

        orderService.updateOrderStatus(orderId, "PAYMENT_AT_DELIVERY");
        execution.setVariable("orderStatus", "PAYMENT_AT_DELIVERY");
    }
}