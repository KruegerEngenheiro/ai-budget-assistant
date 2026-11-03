package com.krueger.budget.domain;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Picks the right reserve rule at runtime. Spring injects every ReserveRule bean
 * as a list, and the selector returns the one that handles the given source.
 */
@Component
public class ReserveRuleSelector {

    private final List<ReserveRule> rules;

    public ReserveRuleSelector(List<ReserveRule> rules) {
        this.rules = rules;
    }

    public ReserveRule select(FundingSource source) {
        for (ReserveRule rule : rules) {
            if (rule.appliesTo() == source) {
                return rule;
            }
        }
        throw new IllegalArgumentException("No reserve rule for funding source: " + source);
    }
}
