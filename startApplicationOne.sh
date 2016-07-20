#!/usr/bin/env bash
JAR="$(pwd)/target/encryptionAgency-1.0-SNAPSHOT.jar"
JAVA_OPTION=java.rmi.server.codebase=file://$(pwd)/target/classes/
killall rmiregistry
pkill -f /.*java.*encryptionAgency.*/g
sleep 2
rmiregistry -J-D${JAVA_OPTION} &
sleep 3
java -cp ${JAR} de.tub.sase.encryption.agency.ApplicationOne -D${JAVA_OPTION}