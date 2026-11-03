package com.krueger.budget.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * National funds are reserved in full: the reserved amount equals the requested amount.
 */
@Component
public class NationalFundsReserve implements ReserveRule {

    @Override
    public FundingSource appliesTo() {
        return FundingSource.NATIONAL;
    }

    @Override
    public BigDecimal reservedAmountFor(BudgetCommitment commitment) {
        return commitment.getRequestedAmount();
    }
}
