package com.example.pearllogistics.CartManagement.Repository;

import com.example.pearllogistics.CartManagement.Entity.Cart;
import com.example.pearllogistics.ItemManagement.Item;
import com.example.pearllogistics.CartManagement.Entity.LineItem;
import com.example.pearllogistics.CartManagement.Entity.LineItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LineItemRepository extends JpaRepository<LineItem, LineItemId> {
    List<LineItem> findByCart(Cart cart);
    Optional<LineItem> findByCartAndItem(Cart cart, Item item);
    void deleteByCartAndItem(Cart cart, Item item);
}
