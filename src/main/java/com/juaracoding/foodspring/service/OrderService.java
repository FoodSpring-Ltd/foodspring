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
import com.juaracoding.foodspring.utils.*;
import com.midtrans.httpclient.error.MidtransError;
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
    private NotificationUtil notificationUtil;

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
            shopOrder.setOrderItems(orderItems);
            String snapToken = createSnapToken(shopOrder, grandTotal);
            shopOrder.setSnapToken(snapToken);
            shopOrder.setGrandTotalIDR(CurrencyFormatter.toRupiah((double) grandTotal));
            shopOrder.setGrandTotal((double) grandTotal);


        } catch (Exception e) {
            strExceptionArr[1] = "createOrder(WebRequest request)";
            LoggingFile.exceptionString(strExceptionArr, e, "y");
            if (e instanceof MidtransError) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATE_PAYMENT,
                        HttpStatus.INTERNAL_SERVER_ERROR, null, "OS0001", request);
            }
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATE_ORDER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "OS0001", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CREATE_ORDER,
                HttpStatus.CREATED, convertToOrderResponseList(List.of(shopOrder)).get(0), null, request);
    }


    @Transactional(rollbackFor = Exception.class)
    public void handleMidtransStatusUpdate(MidtransNotif midtransNotif, WebRequest request) {
        Optional<ShopOrder> shopOrder;
        try {
            shopOrder = shopOrderRepository.findById(midtransNotif.getOrderId());
            if (shopOrder.isEmpty()) {
                new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_NO_ORDER_FOUND,
                        HttpStatus.NOT_ACCEPTABLE, null, "OS0003", request);
                return;
            }
            ShopOrder order = shopOrder.get();
            if (midtransNotif.getTransactionStatus().equals("settlement") || midtransNotif.getTransactionStatus().equals("capture")) {
                order.setOrderStatus(OrderStatus.PAID);
                order.setUpdatedAt(LocalDateTime.now());
                order.setMidtransTransactionId(midtransNotif.getTransactionId());
                order.setPaymentType(midtransNotif.getPaymentType());
                notificationUtil.sendOrderPaidNotifToAdmin(order.getShopOrderId());
                notificationUtil.sendOrderPaidNotifToUser(order.getShopOrderId(), order.getUser().getUsername());
            } else if (midtransNotif.getTransactionStatus().equals("deny")
                    || midtransNotif.getTransactionStatus().equals("expire")
                    || midtransNotif.getTransactionStatus().equals("cancel")) {
                order.setOrderStatus(OrderStatus.CANCELED);
                order.setUpdatedAt(LocalDateTime.now());
                order.setMidtransTransactionId(midtransNotif.getTransactionId());
                order.setPaymentType(midtransNotif.getPaymentType());
                notificationUtil.sendOrderCanceledNotifToUser(order.getShopOrderId(), "Payment " + midtransNotif.getTransactionStatus(), order.getUser().getUsername());
            } else {
                new ResponseHandler().generateModelAttribut(ConstantMessage.ORDER_STATUS_NOT_CHANGED,
                        HttpStatus.OK, order, null, request);
                return;
            }
            shopOrderRepository.save(order);
        } catch (Exception e) {
            strExceptionArr[1] = "updateOrderPaymentStatus(MidtransNotif midtransNotif, WebRequest request) --LINE 139";
            LoggingFile.exceptionString(strExceptionArr, e, "y");
            new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_UPDATE_ORDER_STATUS,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "OS0003", request);
            return;
        }
        new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_UPDATE_ORDER_STATUS,
                HttpStatus.OK, shopOrder.orElse(null), "OS0003", request);
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
                price = CalcUtils.getDiscountedPrice(item.getUnitPrice(), item.getDiscountPercentage()) * item.getQty();
            }
            total += price;
        }
        return total;
    }

    protected List<OrderResponse> convertToOrderResponseList(List<ShopOrder> shopOrders) {
        List<OrderResponse> result = new ArrayList<>();
        for (ShopOrder order : shopOrders) {
            OrderResponse res = new OrderResponse();
            List<OrderItemResponse> orderItems = OrderItemMapper.INSTANCE.toOrderItemResponseList(order.getOrderItems());
            double grandTotal = orderItems
                    .stream()
                    .mapToDouble(OrderItemResponse::getTotalPrice)
                    .sum();
            if (order.getModifiedBy() != null) {
                Optional<User> user = userRepository.findById(order.getModifiedBy());
                user.ifPresent(value -> res.setAdminUsername(value.getUsername()));
            }

            res.setOrderItems(orderItems);
            res.setUpdatedAt(order.getUpdatedAt());
            res.setGrandTotal(grandTotal);
            res.setGrandTotalIDR(CurrencyFormatter.toRupiah(grandTotal));
            res.setShopOrderId(order.getShopOrderId());
            res.setCreatedAt(order.getCreatedAt());
            res.setIsPaid(!order.getOrderStatus().equals(OrderStatus.UNPAID));
            res.setStatus(order.getOrderStatus().toString());
            res.setSnapToken(order.getSnapToken());
            result.add(res);
        }
        return result;
    }

    public String createSnapToken(String orderId, Integer grandTotal) throws MidtransError {
        Optional<ShopOrder> order = shopOrderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new MidtransError("Order doesn't exist");
        }
        List<MidtransItemDetails> midtransItemDetails = OrderItemMapper.INSTANCE.toMidtransItemDetailsList(order.get().getOrderItems());
        CustomerDetails customerDetails = new CustomerDetails();
        if (order.get().getUser() != null) {
            customerDetails = UserMapper.INSTANCE.toCustomerDetails(order.get().getUser());
        }
        TransactionDetailsMidtrans transactionDetailsMidtrans = new TransactionDetailsMidtrans();
        transactionDetailsMidtrans.setOrder_id(order.get().getShopOrderId());
        transactionDetailsMidtrans.setGross_amount(grandTotal);
        Map<String, Object> paymentPayload = PaymentUtils.getPaymentRequestBody(transactionDetailsMidtrans, customerDetails, midtransItemDetails);
        String snapToken = midtransSnapApi.createTransactionToken(paymentPayload);
        order.get().setSnapToken(snapToken);
        shopOrderRepository.save(order.get());
        return snapToken;

    }
    public String createSnapToken(ShopOrder order, Integer grandTotal) throws MidtransError {
        List<MidtransItemDetails> midtransItemDetails = OrderItemMapper.INSTANCE.toMidtransItemDetailsList(order.getOrderItems());
        CustomerDetails customerDetails = new CustomerDetails();
        if (order.getUser() != null) {
            customerDetails = UserMapper.INSTANCE.toCustomerDetails(order.getUser());
        }
        TransactionDetailsMidtrans transactionDetailsMidtrans = new TransactionDetailsMidtrans();
        transactionDetailsMidtrans.setOrder_id(order.getShopOrderId());
        transactionDetailsMidtrans.setGross_amount(grandTotal);
        Map<String, Object> paymentPayload = PaymentUtils.getPaymentRequestBody(transactionDetailsMidtrans, customerDetails, midtransItemDetails);
        return midtransSnapApi.createTransactionToken(paymentPayload);

    }



}