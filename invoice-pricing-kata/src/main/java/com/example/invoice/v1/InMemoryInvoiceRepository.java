package com.example.invoice.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InMemoryInvoiceRepository {

    private final List<Invoice> stored = new ArrayList<>();

    public void save(Invoice invoice) {
        stored.add(invoice);
    }

    public List<Invoice> findAll() {
        return Collections.unmodifiableList(stored);
    }
}
