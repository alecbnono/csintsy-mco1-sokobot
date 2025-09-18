#!/bin/bash
# Clean up old class files
find . -name "*.class" -type f -delete

# Compile
javac src/Driver.java -cp src

# Run with first argument ($1) + "fp"
java -cp src Driver "$1" fp
