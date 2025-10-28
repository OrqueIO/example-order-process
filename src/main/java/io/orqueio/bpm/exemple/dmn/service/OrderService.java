package io.orqueio.bpm.exemple.dmn.service;

import io.orqueio.bpm.exemple.dmn.model.Order;
import io.orqueio.bpm.exemple.dmn.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Créer un order avec double
    public Order createOrder(String clientType, double orderAmount, Boolean paymentOnline) {
        Order order = new Order(clientType, orderAmount, paymentOnline);
        return orderRepository.save(order);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, String status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public void applyDiscount(Long orderId, double discount) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setDiscountApplied(discount);
            order.calculateFinalAmount();
            orderRepository.save(order);
        });
    }
}
