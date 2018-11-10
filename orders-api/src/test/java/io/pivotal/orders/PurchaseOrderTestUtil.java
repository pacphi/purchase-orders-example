package io.pivotal.orders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class PurchaseOrderTestUtil {

    // use when you want to look up matching PO by dateCreated
    static List<PurchaseOrder> vendPersistentPurchaseOrders(UUID orderId, LocalDateTime dateCreated) {
        Order order = 
            new Order(orderId, "tony.stark@starkenterprises.com", "nick.fury@shield.com",
                "AVE", "Stark Enterprises", "for Thanos", dateCreated, "REQUESTED");
        LineItem lineItem = 
            LineItem
                .newInstance()
                    .id(UUID.randomUUID())
                    .orderId(orderId)
                    .itemCode("BERTHA")
                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                    .quantity(1L)
                    .unitPrice(15000000.00)
                    .unitOfMeasure("PC");
        return Arrays.asList(new PurchaseOrder(order, Arrays.asList(lineItem)));
    }

    // use when you want to look up either a matching PO or lineItem by orderId or lineItemId respectively
    static PurchaseOrder vendPersistentPurchaseOrder(UUID orderId, UUID lineItemId) {
        Order order = 
            Order
                .newInstance()
                    .id(orderId)
                    .requestedBy("tony.stark@starkenterprises.com")
                    .orderedBy("nick.fury@shield.com")
                    .branch("AVE")
                    .supplier("Stark Enterprises")
                    .remarks("for Thanos")
                    .status("REQUESTED");
        LineItem lineItem = 
            LineItem
                .newInstance()
                    .id(lineItemId)
                    .orderId(orderId)
                    .itemCode("BERTHA")
                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                    .quantity(1L)
                    .unitPrice(15000000.00)
                    .unitOfMeasure("PC");
        return new PurchaseOrder(order, Arrays.asList(lineItem));
    }

    // use when you want to create a new PO
    static PurchaseOrder vendDetachedPurchaseOrder() {
        return new PurchaseOrder(vendDetachedOrder(), Arrays.asList(vendDetachedLineItem()));
    }

    // use when you want to create a new PO without lineItems
    static PurchaseOrder vendDetachedPurchaseOrderWithoutLineItems() {
        return new PurchaseOrder(vendDetachedOrder(), new ArrayList<LineItem>());
    }

    static Order vendDetachedOrder() {
        return
            Order
                .newInstance()
                    .requestedBy("tony.stark@starkenterprises.com")
                    .orderedBy("nick.fury@shield.com")
                    .branch("AVE")
                    .supplier("Stark Enterprises")
                    .remarks("for Thanos")
                    .status("REQUESTED");
    }

    static Order vendInvalidDetachedOrder() {
        return
            Order
                .newInstance()
                    .id(UUID.randomUUID())
                    .requestedBy("tony.stark@starkenterprises.com")
                    .orderedBy("nick.fury@shield.com")
                    .branch("AVE")
                    .supplier("Stark Enterprises")
                    .remarks("for Thanos")
                    .status("REQUESTED");
    }

    static LineItem vendDetachedLineItem() {
        return
            LineItem
                .newInstance()
                    .itemCode("BERTHA")
                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                    .quantity(1L)
                    .unitPrice(15000000.00)
                    .unitOfMeasure("PC");
    }

    static LineItem vendInvalidDetachedLineItem() {
        return
            LineItem
                .newInstance()
                    .orderId(UUID.randomUUID())
                    .id(UUID.randomUUID())
                    .itemCode("BERTHA")
                    .itemDescription("Near Earth orbit Hulk buster suit of armor")
                    .quantity(1L)
                    .unitPrice(15000000.00)
                    .unitOfMeasure("PC");
    }
}