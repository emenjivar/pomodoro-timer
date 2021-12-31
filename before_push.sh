#!/bin/bash
# Linter
./gradlew lint

# Kotlin linter
./gradlew ktlintCheck

# Unit testing
./gradlew testDebugUnitTest