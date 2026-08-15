# VoxPilot scenarios for Invasion (Forge 1.20.1)

JSON scenarios for [VoxPilot](https://github.com/Misosiruzuki/VoxPilot) against this MDK.

## Prerequisites

- Java 17
- `VoxPilot.jar` from VoxPilot GitHub Releases
- This repository checked out and able to run as a Forge 1.20.1 MDK (`port/1.20.1`)

## Run

```text
java -jar VoxPilot.jar run --project /path/to/Invasion-mod --scenario /path/to/Invasion-mod/voxpilot/scenarios/p0-continuous-start.json
```

Windows example:

```text
java -jar VoxPilot.jar run --project C:\src\Invasion-mod --scenario C:\src\Invasion-mod\voxpilot\scenarios\p0-continuous-start.json
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

## Scenarios (P0 continuous-mode skeleton)

| File | Purpose |
|------|---------|
| `p0-continuous-start.json` | `/invasion continuous` after nearby nexus; expect success chat + `active=true` |
| `p0-invasion-begin.json` | `/invasion begin 1` → end |
| `p0-continuous-night-probe.json` | Continuous + time advance; watch for attack / mode=3 |
| `p0-catalyst-give-smoke.json` | Creative give of catalysts + nexus place (smoke / screenshots) |

## Pass criteria (`p0-continuous-start`)

Client or server chat / log should contain:

```text
Started continuous mode (next attack scheduled)
```

`frames.jsonl` `trackedBlocks` for label `nexus` should show `active=true` after the continuous command.

`/invasion nexusstatus` should print a mode/power dump (not `No focus nexus`).

## Notes

- `/invasion` commands resolve nexus in order: existing focus → block under crosshair → nearest nexus within 12 blocks (so VoxPilot does not require a prior GUI open).
- Scenarios still briefly press `use` while facing the nexus as a belt-and-suspenders interact.
- Default config `minDaysToAttack` / `maxDaysToAttack` (2–3) makes real night waits long.
- VoxPilot may install performance mods into `run/`; see VoxPilot README.
- Operator player name used by VoxPilot offline server is `Dev`.
