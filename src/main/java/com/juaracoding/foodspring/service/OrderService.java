package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/27/2023 9:03 AM
@Last Modified 8/27/2023 9:03 AM
Version 1.0
*/

import com.foodspring.utils.CurrencyFormatter;
import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.dto.*;
import com.juaracoding.foodspring.enums.OrderStatus;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.CartItem;
import com.juaracoding.foodspring.model.OrderItem;
import com.juaracoding.foodspring.model.ShopOrder;
import com.juaracoding.foodspring.model.User;
import com.juaracoding.foodspring.model.mapper.CartItemMapper;
import com.juaracoding.foodspring.model.mapper.OrderItemMapper;
import com.juaracoding.foodspring.model.mapper.UserMapper;
import com.juaracoding.foodspring.repository.*;
import com.juaracoding.foodspring.utils.CalcUtils;
import com.juaracoding.foodspring.utils.ConstantMessage;
import com.juaracoding.foodspring.utils.PaymentUtils;
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
@Transactional
public class OrderService {

    private final String[] strExceptionArr = new String[2];
    private final UserRepository userRepository;
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
    private TransformToDTO<ShopOrder, OrderResponse> transformer = new TransformToDTO<>();
    @Autowired
    private ShopOrderRepository shopOrderRepository;

    public OrderService(UserRepository userRepository) {
        strExceptionArr[0] = "OrderService";
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> createOrder(WebRequest request) {
        Long userId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        ShopOrder shopOrder = new ShopOrder();
        try {
            List<CartItem> cartItems = cartItemRepository.findByCartUserUserId(userId);
            Optional<User> user = userRepository.findById(userId);
            if (cartItems.size() == 0) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.CART_EMPTY,
                        HttpStatus.OK, null, null, request);
            }
            List<OrderItem> orderItems = CartItemMapper.INSTANCE.toOrderItemList(cartItems);
            shopOrder.setUser(cartItems.get(0).getCart().getUser());
            shopOrder.setOrderDate(LocalDateTime.now());
            shopOrderRepository.save(shopOrder);
            orderItems.forEach(item -> {
                item.setShopOrder(shopOrder);
            });

            orderItemRepository.saveAllAndFlush(orderItems);
            cartItemRepository.deleteAllInBatch(cartItems);
            int grandTotal = getGrandTotalOrder(orderItems).intValue();
            List<MidtransItemDetails> midtransItemDetails = OrderItemMapper.INSTANCE.toMidtransItemDetailsList(orderItems);
            CustomerDetails customerDetails = new CustomerDetails();
            if (user.isPresent()) {
                customerDetails = UserMapper.INSTANCE.toCustomerDetails(user.get());
            }
            TransactionDetailsMidtrans transactionDetailsMidtrans = new TransactionDetailsMidtrans();
            transactionDetailsMidtrans.setOrder_id(shopOrder.getShopOrderId());
            transactionDetailsMidtrans.setGross_amount(grandTotal);
            Map<String, Object> paymentPayload = PaymentUtils.getPaymentRequestBody(transactionDetailsMidtrans, customerDetails, midtransItemDetails);
            String snapToken = midtransSnapApi.createTransactionToken(paymentPayload);
            shopOrder.setOrderItems(orderItems);
            shopOrder.setSnapToken(snapToken);
            shopOrder.setGrandTotalIDR(CurrencyFormatter.toRupiah((double) grandTotal));
            shopOrder.setGrandTotal((double) grandTotal);


        } catch (Exception e) {
            strExceptionArr[1] = "createOrder(WebRequest request)";
            LoggingFile.exceptionString(strExceptionArr, e, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATE_ORDER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "OS0001", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CREATE_ORDER,
                HttpStatus.CREATED, convertToOrderResponseList(List.of(shopOrder)).get(0), null, request);
    }


    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> handleMidtransStatusUpdate(MidtransNotif midtransNotif, WebRequest request) {
        Optional<ShopOrder> shopOrder;
        try {
            shopOrder = shopOrderRepository.findById(midtransNotif.getOrderId());
            if (shopOrder.isEmpty()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_NO_ORDER_FOUND,
                        HttpStatus.NOT_ACCEPTABLE, null, "OS0003", request);
            }
            ShopOrder order = shopOrder.get();
            if (midtransNotif.getTransactionStatus().equals("settlement") || midtransNotif.getTransactionStatus().equals("capture")) {
                order.setOrderStatus(OrderStatus.PAID);
                order.setUpdatedAt(LocalDateTime.now());
                order.setMidtransTransactionId(midtransNotif.getTransactionId());
                order.setPaymentType(midtransNotif.getPaymentType());
            } else if (midtransNotif.getTransactionStatus().equals("deny") || midtransNotif.getTransactionStatus().equals("expired")) {
                order.setOrderStatus(OrderStatus.CANCELED);
                order.setUpdatedAt(LocalDateTime.now());
                order.setMidtransTransactionId(midtransNotif.getTransactionId());
                order.setPaymentType(midtransNotif.getPaymentType());
            } else {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ORDER_STATUS_NOT_CHANGED,
                        HttpStatus.OK, order, null, request);
            }
            shopOrderRepository.save(order);
        } catch (Exception e) {
            strExceptionArr[1] = "updateOrderPaymentStatus(MidtransNotif midtransNotif, WebRequest request) --LINE 139";
            LoggingFile.exceptionString(strExceptionArr, e, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_UPDATE_ORDER_STATUS,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "OS0003", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_UPDATE_ORDER_STATUS,
                HttpStatus.OK, shopOrder.orElse(null), "OS0003", request);
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
                HttpStatus.OK, shopOrder.orElse(null), null, request);
    }
    public Map<String, Object> getAllOrderByStatus(Pageable pageable, OrderStatus status, WebRequest request) {
        Long userId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        Page<ShopOrder> shopOrders;
        Map<String, Object> result;
        try {
            shopOrders = shopOrderRepository.findAllByUserUserIdAndOrderStatus(userId, status, pageable);
            List<OrderResponse> response = convertToOrderResponseList(shopOrders.getContent());
            result = transformer.transformObject(new HashMap<>(), response, shopOrders);
        } catch (Exception e) {
            strExceptionArr[1] = "getAllOrder(Pageable pageable, WebRequest request) --LINE 94";
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATE_ORDER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "OS0002", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                HttpStatus.OK, result, null, request);
    }

    public Map<String, Object> getShopOrderById(String shopOrderId, WebRequest request) {
        Optional<ShopOrder> shopOrder = shopOrderRepository.findById(shopOrderId);
        if (shopOrder.isEmpty()) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_NO_ORDER_FOUND,
                    HttpStatus.NOT_FOUND, null, null, request);
        }
        List<OrderResponse> orderResponses = convertToOrderResponseList(List.of(shopOrder.get()));
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                HttpStatus.OK, orderResponses.get(0), null, request);
    }

    private Double getGrandTotalOrder(List<OrderItem> items) {
        double total = 0;
        for (OrderItem item : items) {
            double price = item.getUnitPrice() * item.getQty();
            if (item.getDiscountPercentage() != null) {
                price = CalcUtils.getDiscountedPrice(item.getUnitPrice(), item.getDiscountPercentage());
            }
            total += price;
        }
        return total;
    }

    private List<OrderResponse> convertToOrderResponseList(List<ShopOrder> shopOrders) {
        List<OrderResponse> result = new ArrayList<>();
        for (ShopOrder order : shopOrders) {
            OrderResponse res = new OrderResponse();
            List<OrderItemResponse> orderItems = OrderItemMapper.INSTANCE.toOrderItemResponseList(order.getOrderItems());
            double grandTotal = orderItems
                    .stream()
                    .mapToDouble(OrderItemResponse::getTotalPrice)
                    .sum();
            res.setOrderItems(orderItems);
            res.setGrandTotal(grandTotal);
            res.setGrandTotalIDR(CurrencyFormatter.toRupiah(grandTotal));
            res.setShopOrderId(order.getShopOrderId());
            res.setCreatedAt(order.getCreatedAt());
            res.setIsPaid(!order.getOrderStatus().equals(OrderStatus.UNPAID));
            res.setSnapToken(order.getSnapToken());
            result.add(res);
        }
        return  result;
    }
}