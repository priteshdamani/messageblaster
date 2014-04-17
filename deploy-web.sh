#!/bin/sh
sudo service tomcat7 stop
sudo rm -rf /var/lib/tomcat7/webapps/ROOT/*
sudo cp -R ~/messageblaster/web/target/web-1.0/* /var/lib/tomcat7/webapps/ROOT
sudo service tomcat7 start
tail -f /var/lib/tomcat7/logs/catalina.out