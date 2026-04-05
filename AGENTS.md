# AGENTS.md

## Project overview
This repository is a Minecraft Forge mod for Minecraft 1.20.x, based on Forge MDK.

The goal is to make safe, minimal, version-appropriate changes that fit the existing project structure and Forge conventions.

---

## General working rules
- Prefer small, targeted edits over large rewrites.
- Preserve existing architecture unless explicitly asked to refactor.
- Follow the patterns already present in the repository before introducing a new one.
- Keep compatibility with the current Minecraft version, Forge version, mappings, and Java version already configured in the project.
- Do not upgrade dependencies, mappings, Gradle plugins, or Forge itself unless explicitly asked.
- Do not introduce Fabric, NeoForge, Architectury, Kotlin, mixins, or other ecosystem-specific patterns unless explicitly requested.

---

## Read first
Before making version-sensitive or build-related changes, inspect these files first if they exist:
- `build.gradle`
- `gradle.properties`
- `settings.gradle`
- `src/main/resources/META-INF/mods.toml`
- the main mod entry class
- existing registry classes
- existing client setup classes
- existing datagen classes
- existing packet / networking classes
- existing config classes

When unsure, use the repository's existing code as the source of truth.

---

## What must remain stable
Do not change any of the following unless explicitly asked:
- mod id
- package names
- resource namespaces
- registry names / registry ids
- save/data formats
- network protocol identifiers
- public API names used by other parts of the mod
- Gradle wrapper files
- project layout

Assume registry names and resource ids are part of the mod's stable surface.

---

## Forge-specific rules

### DeferredRegister
When adding blocks, items, sounds, creative tabs, menu types, entity types, block entity types, mob effects, custom recipe serializers, custom recipe types, biome modifier serializers, or other registries:
- Prefer `DeferredRegister` and `RegistryObject` patterns.
- Reuse the repository's existing registration style if one already exists.
- Keep registration centralized and consistent.
- Do not replace existing `DeferredRegister` usage with ad hoc static initialization.
- Avoid registration side effects during class loading.
- Keep names stable and lowercase snake_case for registry ids.
- If a block is added, consider whether a matching `BlockItem` is needed.
- If a `BlockEntityType` is added, ensure the valid block set is correct.
- If an entity type is added, ensure attributes, renderer registration, spawn rules, and localization are handled where appropriate.
- If a custom menu or screen is added, keep client-only screen registration separate from common setup.

### Event bus and setup
- Use the correct Forge event bus for the task.
- Keep mod construction/setup logic in appropriate lifecycle events.
- Do not move client-only logic into common initialization.
- Do not reference client-only classes from common/server-safe code.
- Prefer existing subscription style already used in the repository:
    - `@Mod.EventBusSubscriber`
    - explicit listener registration
    - mod event bus registration in constructor
- Do not register duplicate listeners.

### Sides and client/server separation
- Treat client-only code as client-only.
- Put rendering, color handlers, screens, model layer registration, key mappings, and other client features into client-specific locations.
- Do not import client classes into common code paths.
- Be careful with level access, logical side, and dedicated server compatibility.
- When adding gameplay logic, ensure it behaves correctly on both integrated and dedicated servers.

### Networking
If editing packets or custom networking:
- Reuse the existing channel and packet registration pattern.
- Keep packet ids stable unless explicitly migrating the protocol.
- Ensure encode/decode/handle logic is symmetric and version-appropriate.
- Keep side checks explicit.
- Do not run unsafe world access on the wrong thread.
- Enqueue work when required by the existing networking pattern.

### Capabilities / attachments / persistent data
If the mod uses capabilities or other persistent attachment patterns:
- Reuse the existing storage and synchronization approach.
- Keep serialization keys stable.
- Preserve backward compatibility for saved data where practical.
- Only add migration logic if necessary and keep it minimal.

---

## Datagen rules
When generating data or assets:
- Reuse the project's existing datagen entry points, providers, and helper utilities.
- Do not replace manual assets with generated assets unless explicitly asked.
- Do not assume datagen is enabled; inspect the repository first.

When adding content, consider whether matching datagen updates are needed for:
- blockstates
- block models
- item models
- language entries
- loot tables
- recipes
- tags
- advancements
- sounds definitions
- damage types
- worldgen data
- biome modifiers
- pack metadata

### Datagen style
- Prefer extending existing provider classes and helper methods already used by the project.
- Keep generated naming aligned with registry ids.
- Do not generate unnecessary files.
- Avoid duplicating generated and manually maintained assets for the same id.
- If a resource is intentionally handwritten, preserve that decision unless explicitly asked to convert it.
- Respect existing output paths and pack structure.

### Tags and recipes
- When adding new items/blocks, consider whether tags are required for crafting, mining, interoperability, or gameplay logic.
- Prefer adding to existing tag files/providers rather than inventing redundant tags.
- Keep recipe ids stable and readable.
- Do not silently overwrite or rename recipe ids.

### Localization
- Add language entries for new user-facing names, tooltips, subtitles, GUI labels, and messages where appropriate.
- Follow the existing localization style.
- Do not remove existing translation keys unless explicitly asked.

---

## Resources and assets
Respect standard Minecraft / Forge resource layout:
- `assets/<modid>/...`
- `data/<modid>/...`

Rules:
- Resource names must be lowercase snake_case.
- Keep file paths aligned with registry ids.
- Do not rename or move assets unnecessarily.
- When adding a block/item, ensure models/textures/lang/loot/recipes/tags are considered as needed.
- When adding sounds, ensure sound definitions and subtitles are handled if the project does that.
- When editing JSON, preserve formatting conventions already present in the repository.

---

## Build and Gradle rules
- Do not modify `gradlew`, `gradlew.bat`, or `gradle/wrapper/*` unless explicitly asked.
- Do not change Java toolchain, mappings channel, Forge version, or plugin versions unless explicitly asked.
- Prefer minimal `build.gradle` edits.
- Avoid introducing new repositories unless strictly necessary and explicitly justified.
- Do not add large new dependencies for simple tasks.
- Do not enable unrelated plugins or code quality tools unless requested.

---

## Files and directories to avoid
Do not spend time editing or reading generated, cache, or local-environment files unless necessary:
- `.gradle/`
- `build/`
- `out/`
- `run/`
- `.idea/`
- `.vscode/`
- logs
- crash reports
- generated temporary output
- Gradle wrapper binaries
- local OS metadata files

Treat these as low-priority unless the task is specifically about them.

---

## Safe change policy
Before making a change:
1. Inspect existing patterns.
2. Prefer consistency over novelty.
3. Change only what is necessary for the requested task.
4. If a task is ambiguous, choose the least invasive interpretation.
5. If a change is version-sensitive, mention that clearly.

After making a change:
- Summarize what changed.
- Mention any assumptions.
- Mention any follow-up work still needed, especially assets, datagen, or registration steps.

---

## Validation checklist
Before finishing, validate as much as possible with the least invasive suitable checks.

Preferred order:
1. compile relevant code mentally against existing repository patterns
2. run `./gradlew build` when available
3. if the task is client/content related and the environment supports it, consider `./gradlew runClient`
4. if datagen was changed and the project has datagen configured, run the appropriate datagen task
5. inspect generated outputs for obvious id/path mismatches if datagen was involved

If you cannot run validation, say so explicitly.

---

## Content-specific checklists

### When adding a block
Consider all of the following:
- block registration
- `BlockItem` registration
- item group / creative tab visibility if used
- blockstate
- block model
- item model
- texture
- loot table
- lang entry
- tags
- recipe if applicable
- mining/tool requirements if the mod tracks them
- block entity association if needed

### When adding an item
Consider:
- item registration
- item model
- texture
- lang entry
- creative tab placement if applicable
- recipe if applicable
- tags if applicable

### When adding a block entity
Consider:
- `BlockEntityType` registration
- valid block binding
- ticker logic
- serialization
- menu/screen if applicable
- renderer if applicable
- synchronization if needed
- client/server separation

### When adding an entity
Consider:
- entity type registration
- attributes
- renderer
- spawn egg if applicable
- model / animation setup if the project uses one
- localization
- loot / drops
- spawn placement / rules if applicable

### When adding worldgen
Consider:
- placed/configured features or the version-appropriate Forge/Minecraft equivalent already used in the repo
- biome modifiers or tag-based integration if used
- data-driven registration consistency
- server safety
- pack data structure and ids

### When adding recipes or data-driven content
Consider:
- recipe serializer/type registration if custom
- unlock criteria
- recipe advancement
- tags
- localization impact
- JEI/REI-style integration only if the project already has it or the task requests it

---

## What not to do
- Do not perform broad package renames.
- Do not rewrite working registry code into a different style without a strong reason.
- Do not mix client-only code into common code.
- Do not silently change ids.
- Do not invent unsupported Forge APIs for 1.20.x.
- Do not assume mappings names or helper methods without checking local project usage.
- Do not replace existing handwritten resources with generated ones unless explicitly asked.
- Do not add unnecessary abstraction for simple content additions.

---

## Preferred response style for code changes
When finishing a task:
- briefly state what files were changed
- explain why those files were changed
- call out any Forge-version-sensitive decisions
- mention anything that still needs manual verification in-game or via datagen/build
