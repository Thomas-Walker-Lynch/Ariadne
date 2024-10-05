#!/bin/env bash

if [ -z "$ENV_DEV" ]; then
  echo "make.sh:: script can only be run from in developer environment"
  return 1
fi

cd "$REPO_HOME"/developer/groovyc
groovyc AriadneGraph.groovy
