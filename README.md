# Monopoly
This repository contains implementation of a Monopoly game clone.

## Simplifications
The game logic was altered to be simplier to code. It could be easily adjusted
to be exact copy of an original if necessary.

The list of changes:
- all chances are money centered;
- jail-exiting process was replaced with just a teleport;
- added automatic bankrupcy prevention by selling property;
- etc :)

## Demo
https://github.com/ivatolm/monopoly/assets/68593371/70e42fac-9797-427e-87fe-8c3d0664479d

# Installation
All required dependencies will be installed on the first run.
This project is using:
- [libgdx](https://github.com/libgdx/libgdx)
- [kryonet](https://github.com/EsotericSoftware/kryonet)

# Running
Running is done via gradle.
Just execute the following command in the root directory of the project:
```
./gradlew run
```
