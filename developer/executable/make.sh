#!/bin/env bash

if [ -z "$ENV_DEV" ]; then
  echo "make.sh:: script can only be run from the developer environment"
  return 1
fi

# Ensure we are in the right directory
cd "$REPO_HOME"/developer

# Clean the scratch_pad and jvm directories
echo "Cleaning scratch_pad and jvm directories..."
rm -rf scratch_pad/*
rm -rf jvm/*

# Compile all files
echo "Compiling files..."
groovyc groovyc/*.groovy -d scratch_pad
javac javac/*.java -d scratch_pad

if [ $? -ne 0 ]; then
  echo "Compilation failed."
  exit 1
fi

# Create a JAR file from the compiled class files
echo "Creating JAR file..."
mkdir -p jvm
jar cf jvm/Ariadne.jar -C scratch_pad .

if [ $? -eq 0 ]; then
  echo "JAR file created successfully: jvm/Ariadne.jar"
else
  echo "Failed to create JAR file."
  exit 1
fi
