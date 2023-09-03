package com.juaracoding.foodspring.service;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 9/3/2023 7:24 AM
@Last Modified 9/3/2023 7:24 AM
Version 1.0
*/

import com.foodspring.utils.CurrencyFormatter;
import com.foodspring.utils.LoggingFile;
import com.foodspring.utils.PDFGeneratorUtil;
import com.foodspring.utils.ReadTextFileSB;
import com.juaracoding.foodspring.dto.Invoice;
import com.juaracoding.foodspring.dto.InvoiceItem;
import com.juaracoding.foodspring.handler.ResponseHandler;
import com.juaracoding.foodspring.model.ShopOrder;
import com.juaracoding.foodspring.model.User;
import com.juaracoding.foodspring.model.mapper.OrderItemMapper;
import com.juaracoding.foodspring.repository.ShopOrderRepository;
import com.juaracoding.foodspring.repository.UserRepository;
import com.juaracoding.foodspring.utils.ConstantMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private PDFGeneratorUtil pdfGeneratorUtil;

    private String[] strExceptions = new String[2];

    @Autowired
    private ShopOrderRepository shopOrderRepository;
    private final UserRepository userRepository;

    public InvoiceService(UserRepository userRepository) {
        strExceptions[0] = "InvoiceService";
        this.userRepository = userRepository;
    }

    public Map<String, Object> generateOrderInvoice(String orderId, WebRequest request) {
        Invoice invoice = new Invoice();
        Long userId = (Long) request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION);
        byte[] invoicePdf = null;
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<ShopOrder> order = shopOrderRepository.findById(orderId);
            Optional<User> user = userRepository.findById(userId);
            if (order.isEmpty() || user.isEmpty()) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_NO_ORDER_FOUND,
                        HttpStatus.NOT_FOUND, null, "IS0001", request);
            }

            List<InvoiceItem> invoiceItems = OrderItemMapper.INSTANCE.toInvoiceItemList(order.get().getOrderItems());
            double grandTotal = invoiceItems.parallelStream().mapToDouble(InvoiceItem::getTotalPrice).sum();
            invoice.setInvoiceItems(invoiceItems);
            invoice.setDatetime(order.get().getCreatedAt());
            invoice.setSubTotalIDR(CurrencyFormatter.toRupiah(grandTotal));
            invoice.setGrandTotalIDR(CurrencyFormatter.toRupiah(grandTotal));
            User userTmp = user.get();
            invoice.setCustomerName(userTmp.getFirstName().concat(" ").concat(userTmp.getLastName()));
            invoice.setCustomerEmail(userTmp.getEmail());
            String fileName = String.format("INV_%s", order.get().getShopOrderId());
            result.put("fileName", fileName);
            Map<String, Object> data = new HashMap<>();
            data.put("data", invoice);
            String content = new ReadTextFileSB(new ClassPathResource("templates/report/invoice-template.html").getPath()).getContentFile();
            invoicePdf = pdfGeneratorUtil.htmlToPdf(content, data);
            result.put("file", invoicePdf);

        } catch (Exception e) {
            strExceptions[1] = "generateOrderInvoice(String orderId, WebRequest request)";
            LoggingFile.exceptionString(strExceptions, e, "y");
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CREATING_INVOICE,
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "IS0001", request);
        }
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CREATING_INVOICE,
                HttpStatus.OK, result, null, request);
    }

}
