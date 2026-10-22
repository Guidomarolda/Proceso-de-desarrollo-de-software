@echo off
chcp 65001 > nul
echo Compilando proyecto Veterinaria...
if not exist "out\app" mkdir "out\app"
if not exist "bin" mkdir "bin"
javac -encoding UTF-8 -d "out\app" -sourcepath src src\Veterinaria\Main.java
javac -encoding UTF-8 -d "bin" -sourcepath src src\Veterinaria\Main.java
if %errorlevel% neq 0 (
    echo [ERROR] La compilacion fallo.
    pause
    exit /b %errorlevel%
)
echo Ejecutando aplicacion grafica PetCarePlus...
start "PetCarePlus" java -cp "out\app" Veterinaria.gui.PetCarePlusApp
