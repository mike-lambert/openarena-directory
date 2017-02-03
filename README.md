# OpenArena Directory Server
A web service, which intended to provide web API and web UI for master servers of FPS games such as OpenArena or Quake 3.
It also can acts as frontend for [qstat](https://sourceforge.net/projects/qstat/) utility.
## Build requirements
JDK 8 itself and no more. Just use gradle wrapper in project.

- Build zip with self-hosted app and boot scripts :  **gradlew packageApp**
- Build docker image with app:  **gradlew dockerImage**
- Build WAR for deploying over servlet container :  **gradlew war**
- Run app by Gradle:  **gradlew appRun**
- Run app for debugging:  **gradlew appRunDebug**

## QStat configuration addition
QStat doesn't work with OpenArena out of the box, so, you must edit qstat.cfg and add next lines:

```
gametype OPENARENA new extend Q3S
    name = OpenArena Server
    template var = OA
    game rule = gamename
end
gametype OAM new extend Q3M
    name = Openarena Master
    default port = 27950
    master for gametype = OPENARENA
    master protocol = 71
end
```

## Environment assumptions
1. installed wine
2. qstat.exe at /opt/openarena/qstat directory

When using Windows, or having native binary under Linux/Mac, you must replace this path in relevant sources for passing tests