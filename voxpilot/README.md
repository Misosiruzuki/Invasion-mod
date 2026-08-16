# VoxPilot scenarios for Invasion (Forge 1.20.1)

JSON scenarios for [VoxPilot](https://github.com/Misosiruzuki/VoxPilot) against this MDK.

## Convention

**Every feature implementation must ship with a dedicated scenario** under `voxpilot/scenarios/` (plus unit tests when logic is pure). Do not land code-only changes without a scenario named for the checklist item or feature.

## Prerequisites

- Java 17
- `VoxPilot.jar` from VoxPilot GitHub Releases
- This repository checked out and able to run as a Forge 1.20.1 MDK (`port/1.20.1`)

## Run

```text
java -jar VoxPilot.jar run --project /path/to/Invasion-mod --scenario /path/to/Invasion-mod/voxpilot/scenarios/<scenario>.json
```

Windows example:

```text
java -jar VoxPilot.jar run --project C:\src\Invasion-mod --scenario C:\src\Invasion-mod\voxpilot\scenarios\b40-continuous-generate-wave.json
```

Reports:

```text
run/voxpilot-reports/<timestamp>/
  report.html
  frames.jsonl
  frames/*.png
  client.log
  server.log
```

## Scenarios

### P0 continuous-mode skeleton

| File | Purpose |
|------|---------|
| `p0-continuous-start.json` | `/invasion continuous`; mode=2, `active=true` |
| `p0-invasion-begin.json` | `/invasion begin 1` → end |
| `p0-continuous-night-probe.json` | `/invasion continuous soon`; mode=3 path (generic) |
| `p0-catalyst-give-smoke.json` | Give catalysts / flux / damping (smoke) |

### Wiki parity B-40..B-42

| File | Purpose |
|------|---------|
| `b40-continuous-generate-wave.json` | Full 1.7 continuous `generateWave` via `continuous soon` → mode=3 → end |

## Pass criteria (`b40-continuous-generate-wave`)

1. `Continuous mode; next attack in ~30 ticks`
2. Early `nexusstatus`: `mode=2`
3. `A continuous-mode attack has begun!` and/or `mode=3` / `contAtk=true`
4. `Invasion ended` → `mode=0`

## Notes

- `/invasion` resolves nexus: focus → look-at → nearest within 12 blocks.
- `continuous soon` is test-only (skips minDays wait).
- VoxPilot may install performance mods into `run/`.
- Operator player name: `Dev`.
