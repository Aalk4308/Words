#!/bin/bash

for prog in "$@"; do
    echo "Logging $prog ..."
    java -jar jar/Words.jar "$prog" -nogui > run.log.tmp
    echo "Diff with $prog.log"

    if ! diff run.log.tmp "$prog.log"; then
    	echo "FAILED: diff is not-empty.  Check run.log.tmp.  Aborting..."
    	exit 1
    fi
    echo "OK"

    echo
    echo "==========="
    echo
done

rm run.log.tmp
