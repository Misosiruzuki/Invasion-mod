@echo off
setlocal EnableExtensions EnableDelayedExpansion
chcp 65001 >nul

rem ============================================================
rem  Invasion-mod: sync local working tree with origin/port/1.20.1
rem  Prefer SSH remote (git@github.com:...). HTTPS also works if
rem  credential helper / PAT is already set up.
rem
rem  Usage (from anywhere):
rem    scripts\sync-port.bat
rem    scripts\sync-port.bat pull
rem    scripts\sync-port.bat push
rem    scripts\sync-port.bat status
rem    scripts\sync-port.bat use-ssh
rem ============================================================

set "BRANCH=port/1.20.1"
set "MODE=%~1"
if "%MODE%"=="" set "MODE=pull"

rem Move to repo root (parent of scripts\)
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
git log -1 --oneline
echo origin/%BRANCH%:
git log -1 --oneline "origin/%BRANCH%" 2>nul
echo.
git rev-list --left-right --count "origin/%BRANCH%...HEAD" 2>nul
echo ^(left=behind remote, right=ahead of remote^)
exit /b 0

:pull
echo Fetching origin/%BRANCH% ...
git fetch origin "%BRANCH%"
if errorlevel 1 (
  echo [ERROR] git fetch failed. Check SSH keys or remote URL.
  echo   Try: scripts\sync-port.bat use-ssh
  exit /b 1
)

git checkout "%BRANCH%" 2>nul
if errorlevel 1 (
  echo Creating local branch %BRANCH% tracking origin...
  git checkout -B "%BRANCH%" "origin/%BRANCH%"
  if errorlevel 1 (
    echo [ERROR] checkout failed
    exit /b 1
  )
) else (
  git branch --set-upstream-to="origin/%BRANCH%" "%BRANCH%" 2>nul
)

echo.
echo Checking for local uncommitted changes...
git diff --quiet --exit-code
set "DIRTY=%ERRORLEVEL%"
git diff --cached --quiet --exit-code
if errorlevel 1 set "DIRTY=1"
if not "%DIRTY%"=="0" (
  echo [WARN] Working tree has local changes.
  echo        Stash or commit before pull if merge conflicts worry you.
  git status -sb
  echo.
)

echo Fast-forward pull...
git pull --ff-only origin "%BRANCH%"
if errorlevel 1 (
  echo.
  echo [WARN] Fast-forward failed. Local and remote diverged.
  echo Options:
  echo   1^) Review:  git log --oneline --left-right origin/%BRANCH%...HEAD
  echo   2^) Rebase:  git pull --rebase origin %BRANCH%
  echo   3^) Force local to remote ^(DESTROYS local commits^):
  echo        git reset --hard origin/%BRANCH%
  exit /b 1
)

echo.
echo Synced. HEAD:
git log -1 --oneline
git status -sb
echo.
echo Optional: open report folder after VoxPilot
echo   dir run\voxpilot-reports
exit /b 0

:push
echo Pushing local %BRANCH% to origin...
git checkout "%BRANCH%" 2>nul
if errorlevel 1 (
  echo [ERROR] Local branch %BRANCH% missing
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
