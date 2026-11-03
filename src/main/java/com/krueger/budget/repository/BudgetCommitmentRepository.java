package com.krueger.budget.repository;

import com.krueger.budget.domain.BudgetCommitment;
import com.krueger.budget.domain.FundingSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access for budget commitments. Spring Data builds the implementation at
 * runtime and manages it as a single bean (Singleton) in the application context.
 */
@Repository
public interface BudgetCommitmentRepository extends JpaRepository<BudgetCommitment, Long> {

    List<BudgetCommitment> findByFundingSource(FundingSource fundingSource);
}
