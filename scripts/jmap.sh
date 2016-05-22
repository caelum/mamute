#!/bin/bash
# before using the script first you need to run jps and get the PID
# of the process to be profiled. Then run this script with the PID
# as argument
while true; do
    #http://unix.stackexchange.com/questions/196549/hide-curl-output
    curl -s localhost:8080 > /dev/null
    jmap -histo:live $1 | head -n 8
    sleep 20s
done
