package com.krueger.budget.controller;

import com.krueger.budget.domain.BudgetCommitment;
import com.krueger.budget.service.BudgetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Plain REST access to budget commitments, with no AI in the loop.
 * This is the "manual" door; the AI assistant (next step) is a second door that
 * reaches the same BudgetService through tool calling.
 */
@RestController
@RequestMapping("/commitments")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public BudgetCommitment register(@RequestBody BudgetCommitment commitment) {
        return budgetService.register(commitment);
    }

    @GetMapping
    public List<BudgetCommitment> list() {
        return budgetService.listAll();
    }
}
