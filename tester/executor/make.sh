#!/bin/env bash

if [ -z "$ENV_TESTER" ]; then
  echo "make.sh:: script can only be run in the tester  environment"
  env_error=true
fi

set -x

groovyc TestGraph.groovy
