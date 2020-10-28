package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    /*fields*/
    private String productType;
    private BigDecimal costPerSqFt;
    private BigDecimal laborCostPerSqFt;

    /*ctor*/
    public Product(String productType, BigDecimal costPerSqFt, BigDecimal laborCostPerSqFt) {
        this.productType = productType;
        this.costPerSqFt = costPerSqFt;
        this.laborCostPerSqFt = laborCostPerSqFt;
    }

    /*read/write*/
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPerSqFt() {
        return costPerSqFt;
    }

    public void setCostPerSqFt(BigDecimal costPerSqFt) {
        this.costPerSqFt = costPerSqFt;
    }

    public BigDecimal getLaborCostPerSqFt() {
        return laborCostPerSqFt;
    }

    public void setLaborCostPerSqFt(BigDecimal laborCostPerSqFt) {
        this.laborCostPerSqFt = laborCostPerSqFt;
    }

    /*testing methods*/
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.productType);
        hash = 37 * hash + Objects.hashCode(this.costPerSqFt);
        hash = 37 * hash + Objects.hashCode(this.laborCostPerSqFt);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.costPerSqFt, other.costPerSqFt)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSqFt, other.laborCostPerSqFt)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product{" + "productType=" + productType + ", costPerSqFt=" + costPerSqFt + ", laborCostPerSqFt=" + laborCostPerSqFt + '}';
    }

}
