package io.pivotal.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class PurchaseOrdersService {

    private OrdersRepository ordersRepo;
    private LineItemsRepository lineItemsRepo;

    @Autowired
    public PurchaseOrdersService(OrdersRepository ordersRepo, LineItemsRepository lineItemsRepo) {
        this.ordersRepo = ordersRepo;
        this.lineItemsRepo = lineItemsRepo;
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrder entity) {
        UUID newOrderId = ordersRepo.create(entity.getOrder());
        List<LineItem> lineItems = new ArrayList<>();
        entity.getLineItems().forEach(li -> {
            // TODO We could do a better job here.
            // E.g., create line items where condition is met, 
            // then capture and report on items that did not meet condition.
            Assert.isNull(li.getOrderId(), "When creating a new purchase order each line item's orderId should be null!");
            UUID newLineItemId = lineItemsRepo.create(li);
            lineItems.add(LineItem.from(li).orderId(newOrderId).id(newLineItemId));
        });
        return new PurchaseOrder(
            Order.from(entity.getOrder().id(newOrderId)), 
            lineItems);
    }

    @Transactional(readOnly = true)
    public PurchaseOrder findPurchaseOrderByOrderId(UUID orderId) {
        Order existingOrder = ordersRepo.findById(orderId);
        List<LineItem> existingLineItems = lineItemsRepo.findByOrderId(orderId);
        return new PurchaseOrder(existingOrder, existingLineItems);
    }

    @Transactional
    public void deletePurchaseOrderByOrderId(UUID orderId) {
        PurchaseOrder po = findPurchaseOrderByOrderId(orderId);
        if (po != null) {
            po.getLineItems().forEach(
                li -> lineItemsRepo.delete(li.getId())
            );
            ordersRepo.delete(orderId);
        }
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrder> findPurchaseOrderByOrderDateCreated(LocalDate dateCreated) {
        List<Order> existingOrders = ordersRepo.findByCreatedDate(dateCreated);
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        existingOrders.forEach(
            o -> 
                purchaseOrders.add(new PurchaseOrder(o, lineItemsRepo.findByOrderId(o.getId())))
        );
        return purchaseOrders;
    }
}