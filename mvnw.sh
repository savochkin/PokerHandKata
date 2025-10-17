#!/bin/bash
# Helper script to run Maven commands with correct JAVA_HOME

export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# If no arguments provided, default to 'test'
if [ $# -eq 0 ]; then
    mvn test
else
    mvn "$@"
fi
