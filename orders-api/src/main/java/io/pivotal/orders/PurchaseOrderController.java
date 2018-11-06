package io.pivotal.orders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class PurchaseOrderController {

    private PurchaseOrdersService service;

    @Autowired
    public PurchaseOrderController(PurchaseOrdersService service) {
        this.service = service;
    }

    @GetMapping("/purchaseOrders/{id}")
    public ResponseEntity<PurchaseOrder> findPurchaseOrderByOrderId(@PathVariable("id") UUID orderId) {
        return Optional.of(service.findPurchaseOrderByOrderId(orderId))
                .map(existingPurchaseOrder -> ResponseEntity.ok(existingPurchaseOrder))
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/purchaseOrders")
    public ResponseEntity<List<PurchaseOrder>> findPurchaseOrderByOrderDateCreated(
        @RequestParam("dateCreated") @DateTimeFormat(iso = ISO.DATE) LocalDate dateCreated) {
        return Optional.of(service.findPurchaseOrderByOrderDateCreated(dateCreated))
                .map(existingPurchaseOrders -> ResponseEntity.ok(existingPurchaseOrders))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/purchaseOrders/{id}")
    public ResponseEntity<Void> deletePurchaseOrderByOrderId(@PathVariable("id") UUID orderId) {
        service.deletePurchaseOrderByOrderId(orderId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/purchaseOrders")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@Valid @RequestBody PurchaseOrder candidate) {
        PurchaseOrder newPurchaseOrder = service.createPurchaseOrder(candidate);
        UriComponentsBuilder ucb = UriComponentsBuilder.newInstance();
        return ResponseEntity.created(
            ucb.path("/purchaseOrders/{id}").buildAndExpand(newPurchaseOrder.getOrder().getId()).toUri()).build();
    }

}