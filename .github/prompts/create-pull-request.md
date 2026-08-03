# Prompt: Draft a Hymn pull request

You generate **one PR title** and **description** from the **actual diff** for [calmwalija/Hymn](https://github.com/calmwalija/Hymn).

## Repository context

- Kotlin Android app: Compose UI, Room, Hilt, DataStore, Firebase
- **Never** commit or describe secrets (`local.properties`, signing keys, raw `google-services.json`)

## Workflow (required)

1. Run in parallel from repo root:
   - `git status`
   - `git diff` (staged + unstaged)
   - `git log -5 --oneline`
2. If ahead of `main`: `git diff main...HEAD` and `git log main..HEAD --oneline`
3. Read every changed file that matters; group by **ui**, **data**, or **Root / docs / CI**.
4. **Never** invent changes not present in the diff.

## Subject line (PR title)

- Conventional prefix: `feat:`, `fix:`, `refactor:`, `chore:`, `docs:`, `build:`, `ci:`
- Imperative mood; one clear outcome; ≤ 72 chars ideal

## Body structure

```markdown
## Summary
<what changed and why>

## Changes
- **ui** (omit if N/A)
  - ...
- **data** (omit if N/A)
  - ...
- **Root / docs / CI** (if any)
  - ...

## Technical notes
- Migrations / SDK: ...
- Breaking changes: ...
- Follow-ups: ...

## Test plan
- [ ] `./gradlew spotlessApply`
- [ ] `./gradlew assembleDevDebug` (if applicable)
- [ ] Manual: ...
- [ ] Regression: ...

Closes #<issue_number>
```

**Required linking rules**

- Branch: `{issue_number}-{type}-{kebab-case-slug}`
- PR body must include `Closes #N`
- Assign to **`calmwalija`**
- Merge without waiting for CI; continue to the next issue

```bash
gh pr create --assignee calmwalija --title "…" --body "$(cat <<'EOF'
## Summary
…

Closes #N
EOF
)"
```

**Branch / ticket context:**

<Optional: issue number, branch name, or focus area>
