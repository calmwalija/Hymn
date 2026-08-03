# Hymn AI prompts

Prompts for drafting GitHub issues and pull requests against [calmwalija/Hymn](https://github.com/calmwalija/Hymn) in Cursor (or any AI assistant).

## Prompts

| File | When to use | Output |
|------|-------------|--------|
| [`create-issue.md`](create-issue.md) | New bug, feature, or chore before coding | Title + structured issue body ready for `gh issue create` |
| [`create-pull-request.md`](create-pull-request.md) | After you have a branch/diff | PR title + body with Summary / Changes / Test plan |

## How to use

1. Open the prompt file and copy **all** of its contents into chat.
2. Append your context under the prompt’s “User context” section.
3. Review the draft: fix invented paths, remove secrets, tighten acceptance criteria.
4. Create the GitHub item:
   - Issue: `gh issue create --assignee calmwalija …`
   - PR: `gh pr create --assignee calmwalija …` with `Closes #<issue>` in the body

## Related GitHub templates

| Path | Role |
|------|------|
| [`../ISSUE_TEMPLATE/`](../ISSUE_TEMPLATE/) | **Bug report** and **Feature request** forms |
| [`../pull_request_template.md`](../pull_request_template.md) | Default PR body skeleton |

## Conventions

- One issue / one PR concern.
- Area: `ui`, `data`, `both`, `docs / infra`.
- No secrets in issue/PR text.
- Acceptance criteria as checklists.
- PR titles: `fix:`, `feat:`, `docs:`, `chore:` (+ optional scope).
- Issues and PRs assigned to **`calmwalija`**.
- Footer **`Closes #N`** on PRs.
- Before push: `./gradlew spotlessApply`.
- Merge without waiting for CI; continue to the next issue.

## Branch naming

```
{issue_number}-{type}-{kebab-case-slug}
```

Example: `250-chore-github-templates-and-ci`.
