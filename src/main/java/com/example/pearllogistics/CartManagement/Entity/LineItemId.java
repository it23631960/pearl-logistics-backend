package com.example.pearllogistics.CartManagement.Entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemId implements Serializable {
    private Long cart;
    private Long item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineItemId that)) return false;
        return Objects.equals(cart, that.cart) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart, item);
    }
}
