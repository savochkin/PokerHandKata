package com.example.invoice.hex.port.out;

import com.example.invoice.hex.domain.Invoice;

import java.util.List;

public interface InvoiceRepository {

    void save(Invoice invoice);

    List<Invoice> findAll();
}
