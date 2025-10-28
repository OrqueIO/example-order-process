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
        Boolean paymentValid = (Boolean) execution.getVariable("paymentValid");
        
        Object discountObj = execution.getVariable("discount");
        Double discount = 0.0;
        
        if (discountObj != null) {
            if (discountObj instanceof Long) {
                discount = ((Long) discountObj).doubleValue();
            } else if (discountObj instanceof Double) {
                discount = (Double) discountObj;
            } else if (discountObj instanceof Integer) {
                discount = ((Integer) discountObj).doubleValue();
            }
        }

        if (orderAmount == null) {
            throw new IllegalArgumentException("The variable 'orderAmount' is missing!");
        }


        Order order = new Order();
        order.setClientType(clientType);
        order.setOrderAmount(orderAmount);
        order.setPaymentOnline(paymentOnline);
        order.setPaymentValid(paymentValid);
        order.setDiscountApplied(discount);
        order.setStatus("CREATED");

        order.calculateFinalAmount();

        execution.setVariable("finalAmount", order.getFinalAmount());

        Order savedOrder = orderService.save(order);
        execution.setVariable("orderId", savedOrder.getId());

    }
}