package com.krueger.budget.validation;

import com.krueger.budget.domain.BudgetCommitment;
import com.krueger.budget.domain.FundingSource;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pure unit tests of the guardrail. They prove that a valid commitment passes
 * and that each broken rule is rejected before anything is saved.
 */
class CommitmentValidatorTest {

    private final CommitmentValidator validator = new CommitmentValidator();

    private BudgetCommitment validCommitment() {
        BudgetCommitment commitment = new BudgetCommitment();
        commitment.setDescription("Shelter kits");
        commitment.setRequestedAmount(new BigDecimal("100000"));
        commitment.setFundingSource(FundingSource.INTERNATIONAL);
        return commitment;
    }

    @Test
    void validCommitmentPasses() {
        assertDoesNotThrow(() -> validator.validate(validCommitment()));
    }

    @Test
    void negativeAmountIsRejected() {
        BudgetCommitment commitment = validCommitment();
        commitment.setRequestedAmount(new BigDecimal("-1"));
        assertThrows(InvalidCommitmentException.class, () -> validator.validate(commitment));
    }

    @Test
    void nullAmountIsRejected() {
        BudgetCommitment commitment = validCommitment();
        commitment.setRequestedAmount(null);
        assertThrows(InvalidCommitmentException.class, () -> validator.validate(commitment));
    }

    @Test
    void blankDescriptionIsRejected() {
        BudgetCommitment commitment = validCommitment();
        commitment.setDescription("   ");
        assertThrows(InvalidCommitmentException.class, () -> validator.validate(commitment));
    }

    @Test
    void nullFundingSourceIsRejected() {
        BudgetCommitment commitment = validCommitment();
        commitment.setFundingSource(null);
        assertThrows(InvalidCommitmentException.class, () -> validator.validate(commitment));
    }
}
