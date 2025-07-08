#!/usr/bin/env bash

set -euo pipefail

# --- Artifactory repo URLs ---
ARTIFACTORY_RELEASE_URL="https://depop.jfrog.io/artifactory/depop-scala-local/"
ARTIFACTORY_SNAPSHOT_URL="https://depop.jfrog.io/artifactory/depop-snapshot-local/"

# --- Validate required env vars ---
for var in BRANCH_NAME JFROG_API_USERNAME JFROG_API_KEY; do
  if [[ -z "${!var:-}" ]]; then
    echo "Environment variable $var is not set"
    exit 1
  fi
done

if [[ "$BRANCH_NAME" == "master" ]]; then
  # PUBLISH_VERSION unset for master builds. Defaulted in gradle build file.
  export ARTIFACTORY_URL="$ARTIFACTORY_RELEASE_URL"
else
  export PUBLISH_VERSION="$(echo "$BRANCH_NAME" | tr '/ ' '-' | tr -cd '[:alnum:]-')-SNAPSHOT"
  export ARTIFACTORY_URL="$ARTIFACTORY_SNAPSHOT_URL"
  echo "Version: $PUBLISH_VERSION"
fi

echo "Branch: $BRANCH_NAME"
echo "Repo: $ARTIFACTORY_URL"
echo

./gradlew :dispute-tasks:publish --info

echo
echo "✅ Publish complete."
