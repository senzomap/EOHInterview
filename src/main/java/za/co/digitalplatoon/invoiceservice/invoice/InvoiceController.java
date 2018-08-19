/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.digitalplatoon.invoiceservice.invoice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 *
 * @author Senzo
 */
@RestController
public class InvoiceController {
    @Autowired
    InvoiceRepository invoiceRepository;
    
    @Autowired
    LineItemRepository itemRepository;
    
    @GET
    @RequestMapping("/invoices/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Invoice viewInvoice(@PathVariable("id") Long invoiceId){
        Optional<Invoice> findById = invoiceRepository.findById(invoiceId);
        Invoice invoice = findById.get();
        
       return invoice;
    }
    
    @GET
    @RequestMapping(value="/invoices", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Invoice> allInvoices(){
        List<Invoice> invoiceList = new ArrayList<>();
        Iterable<Invoice> findAll = invoiceRepository.findAll();
        Iterator<Invoice> iterator = findAll.iterator();
        while(iterator.hasNext()){
            invoiceList.add(iterator.next());
        }
        return invoiceList;
    }
    
    @POST
    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addInvoice(@RequestBody Invoice invoice){
        invoice.getLineItemList().forEach((item) -> {
            itemRepository.save(item);
        });
        invoiceRepository.save(invoice);
         
        return invoice.getClient()+"'s invoice saved successfully";                 
    }
}
