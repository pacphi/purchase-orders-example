package io.pivotal.orders;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "orderId", "id", "itemCode", "itemDescription", "quantity", "unitPrice", "unitOfMeasure" })
public class LineItem implements Serializable{

    private static final long serialVersionUID = 1L;

    @NotNull
    private UUID orderId;
    private UUID id;
    @NotEmpty
    @Size(max = 10)
    private String itemCode;
    @Size(max = 250)
    private String itemDescription;
    @NotNull
    private Long quantity;
    @NotNull
    private Double unitPrice;
    @NotEmpty
    @Size(max = 3)
    private String unitOfMeasure;

    private LineItem() {}

    @JsonCreator
    public LineItem(@JsonProperty("orderId") UUID orderId,
        @JsonProperty("id") UUID id, @JsonProperty("itemCode") String itemCode, 
        @JsonProperty("itemDescription") String itemDescription, @JsonProperty("quantity") Long quantity, 
        @JsonProperty("unitPrice") Double unitPrice, @JsonProperty("unitOfMeasure") String unitOfMeasure) {
        this.id = id;
        this.itemCode = itemCode;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitOfMeasure = unitOfMeasure;
    }

    public static LineItem newInstance() {
        return new LineItem();
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public LineItem orderId(UUID orderId) {
        this.orderId = orderId;
        return this;
    }

    public UUID getId() {
        return this.id;
    }

    public LineItem id(UUID id) {
        this.id = id;
        return this;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public LineItem itemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemDescription() {
        return this.itemDescription;
    }

    public LineItem itemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
        return this;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public LineItem quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public LineItem unitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public String getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public LineItem unitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof LineItem)) {
            return false;
        }
        LineItem lineItem = (LineItem) o;
        return Objects.equals(orderId, lineItem.orderId) && Objects.equals(id, lineItem.id) && Objects.equals(itemCode, lineItem.itemCode) && Objects.equals(itemDescription, lineItem.itemDescription) && Objects.equals(quantity, lineItem.quantity) && Objects.equals(unitPrice, lineItem.unitPrice) && Objects.equals(unitOfMeasure, lineItem.unitOfMeasure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, id, itemCode, itemDescription, quantity, unitPrice, unitOfMeasure);
    }

}