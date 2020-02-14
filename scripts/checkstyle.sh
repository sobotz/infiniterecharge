#!/usr/bin/env bash

# If any files are changed after we run the formatter, the dev forgot to run them
if [[ `git status --porcelain src` ]]; then
    echo "Working tree should be empty after running style formatters; make sure you're running ./gradlew spotlessApply"

    exit 1;
else
    exit 0;
fi