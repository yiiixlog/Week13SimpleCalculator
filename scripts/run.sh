#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DEFAULT_JAVAFX_HOME="/Users/yvonnesu/Desktop/Week13SimpleCalculator/javafx-24.0.1"
JAVAFX_HOME="${JAVAFX_HOME:-$DEFAULT_JAVAFX_HOME}"
JAVAFX_LIB="$JAVAFX_HOME/lib"
BUILD_DIR="$ROOT_DIR/build/classes"

if [[ ! -d "$JAVAFX_LIB" ]]; then
  echo "JavaFX SDK not found at: $JAVAFX_HOME"
  echo "Set JAVAFX_HOME to your JavaFX SDK folder, for example:"
  echo "  export JAVAFX_HOME=/path/to/javafx-sdk-24.0.1"
  exit 1
fi

java_spec="$(java -XshowSettings:properties -version 2>&1 | awk -F= '/java.specification.version/ {gsub(/^[ \t]+|[ \t]+$/, "", $2); print $2; exit}')"
java_major="${java_spec#1.}"
java_major="${java_major%%.*}"

fx_major="0"
if [[ -f "$JAVAFX_LIB/javafx.properties" ]]; then
  fx_version="$(awk -F= '/^javafx.version/ {print $2; exit}' "$JAVAFX_LIB/javafx.properties")"
  fx_major="${fx_version%%.*}"
fi

required_java_major=""
if [[ -f "$JAVAFX_LIB/javafx.controls.jar" ]]; then
  class_major="$(javap -verbose -classpath "$JAVAFX_LIB/javafx.controls.jar" javafx.scene.control.Button 2>/dev/null | awk '/major version/ {print $3; exit}')"
  if [[ "$class_major" =~ ^[0-9]+$ ]]; then
    required_java_major=$((class_major - 44))
  fi
fi

if [[ -z "$required_java_major" && "$fx_major" =~ ^[0-9]+$ ]]; then
  if (( fx_major >= 24 )); then
    required_java_major=22
  fi
fi

if [[ "$required_java_major" =~ ^[0-9]+$ && "$java_major" =~ ^[0-9]+$ ]]; then
  if (( java_major < required_java_major )); then
    has_setup_error=1
    echo "This JavaFX SDK requires JDK $required_java_major or newer, but the active JDK is $java_spec."
    echo "Install/select JDK $required_java_major+ or use a JavaFX SDK compatible with JDK $java_spec."
  fi
fi

if [[ "$(uname -s)" == "Darwin" && -f "$JAVAFX_HOME/bin/glass.dll" ]]; then
  has_setup_error=1
  echo "The JavaFX SDK at $JAVAFX_HOME looks like a Windows build."
  echo "Download the macOS JavaFX SDK, then set JAVAFX_HOME to that folder."
fi

if [[ "${has_setup_error:-0}" == "1" ]]; then
  exit 1
fi

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

sources=()
while IFS= read -r source_file; do
  sources+=("$source_file")
done < <(find "$ROOT_DIR/src/main/java" -name '*.java' | sort)

javac \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls \
  -d "$BUILD_DIR" \
  "${sources[@]}"

cp -R "$ROOT_DIR/src/main/resources/." "$BUILD_DIR/"

java \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls \
  -cp "$BUILD_DIR" \
  calculator.CalculatorApp
