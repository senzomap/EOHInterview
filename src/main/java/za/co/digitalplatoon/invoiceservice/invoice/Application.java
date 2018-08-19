package za.co.digitalplatoon.invoiceservice.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner demo(InvoiceRepository repository, LineItemRepository itemRepository) {
        return (args) -> {
            LineItem ram = new LineItem();
            ram.setDescription("8 GB RAM DDR3");
            ram.setQuantity(2L);
            ram.setUnitPrice(BigDecimal.valueOf(550.00));
            LineItem cpu = new LineItem();
            cpu.setDescription("i7 Intel Processor 3.5GHz");
            cpu.setQuantity(1L);
            cpu.setUnitPrice(BigDecimal.valueOf(3995.99));
            List<LineItem> myItems = new ArrayList<>();
            myItems.add(cpu);
            myItems.add(ram);
            itemRepository.save(cpu);
            itemRepository.save(ram);
            Invoice invoice = new Invoice();
            invoice.setClient("client");
            invoice.setInvoiceDate(new Date());
            invoice.setVatRate(45L);
            invoice.setLineItemList(myItems);
            repository.save(invoice);
        };
    }

}
