package com.example.invoice.hex.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class Invoice {

    private final List<LineItem> lineItems;
    private final BigDecimal subtotal;
    private final BigDecimal tax;
    private final BigDecimal total;

    public Invoice(List<LineItem> lineItems) {
        this(lineItems, null, null, null);
    }

    public Invoice(List<LineItem> lineItems, BigDecimal subtotal, BigDecimal tax, BigDecimal total) {
        this.lineItems = List.copyOf(Objects.requireNonNull(lineItems, "lineItems"));
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
    }

    public List<LineItem> lineItems() {
        return lineItems;
    }

    public BigDecimal subtotal() {
        return subtotal;
    }

    public BigDecimal tax() {
        return tax;
    }

    public BigDecimal total() {
        return total;
    }
}
