#!/usr/bin/sh

pkill -f "wildfly"

rm -rf /home/huji/web/wildfly-34.0.0.Final/standalone/deployments/app.war

mv /home/huji/web/lab2/app/build/libs/*.war /home/huji/web/wildfly-34.0.0.Final/standalone/deployments/

bash /home/huji/web/wildfly-34.0.0.Final/bin/standalone.sh