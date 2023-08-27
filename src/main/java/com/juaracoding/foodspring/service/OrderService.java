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
import com.juaracoding.foodspring.dto.CustomerDetails;
import com.juaracoding.foodspring.dto.MidtransItemDetails;
import com.juaracoding.foodspring.dto.TransactionDetailsMidtrans;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final String[] strExceptionArr = new String[2];

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

    private TransformToDTO<ShopOrder, ShopOrder> transformer = new TransformToDTO<>();

    @Autowired
    private ShopOrderRepository shopOrderRepository;
    private final UserRepository userRepository;

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
            List <MidtransItemDetails> midtransItemDetails = OrderItemMapper.INSTANCE.toMidtransItemDetailsList(orderItems);
            CustomerDetails customerDetails = new CustomerDetails();
            if (user.isPresent()) {
                customerDetails = UserMapper.INSTANCE.toCustomerDetails(user.get());
            }
            TransactionDetailsMidtrans transactionDetailsMidtrans = new TransactionDetailsMidtrans();
            transactionDetailsMidtrans.setOrder_id(shopOrder.getShopOrderId());
            transactionDetailsMidtrans.setGross_amount(grandTotal);
            Map<String, Object> paymentPayload = PaymentUtils.getPaymentRequestBody(transactionDetailsMidtrans, customerDetails, midtransItemDetails);
            String snapToken = midtransSnapApi.createTransactionToken(paymentPayload);
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
                HttpStatus.CREATED, shopOrder, null, request);
    }


    public Map<String, Object> getAllOrder(Pageable pageable, WebRequest request) {
        Long userId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        Page<ShopOrder> shopOrders;
        Map<String, Object> result;
        try {
            shopOrders = shopOrderRepository.findAllByUserUserIdAndOrderStatus(userId, OrderStatus.UNPAID, pageable);
            result = transformer.transformObject(new HashMap<>(), shopOrders.getContent(), shopOrders);
        } catch (Exception e) {
            strExceptionArr[1] = "getAllOrder(Pageable pageable, WebRequest request) --LINE 94";
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATE_ORDER,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "OS0002", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_RETRIEVE_DATA,
                HttpStatus.OK, result, null, request);
    }


    private Double getGrandTotalOrder(List<OrderItem> items) {
        double total = 0;
        for (OrderItem item: items) {
            double price = item.getUnitPrice() * item.getQty();
            if (item.getDiscountPercentage() != null) {
                price = CalcUtils.getDiscountedPrice(item.getUnitPrice(), item.getDiscountPercentage());
            }
            total += price;
        }
        return  total;
    }
}