# AOC Automation Plan - Living Tracking Document

**Created:** 2026-04-25
**Status:** IN PROGRESS

---

## Overview

Full end-to-end automation pipeline for Advent of Code puzzle solving.
Triggered via `./gradlew autoSolve`. Covers: puzzle selection, browser-based
data retrieval, solution skeleton generation, assisted solving, answer
submission with rate limiting, result verification, documentation update,
and git commit.

---

## Architecture

```
./gradlew autoSolve
       |
AutomationOrchestrator (main entry point)
       |
       +-- PuzzleRandomizer          (existing - select unsolved puzzle)
       +-- BrowserSessionManager     (Playwright init + session cookie)
       +-- PuzzleScraper             (fetch description + title from AoC)
       +-- InputFetcher              (fetch puzzle input, write to resources)
       +-- SolutionSkeletonGenerator (create Java class stub + empty txt files)
       |        [PAUSE - user/Copilot implements solvePartOne / solvePartTwo]
       +-- SolverRunner              (Gradle build + run, capture stdout answers)
       +-- RateLimiter               (enforce >= 60 s between submissions)
       +-- AnswerSubmitter           (Playwright form POST)
       +-- AcceptanceVerifier        (parse response page for correct/wrong/too-soon)
       +-- DocumentationUpdater      (update solutions_database.json + READMEs)
       +-- GitAutomationService      (git add . && git commit)
```

---

## New Files

| File | Package | Status |
|------|---------|--------|
| `docs/AOC_AUTOMATION_PLAN.md` | - | DONE |
| `src/.../automation/AutomationConfig.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/PuzzleInfo.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/SubmissionResult.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/RateLimiter.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/BrowserSessionManager.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/PuzzleScraper.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/InputFetcher.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/SolverRunner.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/AnswerSubmitter.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/AcceptanceVerifier.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/SolutionSkeletonGenerator.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/DocumentationUpdater.java` | `odogwudozilla.automation` | DONE |
| `src/.../automation/GitAutomationService.java` | `odogwudozilla.automation` | DONE |
| `src/.../AutomationOrchestrator.java` | `odogwudozilla` | DONE |

---

## Modified Files

| File | Change | Status |
|------|--------|--------|
| `build.gradle.kts` | Add Playwright dep + `autoSolve` / `installPlaywright` tasks | DONE |
| `.github/copilot-instructions.md` | Document automated workflow alongside manual | DONE |
| `AGENTS.md` | List new automation package and classes | DONE |
| `aoc_automation_handoff.md` | Mark design decisions as implemented | DONE |
| `.gitignore` | Add `.aoc-session` exclusion | DONE |

---

## Implementation Phases

### Phase 1 - Infrastructure (build + config + data classes)
- [x] Add Playwright dependency to `build.gradle.kts`
- [x] Add `autoSolve` and `installPlaywright` Gradle tasks
- [x] `AutomationConfig.java` - all constants
- [x] `PuzzleInfo.java` - data holder
- [x] `SubmissionResult.java` - enum

### Phase 2 - Browser & Network Layer
- [x] `RateLimiter.java`
- [x] `BrowserSessionManager.java`
- [x] `PuzzleScraper.java`
- [x] `InputFetcher.java`
- [x] `AnswerSubmitter.java`
- [x] `AcceptanceVerifier.java`

### Phase 3 - Solve & Generate Layer
- [x] `SolutionSkeletonGenerator.java`
- [x] `SolverRunner.java`

### Phase 4 - Documentation & Git Layer
- [x] `DocumentationUpdater.java`
- [x] `GitAutomationService.java`

### Phase 5 - Orchestration
- [x] `AutomationOrchestrator.java`

### Phase 6 - Instruction Updates
- [x] Update `.github/copilot-instructions.md`
- [x] Update `AGENTS.md`
- [x] Update `aoc_automation_handoff.md`

---

## Key Design Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Browser automation library | Playwright Java | Reliable, handles JS, recommended in handoff |
| Session auth | Env var `AOC_SESSION` with `.aoc-session` file fallback | Secure, flexible |
| Solving mode | Assisted (pause for human/Copilot implementation) | Safer for novel puzzles |
| Rate limiting | Hard 60 s minimum; configurable `MAX_RETRY_ATTEMPTS = 3` | Compliant with AoC ToS |
| JSON updates | Jackson `ObjectMapper` with `JsonNode` | Already a project dependency |
| Answer capture | Parse stdout lines matching `"Part 1:"` / `"Part 2:"` prefix | Consistent with skeleton output |
| Retry strategy | Max 3 attempts, 60 s wait between each | Safe and configurable |

---

## Open Questions - RESOLVED

| Question | Decision |
|----------|----------|
| `--submit-only` flag for re-submitting | DONE - implemented as `--submit YEAR DAY` mode |
| Part 2 scraping: automatic or always attempted? | Automatic - triggered when Part 1 is verified correct |
| Git push: auto-push or leave to user? | Leave to user - pipeline commits only, no push |

---

## Chat Usage (Copilot-driven from chat)

When asked to run the automation from the chat, Copilot uses the terminal tool in two steps:

### Step 1 - Setup (fetches puzzle + generates skeleton)
```bash
./gradlew autoSolve --args="--setup"
```
Copilot then reads the generated description file and implements the solve methods.

### Step 2 - Submit (builds, runs, submits)
```bash
./gradlew autoSolve --args="--auto"
```
Or, for a specific puzzle already set up:
```bash
./gradlew autoSolve --args="--submit YEAR DAY"
```

### Full pipeline (user-interactive, CLI only)
```bash
./gradlew autoSolve
```
Pauses at Stage 5 for the user to type `continue` after implementing solve methods.

---

## Risks

| Risk | Mitigation |
|------|-----------|
| Expired session cookie | Clear error message; instructions to refresh `.aoc-session` |
| AoC HTML structure changes | Playwright selectors documented; easy to update in `PuzzleScraper` |
| Wrong answer causes temp ban | Max retry cap (3); respect `TOO_SOON` signal |
| Skeleton solve methods left as stubs | Orchestrator validates non-empty output before submitting |
| Playwright browsers not installed | `installPlaywright` Gradle task provided |

---

## Prerequisites for Users

1. Set env var: `AOC_SESSION=<your session cookie value>`
   - Or create `.aoc-session` file in project root (gitignored)
2. Run once: `./gradlew installPlaywright` to download browser binaries
3. Ensure Java 11+ and Gradle wrapper are available

---

## Session Cookie - How to obtain

1. Log in to adventofcode.com in your browser
2. Open Developer Tools (F12) > Application > Cookies
3. Copy the value of the `session` cookie
4. Set: `$env:AOC_SESSION="<value>"` (PowerShell) or add to `.aoc-session`

---

*Update this file as each phase is completed. Change TODO -> DONE in the tables above.*




