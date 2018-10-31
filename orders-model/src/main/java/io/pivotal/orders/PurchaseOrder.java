package io.pivotal.orders;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "order", "lineItems"})
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Order order;
    private List<LineItem> lineItems;

    @JsonCreator
    public PurchaseOrder(@JsonProperty("order") Order order, @JsonProperty("lineItems") List<LineItem> lineItems) {
        this.order = order;
        this.lineItems = lineItems;
    }

    public Order getOrder() {
        return this.order;
    }

    public List<LineItem> getLineItems() {
        return this.lineItems;
    }

    public PurchaseOrder order(Order order) {
        this.order = order;
        return this;
    }

    public PurchaseOrder lineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PurchaseOrder)) {
            return false;
        }
        PurchaseOrder purchaseOrder = (PurchaseOrder) o;
        return Objects.equals(order, purchaseOrder.order) && Objects.equals(lineItems, purchaseOrder.lineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, lineItems);
    }

}