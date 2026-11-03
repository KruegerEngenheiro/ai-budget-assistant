package com.krueger.budget.assistant;

import com.krueger.budget.audit.AuditService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * The AI brain. It wraps a ChatClient, gives it a system prompt with the trust
 * rules, and exposes the two tools the model is allowed to call. The model reads
 * the user's message, decides which tool fits, extracts the arguments and calls
 * our real code. We never let the model invent data.
 */
@Service
public class BudgetAssistant {

    private static final String SYSTEM_PROMPT = """
            You are a budget execution assistant for a public humanitarian operation.
            You help register and query budget commitments (in Brazilian public finance, "empenhos")
            by calling the tools available to you.

            Rules you must always follow:
            - Use the tools to register or read data. Never invent amounts, records or numbers.
            - The funding source is either NATIONAL or INTERNATIONAL. Infer it from the user's words.
            - Whenever your answer contains figures, end with a short reminder to confirm against the
              official budget system before acting on it.
            - If the request is unclear or outside budget commitments, say so plainly instead of guessing.
            - Reply in the same language the user used.
            """;

    private final ChatClient chatClient;
    private final RegisterCommitmentTool registerCommitmentTool;
    private final ListCommitmentsTool listCommitmentsTool;
    private final AuditService audit;

    public BudgetAssistant(ChatClient.Builder builder,
                           RegisterCommitmentTool registerCommitmentTool,
                           ListCommitmentsTool listCommitmentsTool,
                           AuditService audit) {
        this.registerCommitmentTool = registerCommitmentTool;
        this.listCommitmentsTool = listCommitmentsTool;
        this.audit = audit;
        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultTools(registerCommitmentTool, listCommitmentsTool)
                .build();
    }

    public String ask(String message) {
        try {
            String answer = chatClient.prompt()
                    .user(message)
                    .call()
                    .content();
            audit.record("ASSISTANT", "ASSISTANT_REQUEST", "Q: " + message + " | A: " + answer);
            return answer;
        } catch (RuntimeException e) {
            audit.record("ASSISTANT", "ASSISTANT_ERROR", "Q: " + message + " | error: " + e.getMessage());
            throw e;
        }
    }
}
