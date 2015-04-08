@echo off

:loop
if "%~1" neq "" (
  echo Logging %1 ...
  java -jar jar/Words.jar %1 -nogui > run.log.tmp
  echo Diff with %1.log
  fc run.log.tmp %1.log >nul 2>&1 && echo OK || (echo FAILED: diff is not-empty.  Check run.log.tmp.  Aborting...  & exit 1)

  echo.
  echo ===========
  echo.
  shift
  goto :loop
)

del run.log.tmp
