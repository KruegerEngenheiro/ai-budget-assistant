package com.krueger.budget.domain;

/**
 * Where the money that backs an expense comes from.
 * NATIONAL: funds from the national (Union) budget.
 * INTERNATIONAL: funds from an international body, for example UN agencies.
 * Each source follows a different reserve rule (see the strategy package).
 */
public enum FundingSource {
    NATIONAL,
    INTERNATIONAL
}
