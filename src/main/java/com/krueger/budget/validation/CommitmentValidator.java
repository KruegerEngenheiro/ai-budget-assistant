package com.krueger.budget.validation;

import com.krueger.budget.domain.BudgetCommitment;
import org.springframework.stereotype.Component;

/**
 * The guardrail: the last check before money is reserved. If a commitment
 * breaks
 * a rule, this throws InvalidCommitmentException and nothing is saved.
 *
 * Carlos writes the rules here. See the TODOs below.
 */
@Component
public class CommitmentValidator {

   public void validate(BudgetCommitment commitment) {
        if (commitment.getRequestedAmount() == null
                || commitment.getRequestedAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidCommitmentException("Requested amount must be greater than zero.");
        }

        if (commitment.getDescription() == null || commitment.getDescription().isBlank()) {
            throw new InvalidCommitmentException("Description must not be empty.");
        }

        if (commitment.getFundingSource() == null) {
            throw new InvalidCommitmentException("Funding source is required (NATIONAL or INTERNATIONAL).");
        }
    }
}
