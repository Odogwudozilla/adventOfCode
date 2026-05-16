---
mode: agent
description: Updates AGENTS.md with any missing or changed project information. Run this whenever the project evolves to keep the AI context current.
---

Read the existing `AGENTS.md` in full before doing anything else. Understand its structure,
sections, and content thoroughly - this is your baseline, assumed correct unless proven
otherwise. Analyse the codebase to identify ONLY what has changed or is missing compared
to the existing `AGENTS.md`. Source existing AI conventions from
`**/{.github/copilot-instructions.md,AGENTS.md,CLAUDE.md,.cursorrules,README.md}`.
Apply minimal, surgical updates: do not rewrite accurate sections; preserve existing
structure and ordering; add content to the most logical existing section; write concise
actionable instructions referencing actual files and patterns from this codebase.
After updating, summarise what was added, modified, or removed.

