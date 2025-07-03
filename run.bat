@echo off
echo Chess Game - Compiling and Running...

REM Check if resources directory exists
if not exist "resources" (
    echo Error: resources directory not found!
    echo Please create a 'resources' directory and add your chess_pieces.png file.
    pause
    exit /b 1
)

REM Check if chess_pieces.png exists
if not exist "resources\chess_pieces.png" (
    echo Error: resources\chess_pieces.png not found!
    echo Please add your chess piece image file to the resources directory.
    echo The file should be named 'chess_pieces.png'
    pause
    exit /b 1
)

REM Create out directory if it doesn't exist
if not exist "out" mkdir out

REM Compile the Java files
echo Compiling Java files...
javac -d out src\*.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Starting the game...
    java -cp out ChessBoard
) else (
    echo Compilation failed!
    pause
    exit /b 1
)

pause 