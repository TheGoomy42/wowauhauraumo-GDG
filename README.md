### "Generic Dungeon Game"/Final Fantasy 1 clone
A simple dungeon crawler/RPG adventure game made in LibGDX Java.

Originally just a generic dungeon game, but after playing some classic FF1 I've decided to make it more like an FF1 clone as you can tell by the graphics so far.

This is basically my first attempt at making a decent game, so I am using a few tutorials etc. 
Feel free to contribute however you like, especially cleaning up the vast array of spaghetti code and probably bad use of 
LibGDX.

This is in the form of a gradle project, for more information of how to incoporate a LibGDX project 
[see here](https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-%28Eclipse%2C-Intellij-IDEA%2C-NetBeans%29)

Looking for built jars? Go to the /build/ folder for all of them.

#### Current Tasks
* **v0.3.0**
  * Document existing code
  * Find and implement a logger library
* **v0.4.0**
  * Add a dungeon
  * Add the ability to encounter and fight enemies in this dungeon

#### Version History
(based off of some variation of [semantic versioning](http://semver.org/))
* **v0.3.0 [CURRENT] [WIP]** 
  * Added some documentation to guide those who want to contribute. [WIP]
  * The player now walks slightly slower in the overworld.
  * Added logging using [EsotericSoftware's Minlog](https://github.com/EsotericSoftware/minlog)
* **v0.2.0** 
  * Added maps (currently just 2) and portals to travel between them.
* **v0.1.0** 
  * Initial release. Includes movement and collision on a single map.
