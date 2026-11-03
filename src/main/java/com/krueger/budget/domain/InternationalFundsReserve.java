package com.krueger.budget.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * International funds need a foreign exchange margin, so the reserved amount is
 * the requested amount plus 5%.
 */
@Component
public class InternationalFundsReserve implements ReserveRule {

    private static final BigDecimal EXCHANGE_MARGIN = new BigDecimal("1.05");

    @Override
    public FundingSource appliesTo() {
        return FundingSource.INTERNATIONAL;
    }

    @Override
    public BigDecimal reservedAmountFor(BudgetCommitment commitment) {
        return commitment.getRequestedAmount()
                .multiply(EXCHANGE_MARGIN)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
