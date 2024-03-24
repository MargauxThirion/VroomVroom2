#!/bin/bash

# Trouver le chemin d'installation de Java et l'exporter comme JAVA_HOME
export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")

echo "JAVA_HOME set to $JAVA_HOME"

# Vérifier la version de Java pour s'assurer que JAVA_HOME est correct
$JAVA_HOME/bin/java -version

# Exécuter le build Gradle
./gradlew build
