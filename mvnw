#!/bin/sh
# Licensed to the Apache Software Foundation (ASF)

# Maven wrapper script
# This script downloads Maven if not already present

BASEDIR=`dirname "$0"`
BASEDIR=`cd "$BASEDIR" && pwd`

# Check if wrapper properties exist
if [ ! -f "$BASEDIR/.mvn/wrapper/maven-wrapper.properties" ]; then
  echo "Maven wrapper properties not found. Using system Maven."
  exec mvn "$@"
fi

# Read properties
MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$BASEDIR}"
MAVEN_WRAPPER_JAR="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.jar"

exec java -cp "$MAVEN_WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"

