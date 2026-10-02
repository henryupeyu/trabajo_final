@echo off
chcp 65001 >nul
rem JDK identificado en la captura del equipo de Jhon.
if exist "%USERPROFILE%\.jdks\ms-21.0.12\bin\java.exe" set "JAVA_HOME=%USERPROFILE%\.jdks\ms-21.0.12"
if not defined JAVA_HOME (
    echo Configura JAVA_HOME con la carpeta de tu JDK 17 o 21.
    pause
    exit /b 1
)
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
call mvnw.cmd clean javafx:run
pause
