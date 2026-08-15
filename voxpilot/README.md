# VoxPilot scenarios for Invasion (Forge 1.20.1)

JSON scenarios for [VoxPilot](https://github.com/Misosiruzuki/VoxPilot) against this MDK.

## Prerequisites

- Java 17
- `VoxPilot.jar` from VoxPilot GitHub Releases
- This repository checked out and able to run as a Forge 1.20.1 MDK (`port/1.20.1`)

## Run

From any directory (adjust paths):

```text
java -jar VoxPilot.jar run --project /path/to/Invasion-mod --scenario /path/to/Invasion-mod/voxpilot/scenarios/p0-continuous-start.json
```

Windows example:

```text
java -jar VoxPilot.jar run --project C:\src\Invasion-mod --scenario C:\src\Invasion-mod\voxpilot\scenarios\p0-invasion-begin.json
```

Reports are written under:

```text
run/voxpilot-reports/<timestamp>/
  report.html
  frames.jsonl
  *.png
  client/server logs
```

## Scenarios (P0 continuous-mode skeleton)

| File | Purpose |
|------|---------|
| `p0-continuous-start.json` | Debug continuous start (`/invasion continuous`), mode=2, nexus ON |
| `p0-invasion-begin.json` | Invasion regression (`/invasion begin 1` → end) |
| `p0-continuous-night-probe.json` | Continuous + time advance; watch for attack / mode=3 |
| `p0-catalyst-give-smoke.json` | Creative give of catalysts + nexus place (smoke / screenshots) |

## Notes

- Slot-based catalyst activation in the Nexus GUI is not fully automated here; P0 paths use `/invasion continuous` and `/invasion begin`.
- Default config `minDaysToAttack` / `maxDaysToAttack` (2–3) makes real night waits long. For night probe, the scenario advances time; expect partial coverage unless config is lowered for local testing.
- VoxPilot may install performance mods into `run/`; see VoxPilot README. Restore backups under `run/voxpilot-config-backup/` if you need vanilla-like behaviour.
- Operator player name used by VoxPilot offline server is `Dev`.
