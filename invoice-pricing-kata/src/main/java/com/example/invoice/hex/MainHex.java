package com.example.invoice.hex;

 import com.example.invoice.hex.adapters.out.InMemoryInvoiceRepository;
 import com.example.invoice.hex.app.InvoiceService;
 import com.example.invoice.hex.domain.Invoice;
 import com.example.invoice.hex.domain.LineItem;

 import java.math.BigDecimal;
 import java.util.List;

public class MainHex {

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
