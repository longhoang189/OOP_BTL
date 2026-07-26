@echo off
chcp 65001 >nul
echo Dang bien dich...
cd /d %~dp0
if not exist out mkdir out
for /r src %%f in (*.java) do set FILES=!FILES! "%%f"
setlocal enabledelayedexpansion
set FILES=
for /r src %%f in (*.java) do set FILES=!FILES! "%%f"
javac -encoding UTF-8 -d out -sourcepath src %FILES%
if %errorlevel% == 0 (
    echo Bien dich thanh cong! Dang chay...
    java -cp out Main
) else (
    echo Bien dich that bai!
    pause
)
