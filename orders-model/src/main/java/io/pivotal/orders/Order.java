package io.pivotal.orders;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "requestedBy", "orderedBy", "branch", "supplier", "remarks", "dateCreated", "status" })
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
    private LocalDateTime dateCreated;
    @NotEmpty
    @Size(max = 12)
    private String status;

    public Order() {}

    @JsonCreator
    public Order(@JsonProperty("id") UUID id, @JsonProperty("requestedBy") String requestedBy, 
        @JsonProperty("orderedBy") String orderedBy, @JsonProperty("branch") String branch, 
        @JsonProperty("supplier") String supplier, @JsonProperty("remarks") String remarks, 
        @JsonProperty("dateCreated") LocalDateTime dateCreated, @JsonProperty("status") String status) {
        this.id = id;
        this.requestedBy = requestedBy;
        this.orderedBy = orderedBy;
        this.branch = branch;
        this.supplier = supplier;
        this.remarks = remarks;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public static Order newInstance() {
        return new Order();
    }

    public static Order from(Order o) {
        return Order
            .newInstance()
                .id(o.getId())
                .requestedBy(o.getRequestedBy())
                .orderedBy(o.getOrderedBy())
                .branch(o.getBranch())
                .supplier(o.getSupplier())
                .remarks(o.getRemarks())
                .status(o.getStatus());
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

    public String getStatus() {
        return this.status;
    }

    public Order status(String status) {
        this.status = status;
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
        return Objects.equals(id, order.id) && Objects.equals(requestedBy, order.requestedBy) && Objects.equals(orderedBy, order.orderedBy) && Objects.equals(branch, order.branch) && Objects.equals(supplier, order.supplier) && Objects.equals(remarks, order.remarks) && Objects.equals(dateCreated, order.dateCreated) && Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestedBy, orderedBy, branch, supplier, remarks, dateCreated, status);
    }


}