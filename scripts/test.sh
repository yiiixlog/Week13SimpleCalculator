#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MAIN_OUT="$ROOT_DIR/build/test-main"
TEST_OUT="$ROOT_DIR/build/test-classes"

rm -rf "$MAIN_OUT" "$TEST_OUT"
mkdir -p "$MAIN_OUT" "$TEST_OUT"

javac \
  -d "$MAIN_OUT" \
  "$ROOT_DIR/src/main/java/calculator/CalculatorModel.java"

javac \
  -cp "$MAIN_OUT" \
  -d "$TEST_OUT" \
  "$ROOT_DIR/src/test/java/calculator/CalculatorModelTest.java"

java \
  -cp "$MAIN_OUT:$TEST_OUT" \
  calculator.CalculatorModelTest
