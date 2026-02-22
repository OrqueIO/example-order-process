package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import io.orqueio.bpm.exemple.dmn.model.Order;
import io.orqueio.bpm.exemple.dmn.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("saveOrderDelegate")
public class SaveOrderDelegate implements JavaDelegate {

    @Autowired
    private OrderService orderService;
    
    @Override
    public void execute(DelegateExecution execution) {

        String clientType = (String) execution.getVariable("clientType");
        Double orderAmount = (Double) execution.getVariable("orderAmount");
        Boolean paymentOnline = (Boolean) execution.getVariable("paymentOnline");

        if (orderAmount == null) {
            throw new IllegalArgumentException("The variable 'orderAmount' is missing!");
        }

        Order order = new Order();
        order.setClientType(clientType);
        order.setOrderAmount(orderAmount);
        order.setPaymentOnline(paymentOnline != null ? paymentOnline : false);
        order.setDiscountApplied(0.0);
        order.setStatus("CREATED");

        order.calculateFinalAmount();

        execution.setVariable("finalAmount", order.getFinalAmount());

        Order savedOrder = orderService.save(order);
        execution.setVariable("orderId", savedOrder.getId());
        execution.setVariable("orderStatus", savedOrder.getStatus());

    }
}