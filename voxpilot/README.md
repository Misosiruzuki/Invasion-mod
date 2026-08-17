# VoxPilot scenarios for Invasion (Forge 1.20.1)

JSON scenarios for [VoxPilot](https://github.com/Misosiruzuki/VoxPilot) against this MDK.

## Convention

**Every feature implementation must ship with a dedicated scenario** under `voxpilot/scenarios/` (plus unit tests when logic is pure). Do not land code-only changes without a scenario named for the checklist item or feature.

**Numbering:** One scenario per checklist number (e.g. `b40-…`, `b41-…`, `b42-…`). Do not bundle multiple checklist IDs into a single scenario file.

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

### Wiki parity B-40..B-42 (one scenario each)

| File | Purpose |
|------|---------|
| `b40-continuous-generate-wave.json` | B-40: full 1.7 group/finale `generateWave` → mode=3 → end |
| `b41-continuous-difficulty-power.json` | B-41: `power 9000` then soon; difficulty 1+power/4500 |
| `b42-continuous-wave-length.json` | B-42: default power continuous soon (lengthSeconds=240 path) |

### Wiki parity P1 / B-23..B-25 damping

| File | Purpose |
|------|---------|
| `b23-damping-agent-weak.json` | B-23: weak damping freezes power rise |
| `b24-damping-agent-strong.json` | B-24: strong damping drains power |
| `b25-damping-power-zero-shutdown.json` | B-25: drain past 0 → idle shutdown |

### Wiki parity P2 / B-26..B-27 Strong Catalyst

| File | Purpose |
|------|---------|
| `b26-strong-catalyst-creative.json` | B-26: creative give of Strong Catalyst |
| `b27-strong-catalyst-wave10.json` | B-27: activate strong → wave=10 invasion |

### Wiki parity P3 / D-41..D-45 Flux conversion

| File | Purpose |
|------|---------|
| `d41-flux-to-diamond.json` | D-41 smoke: give flux (+ crafting table) |
| `d42-flux-to-iron.json` | D-42 smoke |
| `d43-flux-to-redstone.json` | D-43 smoke |
| `d44-flux-to-lapis.json` | D-44 smoke |
| `d45-flux-conversion-shapes.json` | D-45: all conversion products giveable |

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
