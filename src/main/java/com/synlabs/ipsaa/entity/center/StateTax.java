package com.synlabs.ipsaa.entity.center;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class StateTax extends BaseEntity {

    @ManyToOne
    private State state;

    @Column(nullable = false)
    private BigDecimal min;

    @Column(nullable = false)
    private BigDecimal max;

    private BigDecimal professionalTax;

    public State getStates() { return state; }

    public void setStates(State states) { this.state = states; }

    public BigDecimal getMin() { return min; }

    public void setMin(BigDecimal min) { this.min = min; }

    public BigDecimal getMax() { return max; }

    public void setMax(BigDecimal max) { this.max = max; }

    public BigDecimal getProfessionalTax() { return professionalTax; }

    public void setProfessionalTax(BigDecimal professionalTax) { this.professionalTax = professionalTax; }
}
