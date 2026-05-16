# Lesson File Template

Use this template when creating a new lesson file in `docs/memory/lessons/`.
File naming convention: `L-<YYYY-MM-DD>-<workflow>-<sequence>.md`

Example: `L-2026-05-17-puzzle-solving-001.md`

---

```yaml
---
id: L-[YYYY-MM-DD]-[workflow]-[sequence]
title: "[One sentence - the reusable insight]"
summary: "[Same as title, or slightly shorter. Used in the LESSONS.md index row.]"
workflow: puzzle-solving | shared
category: implementation | testing | review | algorithm | ai-behaviour | domain | workflow
tags:
  - [tag-1]
  - [tag-2]
confidence: provisional    # provisional | candidate-confirmed | confirmed | strong | stale | superseded
promotion_candidate: false
promotion_status: none     # none | pending-human-review | approved | rejected

applies_to:
  workflows:
    - puzzle-solving
  agents:
    - puzzle-analyser       # one or more of: puzzle-analyser | solution-implementer | solution-reviewer | puzzle-orchestrator | shared
  layers:
    - [algorithm | parsing | data-structure | testing | io]
  technologies:
    - [java | bfs | dynamic-programming | simulation | graph | etc.]
  triggers:
    - [keyword or puzzle pattern that suggests this lesson is relevant]

evidence:
  - puzzle: "<YEAR>-Day<N>"
    artifact: "<YEAR>-day<N>-analysis.md"
    section: "[Section heading]"
    claim: "[One sentence - what was observed that supports this lesson]"

code_evidence:
  - file: [path/to/ClassName.java]
    method: [methodName()]

counter_evidence: []

source_agent: puzzle-analyser | solution-implementer | solution-reviewer | system-seed
created: [YYYY-MM-DD]
created_by: odogwu
reviewed_by:
review_date:
cited_in: []
cited_count: 0
expiry:
superseded_by:

sensitivity: public-internal
redaction_checked: false
contains_pii: false
contains_secrets: false
---
```

## Detail

[Two to four paragraphs. Explain the insight with enough context for a future agent to
understand what was observed and why it matters. Be specific: name the algorithm pattern,
the failure mode, or the parsing constraint. Avoid vague generalisations. Write in
British English.]

## Recommendation

[One to three sentences. What should a future agent do - or avoid doing - based on this
lesson? Phrase as a concrete instruction.]

## Counter-evidence

[State "None to date." if the counter_evidence field above is empty.]

