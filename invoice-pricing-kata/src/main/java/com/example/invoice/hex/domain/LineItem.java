package com.example.invoice.hex.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class LineItem {

    private final int quantity;
    private final BigDecimal unitPrice;

    public LineItem(int quantity, BigDecimal unitPrice) {
        this.quantity = quantity;
        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
    }

    public int quantity() {
        return quantity;
    }

    public BigDecimal unitPrice() {
        return unitPrice;
    }
}
