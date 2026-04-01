#!/bin/bash
# ─────────────────────────────────────────────────────────
#  Vehicle Rental System — Build & Run Script
#  Requires Java 11+ (JDK with jdk.compiler module)
# ─────────────────────────────────────────────────────────

echo "=== Vehicle Rental System ==="
echo ""

# Step 1: Compile
echo "[1/2] Compiling..."
mkdir -p out
java --add-modules jdk.compiler Compile.java
if [ $? -ne 0 ]; then
  echo "Compilation failed. Make sure you have a JDK (not just JRE) installed."
  exit 1
fi

# Step 2: Run
echo ""
echo "[2/2] Starting application..."
echo ""
java -cp out com.rental.Main
