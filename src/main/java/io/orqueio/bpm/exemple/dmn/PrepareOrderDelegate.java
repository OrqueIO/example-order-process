package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import io.orqueio.bpm.exemple.dmn.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("prepareOrderDelegate")
public class PrepareOrderDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(PrepareOrderDelegate.class);

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");
        log.info("Preparing order #{}...", orderId);

        orderService.updateOrderStatus(orderId, "PREPARING");
        execution.setVariable("orderStatus", "PREPARING");
    }
}