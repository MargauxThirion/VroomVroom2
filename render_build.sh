#!/bin/bash

# Installer Java (exemple pour Ubuntu)
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk

# Définir JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Lancer le build Gradle
./gradlew build
