# OpenArena Directory Server
A web service, which intended to provide web UI and widget factory for [DPMQuery microservice](https://github.com/mike-lambert/dpmquery).

## Build requirements
JDK 8 itself and no more. Just use gradle wrapper in project.

## Building
- Build zip with self-hosted app and boot scripts :  
```bash
./gradlew packageApp
```
- Build docker image with app:  
```bash
./gradlew dockerImage
```
- Build WAR for deploying over servlet container :  
```bash
./gradlew war
```

## Running
- Run app by Gradle:  
```bash
./gradlew appRun
```
- Debugging:  
```bash
./gradlew appRunDebug
```
- Run in production
```bash
cd (here path to unpacked app)
./run.sh
```
on Windows **./run.bat** respectively