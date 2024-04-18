package com.nilknow.yifanerp2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilknow.yifanerp2.config.security.UserIdHolder;
import com.nilknow.yifanerp2.dto.CreateOrderDto;
import com.nilknow.yifanerp2.entity.*;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.repository.LoginUserRepository;
import com.nilknow.yifanerp2.repository.OrderRepository;
import com.nilknow.yifanerp2.repository.ProductRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class OrderService {

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private DeliveryService deliveryService;
    @Resource
    private ProductRepository productRepository;
    @Resource
    private ActionLogService actionLogService;
    @Resource
    private LoginUserRepository loginUserRepository;
    @Resource
    private ObjectMapper objectMapper;

    @Transactional
    public Order create(CreateOrderDto order,String source) throws ResException, JsonProcessingException {
        if (order.getProductId() == null) {
            throw new ResException("Order must have a product ID");
        }
        if (order.getCount() == null || order.getCount() <= 0) {
            throw new ResException("Order quantity must be a positive number");
        }
        if (!StringUtils.hasText(order.getCustomer())) {
            throw new ResException("Order must have a recipient name");
        }
        Optional<Product> productOpt = productRepository.findById(order.getProductId());
        if (productOpt.isEmpty()) {
            throw new ResException("Product with ID " + order.getProductId() + " not found");
        }
        if (StringUtils.hasText(order.getSerialNum())) {
            List<Order> existingOrders = orderRepository.findAllBySerialNum(order.getSerialNum());
            if (!CollectionUtils.isEmpty(existingOrders)) {
                throw new ResException("Order with the same serial number already exists");
            }
        }

        Optional<Delivery> deliveryOpt = deliveryService.findBySerialNum(order.getDeliverySerialNum());
        Delivery delivery = deliveryOpt
                .orElseGet(() -> deliveryService.create(new Delivery(order.getDeliverySerialNum())));

        LoginUser user = loginUserRepository.findById(UserIdHolder.get()).get();

        Product product = productOpt.get();
        Order newOrder = new Order();
        newOrder.setSerialNum(order.getSerialNum());
        newOrder.setProduct(product);
        newOrder.setCount(order.getCount());
        newOrder.setProduceDaysTake(order.getProduceDaysTake());
        newOrder.setCustomer(order.getCustomer());
        newOrder.setNote(order.getNote());
        newOrder.setDelivery(delivery);
        newOrder.setCreateTime(new Date());
        newOrder.setUpdateTime(new Date());
        Order savedOrder = orderRepository.save(newOrder);

        ActionLog actionLog = new ActionLog();
        actionLog.setEventType("add");
        actionLog.setAdditionalInfo(objectMapper.writeValueAsString(order));
        actionLog.setDescription("添加新的订单: " + product.getName() + " " + order.getCount());
        actionLog.setSource(source);
        actionLog.setUser(user);
        actionLog.setTableName("order");
        actionLog.setTimestamp(new Date());
        actionLog.setBatchId(UUID.randomUUID());

        actionLogService.save(actionLog);

        return savedOrder;
    }

    public List<Order> list() {
        return orderRepository.findAllByOrderByUpdateTimeDesc();
    }
}