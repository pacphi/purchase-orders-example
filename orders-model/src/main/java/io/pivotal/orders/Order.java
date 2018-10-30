package io.pivotal.orders;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "requestedBy", "orderedBy", "branch", "supplier", "remarks", "dateCreated", "status", "items" })
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    @Email
    @NotEmpty
    @Size(max = 75)
    private String requestedBy;
    @Email
    @NotEmpty
    @Size(max = 75)
    private String orderedBy;
    @NotEmpty
    @Size(max = 5)
    private String branch;
    @NotEmpty
    @Size(max = 50)
    private String supplier;
    @Size(max = 1000)
    private String remarks;
    @NotNull
    private LocalDateTime dateCreated;
    @NotEmpty
    @Size(max = 12)
    private String status;
    @Size(max = 50)
    private List<LineItem> items;

    public Order() {}

    @JsonCreator
    public Order(@JsonProperty("id") UUID id, @JsonProperty("requestedBy") String requestedBy, 
        @JsonProperty("orderedBy") String orderedBy, @JsonProperty("branch") String branch, 
        @JsonProperty("supplier") String supplier, @JsonProperty("remarks") String remarks, 
        @JsonProperty("dateCreated") LocalDateTime dateCreated, @JsonProperty("status") String status, 
        @JsonProperty("items") List<LineItem> items) {
        this.id = id;
        this.requestedBy = requestedBy;
        this.orderedBy = orderedBy;
        this.branch = branch;
        this.supplier = supplier;
        this.remarks = remarks;
        this.dateCreated = dateCreated;
        this.status = status;
        this.items = items;
    }

    public static Order newInstance() {
        return new Order();
    }

    public UUID getId() {
        return this.id;
    }

    public Order id(UUID id) {
        this.id = id;
        return this;
    }

    public String getRequestedBy() {
        return this.requestedBy;
    }

    public Order requestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
        return this;
    }

    public String getOrderedBy() {
        return this.orderedBy;
    }

    public Order orderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
        return this;
    }

    public String getBranch() {
        return this.branch;
    }

    public Order branch(String branch) {
        this.branch = branch;
        return this;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public Order supplier(String supplier) {
        this.supplier = supplier;
        return this;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Order remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    public Order dateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public Order status(String status) {
        this.status = status;
        return this;
    }

    public List<LineItem> getItems() {
        return this.items;
    }

    public Order items(List<LineItem> items) {
        this.items = items;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(requestedBy, order.requestedBy) && Objects.equals(orderedBy, order.orderedBy) && Objects.equals(branch, order.branch) && Objects.equals(supplier, order.supplier) && Objects.equals(remarks, order.remarks) && Objects.equals(dateCreated, order.dateCreated) && Objects.equals(status, order.status) && Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestedBy, orderedBy, branch, supplier, remarks, dateCreated, status, items);
    }


}