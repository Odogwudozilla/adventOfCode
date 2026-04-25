# AOC Automation Handoff

> **STATUS: IMPLEMENTED** - See `docs/AOC_AUTOMATION_PLAN.md` for the living tracking document
> and the `odogwudozilla.automation` package for all implemented classes.
>
> Run via: `./gradlew autoSolve`

---

## 1. Goal and Scope

### Objective
Design and implement a fully automated Advent of Code workflow integrated into the existing Java project.

The system should:
- Trigger via a simple command (e.g., `random`)
- Select a random unsolved puzzle (year/day)
- Retrieve puzzle description + input
- Solve Part 1 and Part 2
- Submit answers with proper rate limiting
- Verify correctness
- Update documentation
- Commit and push changes

This replaces the current manual browser-driven workflow with a Playwright-powered automation pipeline.

### End-to-End Flow (Target State)

```
User → "random"
  → Select puzzle
  → Fetch puzzle + input
  → Generate/update solution code
  → Solve Part 1
  → Wait ≥60s
  → Submit Part 1
  → Verify
  → Fetch Part 2
  → Solve Part 2
  → Wait ≥60s
  → Submit Part 2
  → Verify
  → Update docs
  → Refactor
  → Commit + push
```

---

## 2. Existing Workflow Context

### Current Manual Flow
- User opens Advent of Code in browser
- Navigates to puzzle
- Copies puzzle text + input
- Pastes into project
- Prompts agent to generate solution
- Manually submits answer
- Checks correctness
- Repeats for Part 2

### Pain Points
- Manual navigation and copying
- Error-prone submission loop
- No enforced rate limiting
- No consistent logging
- Fragmented workflow

### Automation Scope

| Step | Automate |
|------|---------|
| Puzzle selection | Yes |
| Page navigation | Yes |
| Input retrieval | Yes |
| Code execution | Yes |
| Submission | Yes |
| Verification | Yes |
| Docs + Git | Yes |

---

## 3. Recommended Architecture

### Core Stack
- Language: Java
- Automation: Playwright (Java bindings)
- Execution: CLI-triggered orchestration

### Components

#### PuzzleSelector
- Determines unsolved puzzles
- Picks random (year/day)
- Avoids duplicates

#### BrowserSessionManager
- Initializes Playwright
- Loads session cookie
- Creates authenticated context

#### PuzzleScraper
- Extracts problem description

#### InputFetcher
- Retrieves user-specific input
- Stores locally

#### SolverRunner
- Executes Java solution
- Captures output

#### AnswerSubmitter
- Submits answer via browser automation

#### AcceptanceVerifier
- Parses correctness response

#### RateLimiter
- Enforces ≥60s delay

#### DocumentationUpdater
- Updates README/logs

#### GitAutomationService
- Stage, commit, push

---

## 4. Authentication Strategy

### Recommended: Session Cookie
- Extract `session` cookie from browser
- Store securely (env var or config file)
- Inject into Playwright context

### Avoid
- Automating GitHub/Gmail login (fragile, CAPTCHA-prone)

### Security Guidance
- Never log credentials
- Never commit session token
- Use environment variables or encrypted storage

---

## 5. Workflow Details

### Execution Sequence

1. User triggers `random`
2. Select unsolved puzzle
3. Launch Playwright with session cookie
4. Retrieve Part 1 description + input
5. Write to project files
6. Solve Part 1
7. Wait ≥60 seconds
8. Submit answer
9. Verify result
10. Retrieve Part 2
11. Solve Part 2
12. Wait ≥60 seconds
13. Submit Part 2
14. Verify result
15. Update documentation
16. Refactor code safely
17. Stage, commit, push

---

## 6. Rate Limiting and Safety Rules

- Minimum 60 seconds between submissions
- Do not spam retries
- Log all attempts
- Backoff after failures
- Avoid excessive scraping

---

## 7. Design Choices and Trade-offs (DECIDED)

### Playwright vs HTTP
- **Decision: Playwright** - reliable, handles JS pages, recommended in handoff. Implemented in `BrowserSessionManager`.

### Session Cookie vs OAuth
- **Decision: Session Cookie** - stable; loaded from `AOC_SESSION` env var with `.aoc-session` file fallback. Implemented in `BrowserSessionManager`.

### Auto vs Assisted Solving
- **Decision: Assisted** - orchestrator pauses at Stage 5 for user/Copilot to implement solve methods. Safe for novel puzzles. Implemented in `AutomationOrchestrator`.

### Auto Push vs Manual Review
- **Decision: Commit only (no push)** - `GitAutomationService` stages and commits; push is left to the user.

---

## 8. Implementation Guidance for Copilot

- Inspect repo structure first
- Produce implementation plan before coding
- Keep modules isolated and testable
- Avoid hardcoding values
- Follow existing conventions

---

## 9. Risks and Edge Cases

- Incorrect submissions
- Part 2 not appearing
- Expired session cookie
- CAPTCHA interruptions
- Network failures
- Git conflicts
- Already solved puzzle selected
- Rate limiting or temporary bans

---

## 10. Final Deliverable

- CLI-triggered automation
- Modular Java services
- Playwright integration
- Secure auth handling
- Full AoC lifecycle automation

---

## 11. Reference Context

This design is based on the original automation specification provided in the prompt file.

