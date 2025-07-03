#!/bin/bash

# Chess Game Compile and Run Script

echo "Chess Game - Compiling and Running..."

# Check if resources directory exists
if [ ! -d "resources" ]; then
    echo "Error: resources directory not found!"
    echo "Please create a 'resources' directory and add your chess_pieces.png file."
    exit 1
fi

# Check if chess_pieces.png exists
if [ ! -f "resources/chess_pieces.png" ]; then
    echo "Error: resources/chess_pieces.png not found!"
    echo "Please add your chess piece image file to the resources directory."
    echo "The file should be named 'chess_pieces.png'"
    exit 1
fi

# Create out directory if it doesn't exist
mkdir -p out

# Compile the Java files
echo "Compiling Java files..."
javac -d out src/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Starting the game..."
    java -cp out ChessBoard
else
    echo "Compilation failed!"
    exit 1
fi 