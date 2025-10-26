---
description: Update the documentation index after creating new documentation files
---

Update the documentation index (`.claude/session-init.md`) by discovering new markdown files in the project.

Follow these steps:

1. Run the discovery script: `./.claude/update-docs-index.sh`
2. Read the output to identify any new documentation files
3. Read `.claude/session-init.md` to see the current index
4. Update `.claude/session-init.md` with any new files found:
   - Add them to the appropriate category (Root, Backend, Mobile, or Frontend)
   - Organize alphabetically within each category
   - Mark critical files with ⭐ if needed
   - Update the total documentation count at the bottom
5. Confirm the update with a summary of what was added

If no new files are found, report that the index is already up to date.
