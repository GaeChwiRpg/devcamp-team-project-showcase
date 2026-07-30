#!/usr/bin/env bash
# PreToolUse hook — 작업 경계 위반 자동 차단
#
# Edit/Write 호출 시 file_path가 다음 중 하나면 차단:
# - 미션 외 디렉토리 (예: 다른 부트캠프 폴더)
# - .env / 비밀 정보 파일
# - .git/ 내부

set -euo pipefail

INPUT="$(cat)"
FILE_PATH="$(echo "$INPUT" | jq -r '.tool_input.file_path // empty')"

if [[ -z "$FILE_PATH" ]]; then
  exit 0
fi

# 1. .env / 비밀 정보 차단
case "$FILE_PATH" in
  *.env|*.env.local|*.env.production|*secrets*|*credentials*|*.pem|*.key)
    echo "BLOCKED: 비밀 정보 파일 수정 시도: $FILE_PATH" >&2
    exit 2
    ;;
esac

# 2. .git/ 내부 차단
case "$FILE_PATH" in
  */.git/*|.git/*)
    echo "BLOCKED: .git/ 내부 수정 시도: $FILE_PATH" >&2
    exit 2
    ;;
esac

# 3. 작업 경계 (이 레포 root 외부) 차단
REPO_ROOT="$(git rev-parse --show-toplevel 2>/dev/null || pwd)"
case "$FILE_PATH" in
  "$REPO_ROOT"*) ;;  # 레포 내부 OK
  /*)
    echo "BLOCKED: 레포 외부 절대 경로 수정 시도: $FILE_PATH" >&2
    exit 2
    ;;
esac

exit 0
