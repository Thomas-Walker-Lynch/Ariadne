#!/bin/env bash

# smoke test, and yes, there was a lot of smoke

source env_test

echo "test 0 will complain there are no build targets"
build TestGraph
echo ""

echo "test 1 will print the graph title"
build TestGraph title
echo ""

echo "test complete"

