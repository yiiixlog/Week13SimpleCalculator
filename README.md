# Week 13 Simple Calculator

A simple JavaFX calculator with number buttons, clear, four arithmetic operators, equals, and keyboard input.

## Run the Calculator

This project can run without Maven or Gradle. It only needs a JDK and a JavaFX SDK.

```bash
# 1. 切換目前終端機環境至 JDK 24 (指定 Mac 系統的 Java 版本)
export JAVA_HOME=$(/usr/libexec/java_home -v 24)
export PATH="$JAVA_HOME/bin:$PATH"

# 2. 設定 JavaFX SDK 的絕對路徑 (指向你桌面上該專案資料夾內的 SDK)
export JAVAFX_HOME=/Users/yvonnesu/Desktop/Week13SimpleCalculator/javafx-sdk-26.0.1

# 3. 執行編譯與啟動計算機的腳本
./scripts/run.sh
```
Notes:

- JavaFX 24 requires JDK 22 or newer. Newer JavaFX SDKs may require a newer JDK.
- Use a JavaFX SDK that matches your operating system and CPU architecture.
- On this Mac, JDK 24 and `/Users/yvonnesu/Desktop/Week13SimpleCalculator/javafx-sdk-26.0.1` are installed and compatible.

## Test the Calculator Logic

The calculator model is separate from JavaFX, so it can be tested with the installed JDK:

```bash
./scripts/test.sh
```

## Project Structure

```text
src/main/java/calculator/CalculatorApp.java
src/main/java/calculator/CalculatorModel.java
src/main/resources/styles.css
src/test/java/calculator/CalculatorModelTest.java
scripts/run.sh
scripts/test.sh
```
