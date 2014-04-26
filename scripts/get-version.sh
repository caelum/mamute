#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

POM=$SCRIPT_DIR/../pom.xml

grep "version" $POM | head -n 1 | sed -r 's/\s+<version>(.*)<\/version>/\1/'

