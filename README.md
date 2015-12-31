### "Generic Dungeon Game"/Final Fantasy 1 clone
A simple dungeon crawler/RPG adventure game made in LibGDX Java.

See the wiki for information on how the game should work and feel free to contribute!

No, really... please refactor my code D:

Originally just a generic dungeon game, but after playing some classic FF1 I've decided to make it more like an FF1 clone as you can tell by the graphics so far.

This is basically my first attempt at making a decent game, so I am using a few tutorials etc. 
Feel free to contribute however you like, especially cleaning up the vast array of spaghetti code and probably bad use of 
LibGDX.

This is in the form of a gradle project, for more information of how to incoporate a LibGDX project 
[see here](https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-%28Eclipse%2C-Intellij-IDEA%2C-NetBeans%29)

Looking for built jars? Go to the /build/ folder for all of them.

#### Current Tasks
* **v0.4.0** The battles update
  * Add a random battle encounter system on the world screen
  * Add a battle screen where the player uses his four party members to fight a couple of enemies
    * Include fight, magic, item, drink and run options like the original game
* **v0.5.0** The main menu update
  * Add a splash screen
  * Add a main menu screen and GUI
    * Add a 'new game' button
      * Includes screen where you can select your characters FF1-style
* **v0.6.0**
  * Continue to document existing code
  * Add a dungeon
  * Add the ability to encounter and fight enemies in this dungeon
  * Add FF1-style battle mechanism
  * Add multiple floors using two-way and one-way portals
  * Import more tiles into the tilesets to use
  * Import enemy sprites

#### Version History
(based off of some variation of [semantic versioning](http://semver.org/))
* **v0.4.0 [CURRENT] [WIP]**
  * Added a battle screen with the layout working
    * Need to make the different cells more clearly defined
* **v0.3.0** 
  * Added some documentation to guide those who want to contribute. [WIP]
  * The player now walks slightly slower in the overworld.
  * Added logging using [EsotericSoftware's Minlog](https://github.com/EsotericSoftware/minlog)
  * Portals are now the universal teleporting mechanism. No spawns, exits or whatever. They also work, which is a plus.
    * Portals become 'inactive' you tp to them, until you stop colliding with it.
* **v0.2.0** 
  * Added maps (currently just 2) and portals to travel between them.
* **v0.1.0** 
  * Initial release. Includes movement and collision on a single map.
