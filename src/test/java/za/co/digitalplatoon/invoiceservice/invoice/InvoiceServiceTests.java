/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.digitalplatoon.invoiceservice.invoice;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.junit.Before;

import org.junit.Test;

public class InvoiceServiceTests {
    LineItem lineItem,lineItem2;
    Invoice invoice;
    @Before
    public void setUp() {
        lineItem = new LineItem();
        lineItem.setDescription("mac os");
        lineItem.setQuantity(1L);
        lineItem.setUnitPrice(BigDecimal.valueOf(500.00));
        lineItem2 = new LineItem();
        lineItem2.setDescription("windows os");
        lineItem2.setQuantity(2L);
        lineItem2.setUnitPrice(BigDecimal.valueOf(100.00));
        List<LineItem> items = new ArrayList<>();
        items.add(lineItem);
        items.add(lineItem2);
        invoice = new Invoice();
        invoice.setClient("senzo");
        invoice.setInvoiceDate(new Date());
        invoice.setLineItemList(items);
        invoice.setVatRate(14L);
    }
        
    @Test
    public void testLineItemTotal(){        
        assert(lineItem.getLineItemTotal().doubleValue()==500.00);
    }
    @Test
    public void testGetSubTotal(){        
        assert(invoice.getSubTotal().doubleValue()==700.00);
    }
    @Test
    public void testGetVat(){
        double vat = invoice.getVat().doubleValue();
        vat = vat*100;
        vat = Math.round(vat);
        vat = vat/100;
        assert(vat==98.00);
    }
    
    @Test
    public void testGetTotal(){
        assert(invoice.getTotal().doubleValue()==798.00);
    }
    
    @Test
    public void testPostMethod() throws ProtocolException{
        
            try {
                URL url = new URL("http://localhost:8080/invoices");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                String msg = "{\"client\":\"" + invoice.getClient() + "\", "
                        + "\"vatRate\":\""+invoice.getVatRate().intValue()+"\","
                        + "\"lineItemList\":[{\"description\":\"mac os\",\"quantity\":1}]}";
                try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
                    wr.write(msg);
                    wr.flush();
                }
                int responseCode = conn.getResponseCode();                
                assert(responseCode==200);
            }   catch (MalformedURLException ex) {
            Logger.getLogger(InvoiceServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InvoiceServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}