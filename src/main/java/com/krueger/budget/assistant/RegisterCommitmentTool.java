package com.krueger.budget.assistant;

import com.krueger.budget.domain.BudgetCommitment;
import com.krueger.budget.domain.FundingSource;
import com.krueger.budget.service.BudgetService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegisterCommitmentTool {

    private final BudgetService budgetService;

    public RegisterCommitmentTool(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @Tool(
        name = "register-commitment",
        description = "Registers a new budget commitment by providing its description, requested amount, funding source, expense type, and requesting unit."
    )
    public BudgetCommitment register(
            @ToolParam(description = "Short description of the expense") String description,
            @ToolParam(description = "Amount requested, in BRL") BigDecimal requestedAmount,
            @ToolParam(description = "Funding source: NATIONAL or INTERNATIONAL") FundingSource fundingSource,
            @ToolParam(description = "Type of expense, e.g. supplies, per diem") String expenseType,
            @ToolParam(description = "Unit that requested it") String unit) {

        BudgetCommitment commitment = new BudgetCommitment();

        commitment.setDescription(description);
        commitment.setRequestedAmount(requestedAmount);
        commitment.setFundingSource(fundingSource);
        commitment.setExpenseType(expenseType);
        commitment.setUnit(unit);

        return budgetService.register(commitment);
    }
}
