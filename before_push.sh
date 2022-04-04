#!/bin/bash
function error_message() {
   if test $1 -ne 0
    then
      echo -e "\n\e[91m$2"
      exit 1
  fi
}

function sanity() {
	echo -e "\n\e[93mExecuting linter..."
	./gradlew lintDebug

  status=$?
  error_message "$status" "Linter error."

	echo -e "\n\e[93mExecuting kotlin linter..."
	./gradlew ktlintCheck

  status=$?
  error_message "$status" "Kotlin linter error."
}

function unit_test() {
	echo -e "\n\e[93mExecuting tests..."
	./gradlew testDebugUnitTest

  status=$?
  error_message "$status" "Test error."
}

function print_help() {
  echo -e "Execute sanity and testing steps of jenkins pipeline,\n"
  echo -e "if some step fails, the current process will be aborted.\n"
  echo -e "With no arguments, only sanity step will be executed.\n"
  echo -e "\t-t, --test\tExecute tests suite"
  echo -e "\t-s, --sanity\tExecute sanity steps (detekt, lintDebug and ktlintCheck)"
  echo -e "\t-h, --help\tDisplay this help and exit"
}

# Print help and exit
if [ "$*" == "--help" ] || [ "$*" == "-h" ]; then
  print_help
  exit 0
fi

# Execute sanity task by default when args are empty
if [ "$*" == "" ]; then
  echo -e "\n\e[93mCleaning project..."
  ./gradlew clean
	sanity
	exit 0
else
  # Always clean before sanity or tests
  echo -e "\n\e[93mCleaning project..."
  ./gradlew clean
fi

# Loop flags and run  proper task
while test $# != 0
do
	case "$1" in
		-s|--sanity) sanity;;
		-t|--test) unit_test;;
	esac
	shift
done