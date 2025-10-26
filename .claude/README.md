# Claude Code Configuration

This directory contains configuration files for Claude Code sessions.

## Files

### `session-init.md`
**Purpose:** Comprehensive index of all AI-generated documentation in the superpets project.

**Contains:**
- Complete list of 35+ documentation files across the monorepo
- Organized by category (Core, Auth, Database, Mobile, Frontend, etc.)
- Quick reference guide for finding relevant docs
- Documentation discovery commands

**Usage:** Claude Code reads this file at session start to become aware of all available documentation without explicit prompting.

---

### How the Documentation Discovery System Works

1. **At Session Start:**
   - Claude Code reads `CLAUDE.md` (main project instructions)
   - `CLAUDE.md` directs Claude to check `.claude/session-init.md`
   - Claude discovers all 35+ documentation files organized by category

2. **During Development:**
   - When working on authentication → Knows to check OAuth setup guides
   - When working on mobile → Knows to check mobile roadmap, status, design system
   - When working on backend → Knows to check Supabase migration, Sentry setup
   - When working on deployment → Knows to check Railway, GitHub Actions guides

3. **Maintenance:**
   - When adding new documentation → Update `session-init.md` index
   - When removing documentation → Update `session-init.md` index
   - Keep categories organized and mark critical files with ⭐

---

### Benefits

✅ **Autonomous Discovery:** Claude Code finds relevant docs without manual guidance
✅ **Reduced Context:** No need to paste documentation into prompts
✅ **Consistent Awareness:** Every session starts with full project knowledge
✅ **Scalable:** Easy to add new documentation as project grows
✅ **Cross-Session Memory:** Documentation persists across different Claude sessions

---

### Example Session Flow

**User:** "Add Apple OAuth to the mobile app"

**Claude (internally):**
1. Reads `CLAUDE.md` → Learns about project structure
2. Reads `.claude/session-init.md` → Discovers `APPLE_OAUTH_SETUP.md` exists
3. Reads `APPLE_OAUTH_SETUP.md` → Gets complete setup instructions
4. Proceeds with implementation using documented patterns

**Result:** Claude can implement features referencing existing docs without user having to point them out.

---

### Maintenance Commands

**Discover new markdown files:**
```bash
find . -name "*.md" \
  -not -path "*/node_modules/*" \
  -not -path "*/.gradle/*" \
  -not -path "*/build/*" \
  | sort
```

**Update the index:**
Edit `.claude/session-init.md` and add new files to the appropriate category.

