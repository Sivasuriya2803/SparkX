@REM Licensed to the Apache Software Foundation (ASF)
@REM under one or more contributor license agreements.

@echo off
setlocal enabledelayedexpansion

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

cd /d "%APP_HOME%"

@REM Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >nul 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to %JAVA_HOME%, but JAVA is not found there.
echo.
goto fail

:execute
@REM Execute Maven
"%JAVA_EXE%" -version >nul 2>&1
"%JAVA_EXE%" -cp ".mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
if "%ERRORLEVEL%" == "0" goto end

@REM Fallback to system Maven if wrapper not available
@echo Attempting to use system Maven...
mvn %*

:end
@endlocal & exit /b %ERRORLEVEL%

:fail
@endlocal & exit /b 1

