package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/28/2023 7:47 AM
@Last Modified 8/28/2023 7:47 AM
Version 1.0
*/

import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.dto.OrderResponse;
import com.juaracoding.foodspring.dto.OrderStatusDTO;
import com.juaracoding.foodspring.enums.OrderStatus;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.ShopOrder;
import com.juaracoding.foodspring.repository.*;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.TransformToDTO;
import com.midtrans.service.MidtransSnapApi;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.*;
@Service
public class AdminOrderService {
    private final String[] strExceptionArr = new String[2];

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private MidtransSnapApi midtransSnapApi;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderService orderService;

    private TransformToDTO<ShopOrder, OrderResponse> transformer = new TransformToDTO<>();
    @Autowired
    private ShopOrderRepository shopOrderRepository;

    public AdminOrderService() {
        strExceptionArr[0] = "AdminOrderService";
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateOrderStatus(OrderStatusDTO orderStatusRequest, WebRequest request) {
        Long adminId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        if (Objects.isNull(orderStatusRequest.getOrderStatus()) || Objects.isNull(orderStatusRequest.getOrderId())) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INVALID_DATA,
                    HttpStatus.BAD_REQUEST, null, "OS0006", request);
        }
        Optional<ShopOrder> shopOrder = shopOrderRepository.findById(orderStatusRequest.getOrderId());
        if (shopOrder.isEmpty()) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_NO_ORDER_FOUND,
                    HttpStatus.NOT_FOUND, null, "OS0006", request);
        }
        shopOrder.get().setOrderStatus(orderStatusRequest.getOrderStatus());
        shopOrder.get().setModifiedBy(adminId);
        shopOrder.get().setUpdatedAt(LocalDateTime.now());
        shopOrderRepository.save(shopOrder.get());
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_UPDATE_ORDER_STATUS,
                HttpStatus.OK, orderService.convertToOrderResponseList(List.of(shopOrder.get())), null, request);
    }

    public Map<String, Object> getAllOrderByStatus(Pageable pageable, OrderStatus status, WebRequest request) {
        Page<ShopOrder> shopOrders;
        Map<String, Object> result;
        try {
            shopOrders = shopOrderRepository.findAllByOrderStatus(status, pageable);
            List<OrderResponse> response = orderService.convertToOrderResponseList(shopOrders.getContent());
            result = transformer.transformObject(new HashMap<>(), response, shopOrders);
        } catch (Exception e) {
            strExceptionArr[1] = "getAllOrderByStatus(Pageable pageable, OrderStatus status, WebRequest request) --LINE 94";
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_TO_PROCEED,
                    HttpStatus.INTERNAL_SERVER_ERROR, new HashMap<>(), "OS0002", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                HttpStatus.OK, result, null, request);
    }


    public OrderStatusDTO getOrderUpdateStatusPath(OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PAID -> OrderStatusDTO.builder()
                    .orderStatus(OrderStatus.ON_PROCESS)
                    .buttonName("PROCESS")
                    .updateStatusPath(ServicePath.ADMIN_UPDATE_STATUS)
                    .build();
            case ON_PROCESS -> OrderStatusDTO.builder()
                    .orderStatus(OrderStatus.COMPLETED)
                    .buttonName("FINISH")
                    .updateStatusPath(ServicePath.ADMIN_UPDATE_STATUS)
                    .build();
            default -> OrderStatusDTO.builder()
                    .orderStatus(orderStatus).build();
        };

    }
}
