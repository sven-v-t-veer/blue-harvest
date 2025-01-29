#!/bin/bash
java   --add-modules=ALL-SYSTEM \
       --add-opens=java.base/java.util=ALL-UNNAMED \
       -Xms256M \
       -Xmx512M \
       -jar blue-harvest.jar
