@echo off
echo.
echo **************************************
echo **************************************
echo Building debug/release base_config.xml
echo.
echo.
set source=%CD%\build\DEBUG_base_config.xml
set destination=%CD%\res\values\base_config.xml
set env="Debug"

if "%1"=="release" (
set source=%CD%\build\RELEASE_base_config.xml
set env="Release"
)

xcopy %source% %destination% /y
echo.
echo.
echo Build environment: %env%
echo **************************************
echo HELP: Setup release argument for 
echo production environment or nothing for 
echo development environment.
echo **************************************
echo.