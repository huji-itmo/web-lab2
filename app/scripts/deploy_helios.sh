#!/usr/bin/sh

# pkill -f "wildfly"

# rm -rf /home/huji/web/wildfly-34.0.0.Final/standalone/deployments/app.war

# mv /home/huji/web/lab2/app/build/libs/*.war /home/huji/web/wildfly-34.0.0.Final/standalone/deployments/

sshpass -f "/home/huji/.helios_pass" scp -P 2222 /home/huji/web/lab2/app/build/libs/*.war s389491@helios.cs.ifmo.ru:~/wildfly/wildfly-34.0.0.Final/standalone/deployments/
sshpass -f "/home/huji/.helios_pass" scp -P 2222 /home/huji/web/wildfly-34.0.0.Final/standalone/configuration/standalone.xml s389491@helios.cs.ifmo.ru:~/wildfly/wildfly-34.0.0.Final/standalone/configuration/

# bash /home/huji/web/wildfly-34.0.0.Final/bin/standalone.sh