#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

POM=$SCRIPT_DIR/../pom.xml

## Detect if advanced regex is available
`echo 'asd   fsd' | sed -r 's/\s+(.*)/\1/' > /dev/null 2>&1`
if [ "$?" == "1" ]; then
  grep "version" $POM | head -n 1 | sed 's/[[:space:]]//g' | sed 's/<version>//' | sed 's/<\/version>//'
else
  grep "version" $POM | head -n 1 | sed -r 's/\s+<version>(.*)<\/version>/\1/'
fi
