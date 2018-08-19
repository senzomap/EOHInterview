/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.digitalplatoon.invoiceservice.invoice;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author manager
 */
public interface InvoiceRepository extends CrudRepository<Invoice, Long>{
    List<Invoice> findByClient(String name);
}
