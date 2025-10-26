# Documentation System - Quick Reference

## 🔄 Update Documentation Index

After creating new documentation files, use any of these commands:

### Simple Phrases
```
Update docs
Update session-init
Refresh documentation index
```

### Slash Command
```
/update-docs
```

### What Happens
Claude Code will:
1. ✅ Run `./.claude/update-docs-index.sh`
2. ✅ Discover new markdown files
3. ✅ Update `.claude/session-init.md`
4. ✅ Categorize files appropriately
5. ✅ Update the total count
6. ✅ Provide a summary

---

## 📁 File Structure

```
.claude/
├── session-init.md          # Main documentation index (35+ files)
├── README.md                # System explanation
├── QUICK_REFERENCE.md       # This file
├── update-docs-index.sh     # Discovery script
└── commands/
    └── update-docs.md       # Slash command definition
```

---

## 💡 Common Workflows

### After Implementing a New Feature
```
1. Feature implementation creates FEATURE_SETUP.md
2. You: "Update docs"
3. Claude: Updates session-init.md with new file
4. Done! Next session will know about the new doc
```

### Starting a New Session
```
Claude automatically:
1. Reads CLAUDE.md
2. Reads .claude/session-init.md
3. Discovers all 35+ documentation files
4. References them as needed during work
```

### Finding Documentation Manually
```
1. Open .claude/session-init.md
2. Browse by category (Core, Auth, Database, Mobile, etc.)
3. Files marked with ⭐ are critical
```

---

## 🎯 Example Usage

**You create:** `NEW_FEATURE_SETUP.md`

**You say:** "Update docs"

**Claude does:**
```bash
# Runs discovery script
./.claude/update-docs-index.sh

# Finds NEW_FEATURE_SETUP.md
# Updates session-init.md:

### Root Directory (14 files)  # ← Count updated
├── ...
├── NEW_FEATURE_SETUP.md      # ← Added to appropriate category
└── ...

# Responds with summary
"Added NEW_FEATURE_SETUP.md to the documentation index under Root Directory."
```

---

## 🔧 Maintenance Commands

| Command | Description |
|---------|-------------|
| `Update docs` | Refresh the documentation index |
| `/update-docs` | Slash command for same purpose |
| `./.claude/update-docs-index.sh` | Run discovery script manually |

---

## ✨ Benefits

- 🚀 **Zero friction:** Just say "update docs"
- 🤖 **Fully automated:** Claude handles categorization
- 📚 **Always current:** Index stays up to date
- 🔄 **Cross-session:** Next session knows about new docs

