package com.nilknow.yifanerp2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nilknow.yifanerp2.dto.ActionLogDto;
import com.nilknow.yifanerp2.dto.CreateOrderDto;
import com.nilknow.yifanerp2.entity.Order;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.service.ActionLogService;
import com.nilknow.yifanerp2.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private ActionLogService actionLogService;

    @PostMapping
    public Res<Order> create(@RequestBody CreateOrderDto order, @RequestParam String source) throws JsonProcessingException, ResException {
        Order createdOrder = orderService.create(order, source);
        return new Res<Order>().success(createdOrder);
    }

    @GetMapping
    public Res<List<Order>> list() {
        List<Order> orders = orderService.list();
        return new Res<List<Order>>().success(orders);
    }

    @GetMapping("/action_log/list")
    public Res<List<ActionLogDto>> actionLogList() {
        List<ActionLogDto> actionLogs = actionLogService.listByTableName("order");
        return new Res<List<ActionLogDto>>().success(actionLogs);
    }

    @DeleteMapping
    public Res<String> delete(@RequestParam Long orderId,
                              @RequestParam String source)
            throws JsonProcessingException, ResException {
        orderService.delete(orderId, source);
        return new Res<String>().success("success");
    }

//  todo
//  @PutMapping
//  public Res<Order> update(@RequestBody Order order) {
//    Order updatedOrder = orderService.updateOrder(order);
//    return new Res<Order>().success(updatedOrder);
//  }
//
//  @PatchMapping("/{id}/state")
//  public Res<Order> updateOrderState(@PathVariable Long id, @RequestParam String state) {
//    Order updatedOrder = orderService.updateOrderState(id, state);
//    return new Res<Order>().success(updatedOrder);
//  }
//
//  @PatchMapping("/{id}/serialNum")
//  public Res<Order> updateOrderSerialNum(@PathVariable Long id, @RequestParam String serialNum) {
//    Order updatedOrder = orderService.updateOrderSerialNum(id, serialNum);
//    return new Res<Order>().success(updatedOrder);
//  }
//
//   @DeleteMapping("/{id}")
//   public Res<String> deleteOrder(@PathVariable Long id) {
//     orderService.deleteOrder(id);
//     return new Res<String>().success("");
//   }
}
