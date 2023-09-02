package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 9/2/2023 1:40 PM
@Last Modified 9/2/2023 1:40 PM
Version 1.0
*/

import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.dto.OrderCountReport;
import com.juaracoding.foodspring.enums.OrderStatus;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.OrderItem;
import com.juaracoding.foodspring.repository.OrderItemRepository;
import com.juaracoding.foodspring.repository.ShopOrderRepository;
import com.juaracoding.foodspring.utils.CalcUtils;
import com.juaracoding.foodspring.utils.ConstantMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class SalesReportService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ShopOrderRepository shopOrderRepository;


    private String[] strExceptions = new String[2];

    public SalesReportService() {
        strExceptions[0] = "SalesReportService";
    }

    public double getTodayTotalIncome() {
        List<OrderItem> orderItems = orderItemRepository.findAllOrderedItemToday(OrderStatus.COMPLETED);
        return getTotalIncome(orderItems);
    }

    public double getCurrentMonthTotalIncome() {
        List<OrderItem> orderItems = orderItemRepository.findAllOrderedItemByOrderStatusAndMonth(OrderStatus.COMPLETED, new Date());
        return getTotalIncome(orderItems);
    }

    public Map<String, Object> getTodayOrderCountReport(WebRequest request) {
        OrderCountReport report = null;
        try {
            CompletableFuture<Integer> todayActiveOrder = CompletableFuture.supplyAsync(() -> shopOrderRepository.countTodayActiveOrder());
            CompletableFuture<Integer> completedOrder = CompletableFuture.supplyAsync(() -> shopOrderRepository.countTodayOrderByOrderStatus(OrderStatus.COMPLETED));
            CompletableFuture<Integer> inProcessOrder = CompletableFuture.supplyAsync(() -> shopOrderRepository.countTodayOrderByOrderStatus(OrderStatus.ON_PROCESS));
            CompletableFuture<Integer> todayTotalOrder = CompletableFuture.supplyAsync(() -> shopOrderRepository.countTodayTotalOrder());
            CompletableFuture.allOf(todayActiveOrder, completedOrder, inProcessOrder).join();

            report = new OrderCountReport();
            report.setActiveOrder(todayActiveOrder.get());
            report.setCompletedOrder(completedOrder.get());
            report.setInProcessOrder(inProcessOrder.get());
            report.setTotalOrder(todayTotalOrder.get());
        } catch (Exception e) {
            strExceptions[1] = "getTodayOrderCountReport() --LINE 64";
            LoggingFile.exceptionString(strExceptions, e, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.FAILED_GET_ORDER_REPORT,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "SRS0001", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                HttpStatus.OK, report, null, request);
    }

    private double getTotalIncome(List<OrderItem> orderItems) {
        double total = 0.0;
        for (OrderItem item : orderItems) {
            int discount = Objects.isNull(item.getDiscountPercentage()) ? 0 : item.getDiscountPercentage();
            total += CalcUtils.getDiscountedPrice(item.getUnitPrice(), discount) * item.getQty();
        }
        return total;
    }
}
