package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.DecisionService;
import io.orqueio.bpm.engine.RuntimeService;
import io.orqueio.bpm.engine.variable.Variables;
import io.orqueio.bpm.exemple.dmn.model.Order;
import io.orqueio.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import io.orqueio.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableProcessApplication
public class CommandeClientApplication {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private DecisionService decisionService;

    public static void main(String... args) {
        SpringApplication.run(CommandeClientApplication.class, args);
    }

    @EventListener
    @Transactional
    public void onPostDeploy(PostDeployEvent event) {

        Order order = new Order("VIP", 600.0, true);
        order.setPaymentValid(true);
        order.setStatus("IN_PROGRESS");

        Map<String, Object> dmnVars = new HashMap<>();
        dmnVars.put("clientType", order.getClientType());
        dmnVars.put("orderAmount", order.getOrderAmount());

        var decisionResult = decisionService.evaluateDecisionTableByKey("discountDecision", Variables.fromMap(dmnVars));
        double discount = Double.parseDouble(decisionResult.getSingleResult().get("discount").toString());
        order.setDiscountApplied(discount);
        order.calculateFinalAmount();

        
        Map<String, Object> processVars = new HashMap<>();
        processVars.put("clientType", order.getClientType());
        processVars.put("orderAmount", order.getOrderAmount());
        processVars.put("discount", order.getDiscountApplied());
        processVars.put("finalAmount", order.getFinalAmount());
        processVars.put("paymentOnline", order.getPaymentOnline());
        processVars.put("paymentValid", order.getPaymentValid());
        processVars.put("status", order.getStatus());

        runtimeService.startProcessInstanceByKey("Process_CommandeClient", processVars);
    }
}