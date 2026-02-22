package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import io.orqueio.bpm.exemple.dmn.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("capturePaymentDelegate")
public class CapturePaymentDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(CapturePaymentDelegate.class);

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");
        Double orderAmount = (Double) execution.getVariable("orderAmount");
        Double finalAmount = (Double) execution.getVariable("finalAmount");
        Boolean simulateFailure = (Boolean) execution.getVariable("simulatePaymentFailure");


        boolean paymentSuccess = simulateFailure == null || !simulateFailure;

        if (paymentSuccess) {
            log.info("✓ Payment captured successfully for order #{} (amount: {}€, final: {}€)",
                     orderId, orderAmount, finalAmount);
            execution.setVariable("paymentValid", true);
            orderService.updateOrderStatus(orderId, "PAYMENT_CAPTURED");
            execution.setVariable("orderStatus", "PAYMENT_CAPTURED");
        } else {
            log.error("✗ Payment failed for order #{} (amount: {}€, final: {}€) - Bank rejected the transaction",
                      orderId, orderAmount, finalAmount);
            execution.setVariable("paymentValid", false);
            orderService.updateOrderStatus(orderId, "PAYMENT_FAILED");
            execution.setVariable("orderStatus", "PAYMENT_FAILED");
        }
    }
}