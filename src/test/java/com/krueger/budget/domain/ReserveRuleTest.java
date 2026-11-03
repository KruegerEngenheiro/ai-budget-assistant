package com.krueger.budget.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pure unit tests of the reserve rules. No Spring context, no database, no API
 * key: fast and light. They prove the money math, which is the part that must
 * never be wrong.
 */
class ReserveRuleTest {

    @Test
    void nationalFundsReserveTheFullAmount() {
        BudgetCommitment commitment = new BudgetCommitment();
        commitment.setRequestedAmount(new BigDecimal("100000"));
        commitment.setFundingSource(FundingSource.NATIONAL);

        BigDecimal reserved = new NationalFundsReserve().reservedAmountFor(commitment);

        assertEquals(0, reserved.compareTo(new BigDecimal("100000")));
    }

    @Test
    void internationalFundsAddFivePercentMargin() {
        BudgetCommitment commitment = new BudgetCommitment();
        commitment.setRequestedAmount(new BigDecimal("100000"));
        commitment.setFundingSource(FundingSource.INTERNATIONAL);

        BigDecimal reserved = new InternationalFundsReserve().reservedAmountFor(commitment);

        assertEquals(0, reserved.compareTo(new BigDecimal("105000.00")));
    }
}
