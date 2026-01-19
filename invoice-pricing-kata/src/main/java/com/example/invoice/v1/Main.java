package com.example.invoice.v1;

import java.math.BigDecimal;
import java.util.List;

public final class Main {

    public static void main(String[] args) {
        InMemoryInvoiceRepository repository = new InMemoryInvoiceRepository();
        InvoiceService service = new InvoiceService(repository);

        Invoice invoice = new Invoice(
                List.of(
                        new LineItem(2, new BigDecimal("10.00")),
                        new LineItem(1, new BigDecimal("5.50"))
                )
        );

        Invoice priced = service.priceAndStore(invoice);
        System.out.println("Subtotal: " + priced.subtotal());
        System.out.println("Tax: " + priced.tax());
        System.out.println("Total: " + priced.total());
    }
}
