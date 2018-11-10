package io.pivotal.orders;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { PurchaseOrdersService.class })
public class PurchaseOrdersServiceTest {

    @MockBean
    private OrdersRepository ordersRepository;

    @MockBean
    private LineItemsRepository lineItemsRepository;

    @Autowired
    PurchaseOrdersService service;

    @Test
    public void canCreatePurchaseOrderWithValidInput() {
        PurchaseOrder detachedPO = PurchaseOrderTestUtil.vendDetachedPurchaseOrder();
        Order detachedOrder = detachedPO.getOrder();
        LineItem detachedLineItem = detachedPO.getLineItems().get(0);
        doReturn(UUID.randomUUID()).when(ordersRepository).create(detachedOrder);
        doReturn(UUID.randomUUID()).when(lineItemsRepository).create(detachedLineItem);

        PurchaseOrder persistentPO = service.createPurchaseOrder(detachedPO);
        Assertions.assertNotNull(persistentPO);
        Assertions.assertNotNull(persistentPO.getOrder());
        Assertions.assertFalse(CollectionUtils.isEmpty(persistentPO.getLineItems()));
        Assertions.assertNotNull(persistentPO.getOrder().getDateCreated());
    }

    @Test
    public void canFindPurchaseOrderByOrderId() {
        UUID orderId = UUID.randomUUID();
        PurchaseOrder persistentPO = PurchaseOrderTestUtil.vendPersistentPurchaseOrder(orderId, UUID.randomUUID());
        doReturn(persistentPO.getOrder()).when(ordersRepository).findById(orderId);
        doReturn(persistentPO.getLineItems()).when(lineItemsRepository).findByOrderId(orderId);
        PurchaseOrder foundPO = service.findPurchaseOrderByOrderId(orderId);
        Assertions.assertNotNull(foundPO);
        Assertions.assertNotNull(foundPO.getOrder());
        Assertions.assertFalse(CollectionUtils.isEmpty(foundPO.getLineItems()));
    }

    @Test
    public void canDeletePurchaseOrderByOrderId() {
        UUID orderId = UUID.randomUUID();
        UUID lineItemId = UUID.randomUUID();
        PurchaseOrder persistentPO = PurchaseOrderTestUtil.vendPersistentPurchaseOrder(orderId, lineItemId);
        doReturn(persistentPO.getOrder()).when(ordersRepository).findById(orderId);
        doReturn(persistentPO.getLineItems()).when(lineItemsRepository).findByOrderId(orderId);
        
        service.deletePurchaseOrderByOrderId(orderId);
        verify(lineItemsRepository, times(1)).delete(lineItemId);
        verify(ordersRepository, times(1)).delete(orderId);
    }

    @Test
    public void canFindPurchaseOrderByOrderDateCreated() {
        UUID orderId = UUID.randomUUID();
        LocalDateTime dateCreated = LocalDateTime.now();
        PurchaseOrder persistentPO = PurchaseOrderTestUtil.vendPersistentPurchaseOrders(orderId, dateCreated).get(0);
        doReturn(Arrays.asList(persistentPO.getOrder())).when(ordersRepository).findByCreatedDate(dateCreated.toLocalDate().plusDays(1));
        
        List<PurchaseOrder> foundPOs = service.findPurchaseOrderByOrderDateCreated(dateCreated.toLocalDate().plusDays(1));
        verify(ordersRepository, times(1)).findByCreatedDate(dateCreated.toLocalDate().plusDays(1));
        verify(lineItemsRepository, times(1)).findByOrderId(orderId);
        Assertions.assertTrue(foundPOs.size() == 1);
    }

}