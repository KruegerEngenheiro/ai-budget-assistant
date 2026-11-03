package com.krueger.budget.assistant;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The natural-language door into the system. Send a plain command and the
 * assistant decides which real function to run.
 */
@RestController
@RequestMapping("/assistant")
public class AssistantController {

    private final BudgetAssistant assistant;

    public AssistantController(BudgetAssistant assistant) {
        this.assistant = assistant;
    }

    @PostMapping
    public AssistantResponse ask(@RequestBody AssistantRequest request) {
        return new AssistantResponse(assistant.ask(request.message()));
    }

    public record AssistantRequest(String message) {
    }

    public record AssistantResponse(String answer) {
    }
}
