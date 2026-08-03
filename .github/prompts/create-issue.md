# Prompt: Draft a Hymn GitHub issue

You draft **one GitHub issue** (title + body) for [calmwalija/Hymn](https://github.com/calmwalija/Hymn). Output must be accurate, actionable, and ready to paste into GitHub.

## Repository context

- **App**: offline bilingual hymn reader (English + Chichewa)
- **Stack**: Kotlin, Jetpack Compose, Room, Hilt, DataStore, Firebase Analytics/Crashlytics
- **Secrets**: never include `local.properties`, signing keys, or `google-services.json` contents

## Workflow (required)

1. Read the user’s description and any linked context.
2. If code context is needed, search/read only relevant paths — do not guess file names.
3. Identify **which area** is affected: `ui`, `data`, `both`, or `docs / infra`.
4. **Never** invent behavior or root causes not supported by evidence.
5. If information is missing, list **specific questions** under “Open questions”.
6. When creating the issue with `gh`, always assign **`calmwalija`**:

```bash
gh issue create --title "…" --assignee calmwalija --body "$(cat <<'EOF'
…
EOF
)"
```

## Issue title

- Short, imperative, scannable (≤ 80 chars when possible)
- Prefix with area when helpful: `[UI]`, `[Data]`, `[Build]`, `[Chore]`

## Issue body structure

```markdown
## Summary
<2–4 sentences: what is wrong or what we want, and why it matters>

## Area
- [ ] ui
- [ ] data
- [ ] both
- [ ] docs / infra

## Current behavior
<What happens today, with steps if it's a bug>

## Expected behavior
<What should happen instead>

## Reproduction / context
1. ...
2. ...
- Environment: local / Play
- App version:

## Acceptance criteria
- [ ] ...
- [ ] ...

## Technical notes
- Likely files or modules (only if verified):
- Room / prefs implications:

## Open questions
- ...

## Out of scope
- ...
```

After the issue exists, create the branch as `{issue_number}-{type}-{kebab-case-slug}` and open PRs with `Closes #<issue_number>`.

**User context (fill in below when using this prompt):**

<Paste your notes, logs, or goal here>
