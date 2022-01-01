#!/bin/bash
# Linter
echo -e "\n\e[93mExecuting linter..."
./gradlew lint

# Kotlin linter
echo -e "\n\e[93mExecuting kotlin linter..."
./gradlew ktlintCheck

# Unit testing
echo -e "\n\e[93mExecuting unit tests..."
./gradlew testDebugUnitTest