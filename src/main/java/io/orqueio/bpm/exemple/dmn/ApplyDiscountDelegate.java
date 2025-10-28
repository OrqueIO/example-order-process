package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import io.orqueio.bpm.exemple.dmn.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("applyDiscountDelegate")
public class ApplyDiscountDelegate implements JavaDelegate {

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) {

        Long orderId = (Long) execution.getVariable("orderId");
        
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

        if (orderId == null) {
            throw new IllegalStateException("The order has not been created before applying the discount!");
        }

        orderService.applyDiscount(orderId, discount);
        execution.setVariable("finalAmount", 
            orderService.findById(orderId).get().getFinalAmount()
        );
    }
}
