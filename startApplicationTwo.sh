#!/usr/bin/env bash
JAR="$(pwd)/target/encryptionAgency-1.0-SNAPSHOT.jar"
JAVA_OPTION=java.rmi.server.codebase=file://$(pwd)/target/classes/
sleep 2
java -cp ${JAR} de.tub.sase.encryption.agency.ApplicationTwo -D${JAVA_OPTION}