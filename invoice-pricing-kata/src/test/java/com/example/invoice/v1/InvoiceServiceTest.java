package com.example.invoice.v1;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceServiceTest {

    @Test
    void shouldPriceInvoiceWithFixedTwentyPercentTaxAndStoreIt() {
        InMemoryInvoiceRepository repository = new InMemoryInvoiceRepository();
        InvoiceService service = new InvoiceService(repository);

        Invoice invoice = new Invoice(
                List.of(
                        new LineItem(2, new BigDecimal("10.00")),
                        new LineItem(1, new BigDecimal("5.50"))
                )
        );

        Invoice priced = service.priceAndStore(invoice);

        assertThat(priced.subtotal()).isEqualByComparingTo(new BigDecimal("25.50"));
        assertThat(priced.tax()).isEqualByComparingTo(new BigDecimal("5.10"));
        assertThat(priced.total()).isEqualByComparingTo(new BigDecimal("30.60"));

        assertThat(repository.findAll())
                .hasSize(1)
                .first()
                .isSameAs(priced);
    }
}
