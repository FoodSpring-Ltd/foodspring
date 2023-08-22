package com.juaracoding.foodspring.repository;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/15/2023 9:20 PM
@Last Modified 8/15/2023 9:20 PM
Version 1.0
*/

import com.juaracoding.foodspring.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId = :productId AND ci.variant.variantId = :variantId AND ci.cart.cartId = :cartId")
    CartItem findByProductIdAndVariantIdAndCartId(String productId, Long variantId, Long cartId);
}
