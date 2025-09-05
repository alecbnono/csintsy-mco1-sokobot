#!/bin/bash
# Clean up old class files
find . -name "*.class" -type f -delete

# Compile
javac src/main/Driver.java -cp src

# Run with first argument ($1) + "fp"
java -cp src main.Driver "$1" fp
