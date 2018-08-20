/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.digitalplatoon.invoiceservice.invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Senzo
 */
@Entity
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String client;
    private Long vatRate;
    private Date invoiceDate;
    @OneToMany
    private List<LineItem> lineItemList;

    public List<LineItem> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<LineItem> lineItemList) {
        this.lineItemList = lineItemList;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Long getVatRate() {
        return vatRate;
    }

    public void setVatRate(Long vatRate) {
        this.vatRate = vatRate;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.co.digitalplatoon.invoiceservice.entity.Invoice[ id=" + id + " ]";
    }
    
    /**
     * returns subtotal of the invoice: subtotal=sum of all line item totals
     * @return 
     */
    public BigDecimal getSubTotal(){
        List<LineItem> items = getLineItemList();
        double sum=0.0;
        for(LineItem item:items){
            BigDecimal lineItemTotal = item.getLineItemTotal();
            sum+=lineItemTotal.doubleValue();
        }
        BigDecimal subTotal = BigDecimal.valueOf(sum);
        return subTotal;
    }
    /**
     * returns VAT of an invoice: VAT=vatRate*subtotal
     * @return 
     */
    public BigDecimal getVat(){
        return getSubTotal().multiply(BigDecimal.valueOf((vatRate/100.00)));
    }
    
    /**
     * returns total amount for an invoice: total = VAT + subtotal
     * @return 
     */
    public BigDecimal getTotal(){
        return getSubTotal().add(getVat());
    }
}
