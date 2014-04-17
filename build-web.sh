#!/bin/sh
git pull
mvn -P prod-web -DskipTests=true clean install
