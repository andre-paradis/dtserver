# Date time server

## Build instructions:

mvn package

## Run:

java -jar dtserver-1.0.jar  --port 8080 --idle-timeout 10

where:

--port specifies to port the server listen to
--idle-timeout specifies the number of seconds of inactivity after which the client is disconnected
