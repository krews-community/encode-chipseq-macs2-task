#!/bin/bash

set -e

# cd to project root directory
cd "$(dirname "$(dirname "$0")")"

docker build --target base -t genomealmanac/chipseq-macs2-base .

docker run --name chipseq-macs2-base --rm -i -t -d \
    -v /tmp/chipseq-test:/tmp/chipseq-test \
    genomealmanac/chipseq-macs2-base /bin/sh