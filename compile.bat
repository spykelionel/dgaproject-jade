@echo off
mkdir bin 2>nul
javac -cp lib\jade.jar -d bin src\dga\*.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    exit /b %errorlevel%
)
echo Running MainContainer...
java -cp bin;lib\jade.jar dga.MainContainer
pause
