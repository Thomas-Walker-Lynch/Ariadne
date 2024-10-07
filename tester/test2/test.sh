#!/bin/env bash

# smoke test, and yes, there was a lot of smoke
set -x

source env_test
echo $CLASSPATH
build TestGraph HelloWorld.class

echo "test complete"

