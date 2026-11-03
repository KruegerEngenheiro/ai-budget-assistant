package com.krueger.budget.domain;

import java.math.BigDecimal;

/**
 * Strategy: one reserve rule per funding source.
 * Each implementation says which source it handles and how much to reserve.
 */
public interface ReserveRule {

    FundingSource appliesTo();

    BigDecimal reservedAmountFor(BudgetCommitment commitment);
}
