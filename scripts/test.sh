#!/usr/bin/env bash
set -euo pipefail

LOG="$(mktemp)"

cd "$(dirname "$0")/.."

if ! ./gradlew testDebugUnitTest --console=plain >"$LOG" 2>&1; then
    grep -E "FAILED|^e: |tests completed" "$LOG" | head -40
    echo
    echo "Full log: $LOG"
    exit 1
fi

echo "unit tests passed"
