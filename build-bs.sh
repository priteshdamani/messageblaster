#!/bin/sh
git pull
mvn -P prod-bs -DskipTests=true clean install
