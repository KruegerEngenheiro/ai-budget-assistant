# ai-budget-assistant

> A Spring Boot + Spring AI assistant that runs **real budget-execution functions** from a natural-language command, with a validation guardrail and an audit trail. You ask in plain language, an LLM picks the right tool, and your own Java code does the work.

Built for the final "Spring AI" project challenge of the DIO Itaú "Java with AI" Bootcamp.

## The problem
In a public operation, people who manage the budget are not developers. They know the operation, not the system. The gap between "what they want to do" and "the function that does it" is where time and mistakes live. An assistant that understands plain language and calls the real functions closes that gap, but only if it never invents data and always leaves a trace of what it did.

This mirrors my own experience: on Operation Acolhida (Brazilian Army), I was the first to bring an AI assistant into the budget section. My job was not only to help build it, it was to decide whether its answers were correct and safe enough to go live. This project is the public, clean version of that idea.

## The solution
A REST API where a single natural-language endpoint drives real business logic through **LLM tool calling**:

1. You send a command, for example *"Reserve 200,000 BRL for food baskets, international funds, Boa Vista unit."*
2. The model reads the tools it is allowed to use, extracts the data from the sentence and calls one of them.
3. The tool runs **my code**: it validates the input, applies the reserve rule for the funding source, saves it, and logs it.
4. The assistant answers in the user's language and always reminds them to confirm against the official source.

## Architecture
```
com.krueger.budget
├─ domain        BudgetCommitment, FundingSource, ReserveRule (Strategy) + the two rules, ReserveRuleSelector
├─ repository    BudgetCommitmentRepository (Spring Data JPA)
├─ service       BudgetService (Facade: validate -> compute reserve -> persist -> audit)
├─ validation    CommitmentValidator (the guardrail) + InvalidCommitmentException
├─ audit         AuditLog, AuditService, AuditController (the trust trail)
├─ assistant     RegisterCommitmentTool, ListCommitmentsTool (the @Tool functions),
│                BudgetAssistant (ChatClient + system prompt), AssistantController
└─ controller    BudgetController (plain REST door), GlobalExceptionHandler
```
There are two doors into the same business logic: a plain REST door (`/commitments`) and a natural-language door (`/assistant`). Both go through `BudgetService`, so the rules and the audit apply no matter how the request arrives.

## What makes it mine: the trust layer
The challenge was to connect AI to a real function. I went one step further and added what an AI feature needs before it touches money:

- **Validation before saving.** A commitment with a non-positive amount, an empty description or no funding source is rejected with a clear message and a `400`, never saved.
- **An audit trail.** Every assistant interaction, every registration and every rejection is written to `AuditLog` and can be read at `GET /audit`. There is always an answer to "what did the assistant do, and when".
- **A guardrail in the prompt.** The assistant never invents numbers and always tells the user to confirm against the official budget system.

## Design patterns and AI concepts
- **Strategy:** one reserve rule per funding source (`NationalFundsReserve` full amount, `InternationalFundsReserve` amount plus a 5% exchange margin), chosen at runtime by `ReserveRuleSelector`.
- **Facade:** `BudgetService` exposes one `register` method that hides validation, rule selection, calculation, persistence and audit.
- **Singleton:** every service, tool, repository and controller is a Spring-managed bean.
- **Tool Calling (Spring AI):** methods annotated with `@Tool` become functions the LLM can call; the model decides when, our code decides how.

## Tech stack
Java 25, Spring Boot 4.0, Spring AI 2.0 (OpenAI, model `gpt-4o-mini`), Spring Web, Spring Data JPA, H2 (in-memory), Maven.

## How to run
1. Set your OpenAI key as an environment variable (never hard-code it):
   ```bash
   export OPENAI_API_KEY=sk-your-key   # Windows PowerShell: $env:OPENAI_API_KEY="sk-your-key"
   ```
   A local `.env` with `OPENAI_API_KEY=...` works too and is already git-ignored.
2. Start the app:
   ```bash
   mvn spring-boot:run
   ```
   It runs on http://localhost:8080. H2 console at `/h2-console` (JDBC URL `jdbc:h2:mem:budget`, user `sa`, no password).

## How to test the main flow
Register through the AI door:
```bash
curl -X POST http://localhost:8080/assistant -H "Content-Type: application/json" \
  -d '{"message":"Reserve 200000 BRL for food baskets, international funds, Boa Vista unit, supplies."}'
```
The assistant calls `register-commitment`, reserves 210000.00 (the 5% margin) and reminds you to check the source.

Query through the AI door:
```bash
curl -X POST http://localhost:8080/assistant -H "Content-Type: application/json" \
  -d '{"message":"Which international commitments were registered and what is the total reserved?"}'
```

See the trust trail:
```bash
curl http://localhost:8080/audit
```

Run the unit tests (reserve math and validation rules):
```bash
mvn test
```

## Endpoints
| Method | Route          | What it does                                             |
|--------|----------------|----------------------------------------------------------|
| POST   | `/assistant`   | Natural-language command, routed to a real function by AI |
| POST   | `/commitments` | Register a commitment directly (validated)               |
| GET    | `/commitments` | List all commitments                                     |
| GET    | `/audit`       | Read the audit trail                                     |

## What I learned
How tool calling turns a plain Java method into something an LLM can invoke, and how much the tool's description matters, because that text is what the model reads to decide. And that the interesting engineering is not the AI call itself, it is everything around it: validating before you trust, and logging so you can prove what happened.

## Future improvements
- Voice in and out (audio transcription and text-to-speech), the same flow with two more steps.
- Postgres instead of in-memory H2.
- More tools: totals by period, budget limits per unit.

---
**Carlos Eduardo Krueger** · [github.com/KruegerEngenheiro](https://github.com/KruegerEngenheiro) · [linkedin.com/in/carloseduardokrueger](https://linkedin.com/in/carloseduardokrueger) · Built during the DIO Itaú "Java with AI" Bootcamp.
