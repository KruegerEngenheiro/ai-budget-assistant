package com.krueger.budget.assistant;

import com.krueger.budget.domain.BudgetCommitment;
import com.krueger.budget.domain.FundingSource;
import com.krueger.budget.service.BudgetService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCommitmentsTool {

    private final BudgetService budgetService;

    public ListCommitmentsTool(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @Tool(
        name = "list-commitments-by-funding-source",
        description = "Retrieves budget commitments filtered by funding source (NATIONAL or INTERNATIONAL) when the user asks about registered commitments or reserved amounts."
    )
    public List<BudgetCommitment> listByFundingSource(
            @ToolParam(description = "Funding source to filter by: NATIONAL or INTERNATIONAL") FundingSource fundingSource) {

        return budgetService.listByFundingSource(fundingSource);
    }
}
