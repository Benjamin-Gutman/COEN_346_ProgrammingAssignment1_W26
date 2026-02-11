# Copilot / AI Agent Instructions for this repo

Purpose
- Help an AI coding assistant become immediately productive working on this assignment-style Java repo.

Big picture
- Small single-assignment project. Core code lives under `Support/` and runtime inputs under `DataSet/`.
- `Support/LevenshteinDistance.java` implements a Levenshtein distance calculator used to measure "change" between two strings; outputs a numeric distance and a `Change_Ratio` boolean `acceptable_change` flag.
- `DataSet/vm_1.txt` is a (large) input resource used by the project; treat it as an opaque test input (may be >50MB).

Key files to inspect
- `Support/LevenshteinDistance.java` — central algorithm implementation (methods: `Calculate`, `Measure_Change_Ratio`, `SubstitutionCost`, `isAcceptable_change`).
- `DataSet/` — contains runtime inputs; avoid opening very large files in the editor.

Build / run / debug
- There is no build tool (Maven/Gradle) in the repo. Use the JDK commands in the project root to compile Java sources:

```powershell
cd "COEN 346. PA1"
javac Support\*.java
```

- There is no main runner provided. If adding a quick test harness, place it under `Support/` (e.g. `Support/TestRunner.java`) and compile with `javac` above, then run with `java Support.TestRunner`.
- When debugging algorithm behavior, write small reproducer inputs (short strings) rather than using `DataSet/vm_1.txt` because that file is large.

Project-specific conventions and patterns
- Naming: mixed conventions — fields/methods use underscores and mixed casing (example: `Change_Ratio`, `Measure_Change_Ratio`). Follow existing naming when editing to minimize churn.
- Algorithm style: imperative, minimal helper utilities. Changes should be conservative and well-tested (unit-like harnesses recommended).
- Resource handling: dataset files live in `DataSet/`; treat them as external inputs not to be modified.

Integration points & external dependencies
- No external libraries or build system detected — plain JDK-only code.
- Communication between components is in-process (no network or IPC). Focus on function signatures and return values.

Discoveries & cautions (do not assume correctness)
- Potential bug: `LevenshteinDistance.Calculate` declares `int[][] dp = new int[str1.length() + 1][str1.length() + 1];` but indexes `j` up to `str2.length()` — likely the second dimension should be `str2.length() + 1`. Any change must include tests demonstrating the fix.
- `DataSet/vm_1.txt` can be very large; the editor/IDE may not load it fully. Use command-line tools or sample slices when testing.

How the agent should work here
- Read and reference `Support/LevenshteinDistance.java` when suggesting changes. Quote exact method names and line ranges in PR text and commit messages.
- Prefer minimal, test-backed patches: add a small `TestRunner` or JUnit test to reproduce a bug before changing algorithmic code.
- For I/O or dataset changes, prefer creating small synthetic samples under a `tests/` or `samples/` folder rather than editing `DataSet/` itself.

If anything is unclear
- Ask the human: what class should be the runtime entrypoint and whether you may add test harness files under `Support/` or a new `tests/` folder.

End of instructions — request feedback to iterate.
