@echo off
setlocal EnableExtensions EnableDelayedExpansion
chcp 65001 >nul

rem ============================================================
rem  Invasion-mod: sync local working tree with origin/port/1.20.1
rem  Prefer SSH remote (git@github.com:...).
rem
rem  Default "pull" OVERWRITES local tracked files to match remote
rem  (checkout -f + reset --hard). Untracked files that would block
rem  checkout are removed with git clean for paths coming from remote.
rem
rem  Usage:
rem    scripts\sync-port.bat           same as pull
rem    scripts\sync-port.bat pull      force sync to origin (overwrite)
rem    scripts\sync-port.bat push
rem    scripts\sync-port.bat status
rem    scripts\sync-port.bat use-ssh
rem ============================================================

set "BRANCH=port/1.20.1"
set "MODE=%~1"
if "%MODE%"=="" set "MODE=pull"

cd /d "%~dp0.."
if not exist ".git" (
  echo [ERROR] Not a git repo: %CD%
  exit /b 1
)

where git >nul 2>&1
if errorlevel 1 (
  echo [ERROR] git not found on PATH
  exit /b 1
)

echo.
echo === Invasion-mod sync ===
echo Repo:   %CD%
echo Branch: %BRANCH%
echo Mode:   %MODE%
echo.

if /i "%MODE%"=="use-ssh" goto :use_ssh
if /i "%MODE%"=="status" goto :status
if /i "%MODE%"=="pull" goto :pull
if /i "%MODE%"=="push" goto :push
if /i "%MODE%"=="sync" goto :pull

echo [ERROR] Unknown mode: %MODE%
echo Use: pull ^| push ^| status ^| use-ssh
exit /b 2

:use_ssh
echo Switching origin to SSH...
git remote set-url origin git@github.com:Misosiruzuki/Invasion-mod.git
git remote -v
echo.
echo Done. Future pull/push use SSH.
exit /b 0

:status
git remote -v
echo.
git fetch origin "%BRANCH%" 2>&1
if errorlevel 1 (
  echo [ERROR] git fetch failed
  exit /b 1
)
echo.
git status -sb
echo.
echo Local HEAD:
git log -1 --oneline HEAD 2>nul
echo origin/%BRANCH%:
git log -1 --oneline "origin/%BRANCH%" 2>nul
echo.
git rev-list --left-right --count "origin/%BRANCH%...HEAD" 2>nul
echo ^(left=commits only on remote, right=commits only on local^)
exit /b 0

:pull
echo Fetching origin/%BRANCH% ...
git fetch origin "%BRANCH%"
if errorlevel 1 (
  echo [ERROR] git fetch failed. Check SSH keys or remote URL.
  echo   Try: scripts\sync-port.bat use-ssh
  exit /b 1
)

echo.
echo WARNING: Local tracked files will be OVERWRITTEN to match origin/%BRANCH%.
echo          Untracked files that conflict with the branch will be removed.
echo.

rem Force-create/switch local branch to origin tip (overwrites index + worktree)
git checkout -f -B "%BRANCH%" "origin/%BRANCH%"
if errorlevel 1 (
  echo [ERROR] force checkout failed
  exit /b 1
)

git branch --set-upstream-to="origin/%BRANCH%" "%BRANCH%" 2>nul

rem Hard reset so HEAD / index / worktree match remote exactly
git reset --hard "origin/%BRANCH%"
if errorlevel 1 (
  echo [ERROR] git reset --hard failed
  exit /b 1
)

rem Remove untracked files/dirs that are not ignored (keeps run/, .gradle/, etc. if gitignored)
rem -d: directories, -f: force. Does NOT use -x so ignored build outputs stay.
echo Cleaning untracked files that are not gitignored...
git clean -fd
if errorlevel 1 (
  echo [WARN] git clean reported an issue; continuing
)

echo.
echo Synced (overwrite). HEAD:
git log -1 --oneline
git status -sb
echo.
echo Tip: VoxPilot reports under run\voxpilot-reports are kept if gitignored.
exit /b 0

:push
echo Pushing local %BRANCH% to origin...
git checkout "%BRANCH%" 2>nul
if errorlevel 1 (
  echo [ERROR] Local branch %BRANCH% missing. Run: scripts\sync-port.bat pull
  exit /b 1
)
git push -u origin "%BRANCH%"
if errorlevel 1 (
  echo [ERROR] push failed
  exit /b 1
)
echo.
echo Pushed.
git log -1 --oneline
exit /b 0
