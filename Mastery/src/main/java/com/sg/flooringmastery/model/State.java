package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class State {

    /*fields*/
    private String stateAbbreviation;
    private String stateName;
    private BigDecimal taxRate;

    /*ctor*/
    //for orders
    public State(String stateAbbreviation, BigDecimal taxRate) {
        this.stateAbbreviation = stateAbbreviation;
        this.taxRate = taxRate;
    }

    //for state dao
    public State(String stateAbbreviation, String stateName, BigDecimal taxRate) {
        this.stateAbbreviation = stateAbbreviation;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }

    /*read/write*/
    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    /*testing methods*/
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.stateAbbreviation);
        hash = 83 * hash + Objects.hashCode(this.stateName);
        hash = 83 * hash + Objects.hashCode(this.taxRate);
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
        final State other = (State) obj;
        if (!Objects.equals(this.stateAbbreviation, other.stateAbbreviation)) {
            return false;
        }
        if (!Objects.equals(this.stateName, other.stateName)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "State{" + "stateAbbreviation=" + stateAbbreviation + ", stateName=" + stateName + ", taxRate=" + taxRate + '}';
    }

}
