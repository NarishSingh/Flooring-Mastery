package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order {

    /*fields*/
    private LocalDate orderDate;
    private int orderNum;
    private String customerName;
    private State state;
    private Product product;
    private BigDecimal area;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;

    /*ctors*/
    //view to service
    public Order(LocalDate orderDate, String customerName, State state, Product productType, BigDecimal area) {
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.state = state;
        this.product = productType;
        this.area = area;
    }

    //service down
    public Order(LocalDate orderDate, int orderNum, String customerName, State state, Product product, BigDecimal area, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total) {
        this.orderDate = orderDate;
        this.orderNum = orderNum;
        this.customerName = customerName;
        this.state = state;
        this.product = product;
        this.area = area;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    /*read/write*/
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    /*read-only*/
    public LocalDate getOrderDate() {
        return orderDate;
    }

    /*test msg*/
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.orderDate);
        hash = 29 * hash + this.orderNum;
        hash = 29 * hash + Objects.hashCode(this.customerName);
        hash = 29 * hash + Objects.hashCode(this.state);
        hash = 29 * hash + Objects.hashCode(this.product);
        hash = 29 * hash + Objects.hashCode(this.area);
        hash = 29 * hash + Objects.hashCode(this.materialCost);
        hash = 29 * hash + Objects.hashCode(this.laborCost);
        hash = 29 * hash + Objects.hashCode(this.tax);
        hash = 29 * hash + Objects.hashCode(this.total);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.orderNum != other.orderNum) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order{" + "orderDate=" + orderDate + ", orderNum=" + orderNum + ", customerName=" + customerName + ", state=" + state + ", product=" + product + ", area=" + area + ", materialCost=" + materialCost + ", laborCost=" + laborCost + ", tax=" + tax + ", total=" + total + '}';
    }

}
