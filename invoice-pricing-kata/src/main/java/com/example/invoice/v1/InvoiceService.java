package com.example.invoice.v1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class InvoiceService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.20");

    private final InMemoryInvoiceRepository repository;

    public InvoiceService(InMemoryInvoiceRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public Invoice priceAndStore(Invoice invoice) {
        Objects.requireNonNull(invoice, "invoice");

        BigDecimal subtotal = invoice.lineItems().stream()
                .map(li -> li.unitPrice().multiply(BigDecimal.valueOf(li.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax).setScale(2, RoundingMode.HALF_UP);

        Invoice priced = new Invoice(invoice.lineItems(), subtotal, tax, total);
        repository.save(priced);
        return priced;
    }
}
