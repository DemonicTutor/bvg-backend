#!/bin/sh
cd ./target

java -XX:+UseZGC -XX:+AlwaysPreTouch -Xmx128M -Xms128M -Djava.net.preferIPv4Stack=true \
  -jar ./bvg-backend-fat.jar --options '{ "preferNativeTransport": true }' --config '{ "name": "bvg" }' -cp .
