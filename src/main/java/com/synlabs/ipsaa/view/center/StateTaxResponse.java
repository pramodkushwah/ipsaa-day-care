package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.StateTax;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

public class StateTaxResponse implements Response {

    private Long id;
    private Long stateId;
    private BigDecimal min;
    private BigDecimal max;

    public StateTaxResponse() {
    }

    public StateTaxResponse(StateTax tax) {
        this.id= mask(tax.getId());
        this.stateId= mask(tax.getStates().getId());
        this.min=tax.getMin();
        this.max=tax.getMax();
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getStateId() { return stateId; }

    public void setStateId(Long stateId) { this.stateId = stateId; }

    public BigDecimal getMin() { return min; }

    public void setMin(BigDecimal min) { this.min = min; }

    public BigDecimal getMax() { return max; }

    public void setMax(BigDecimal max) { this.max = max; }
}
