#!/bin/sh
java -XX:+UseZGC -XX:+AlwaysPreTouch -Xmx128M -Xms128M -Djava.net.preferIPv4Stack=true \
  -jar ./app.jar --options '{ "preferNativeTransport": true }' -conf ./config.json -cp .
