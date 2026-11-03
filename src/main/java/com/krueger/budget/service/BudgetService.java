package com.krueger.budget.service;

import com.krueger.budget.audit.AuditService;
import com.krueger.budget.domain.BudgetCommitment;
import com.krueger.budget.domain.FundingSource;
import com.krueger.budget.domain.ReserveRule;
import com.krueger.budget.domain.ReserveRuleSelector;
import com.krueger.budget.repository.BudgetCommitmentRepository;
import com.krueger.budget.validation.CommitmentValidator;
import com.krueger.budget.validation.InvalidCommitmentException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Facade over the budget flow. One method hides the moving parts: pick the rule,
 * compute the reserved amount, timestamp and persist. The AI tools (next step)
 * will call this service, never the repository directly.
 */
@Service
public class BudgetService {

    private final ReserveRuleSelector selector;
    private final BudgetCommitmentRepository repository;
    private final CommitmentValidator validator;
    private final AuditService audit;

    public BudgetService(ReserveRuleSelector selector,
                         BudgetCommitmentRepository repository,
                         CommitmentValidator validator,
                         AuditService audit) {
        this.selector = selector;
        this.repository = repository;
        this.validator = validator;
        this.audit = audit;
    }

    public BudgetCommitment register(BudgetCommitment commitment) {
        // Guardrail first: never save a commitment that breaks a business rule.
        try {
            validator.validate(commitment);
        } catch (InvalidCommitmentException e) {
            audit.record("SYSTEM", "VALIDATION_REJECTED", e.getMessage());
            throw e;
        }

        ReserveRule rule = selector.select(commitment.getFundingSource());
        commitment.setReservedAmount(rule.reservedAmountFor(commitment));
        commitment.setCreatedAt(LocalDateTime.now());
        BudgetCommitment saved = repository.save(commitment);

        audit.record("SYSTEM", "COMMITMENT_REGISTERED",
                "id=" + saved.getId()
                        + ", source=" + saved.getFundingSource()
                        + ", requested=" + saved.getRequestedAmount()
                        + ", reserved=" + saved.getReservedAmount());

        return saved;
    }

    public List<BudgetCommitment> listAll() {
        return repository.findAll();
    }

    public List<BudgetCommitment> listByFundingSource(FundingSource source) {
        return repository.findByFundingSource(source);
    }
}
