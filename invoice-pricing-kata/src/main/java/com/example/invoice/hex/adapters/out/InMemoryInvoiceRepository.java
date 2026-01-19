package com.example.invoice.hex.adapters.out;

import com.example.invoice.hex.domain.Invoice;
import com.example.invoice.hex.port.out.InvoiceRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InMemoryInvoiceRepository implements InvoiceRepository {

    private final List<Invoice> stored = new ArrayList<>();

    @Override
    public void save(Invoice invoice) {
        stored.add(invoice);
    }

    @Override
    public List<Invoice> findAll() {
        return Collections.unmodifiableList(stored);
    }
}
