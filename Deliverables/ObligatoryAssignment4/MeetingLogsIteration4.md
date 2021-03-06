# Meeting logs for the Fourth Iteration

## 30.03.2020
__Attendance:__ Everyone

__On the meeting:__ 
* MVP for the last iteration
  * Music? Yeah maybe find something
  * Should we have multiplayer? One person should be working on it, to not throw away too much man power
    * Johanna and Martin is going to take a look at multiplayer to get an overview of how it works
* Points of improvement:
  * Reflections
  * Meetings: From now on always at 2 o’clock on Mondays and Thursday
    * If you cannot attend at the set time, tell the group the day before
    * Johanna plans the meeting beforehand, what are we going to talk about? 
    * Meetings have become more effective, going from about 2 hours, down to 1 hour. 
* Tests: When you are going to make a MVP, then write acceptance crits, then you can use that to make tests afterwards 
* Erlend has done improvements to AI when choosing cards

__For next time__:
* Henrik: Lasers
* Lena: Powerdown
* Erlend: Make AI class that extends Player, timer
* Martin: Researching multiplayer and choosing number of players
* Johanna: Researching multiplayer and debugging


## 02.04.2020
__Attendance:__ Everyone

__On the meeting:__ 
* MVP: Mulitplayer is hard as LAN is not easy to test as we cannot be in the same place. We have decided to use AI instead.
* Main focus: Be able to finish the game; win/lose condition
* Martin, can you make test buttons, so we don’t have to comment out lines?
* Johanna explains rotate and respawn bugfix
* Erlend explains respawn bugfix, respawn at end of round
* In light of Siv’s announcement, we have all decided to further implement Erlend’s AI, instead of Multiplayer. Also be able to select AI difficulty.
* We’re going to keep working and having meetings during the easter break
* The meeting next Thursday, 9th of April, is to be held at 10 am.

__For next time:__
* Henrik: Lasers and repair tiles
* Lena: PowerDown and refactoring
* Erlend: Further AI development
* Martin: Make more buttons
* Johanna: Help Henrik with the visuals of lasers, try to implement pushers


## 06.04.2020
__Attendance:__ Everyone

__On the meeting:__
* We will be having a zoom presentation 14:35 on the 4th of May
* Lena has finished developing power down, but have some problems with nullpointer exception, so will not be pushing it to develop at the moment.
* Erlend has created different difficulties with the AI.
* Johanna showing and explaining new laser graphic.
* Johanna showing and explaining pushers

__For next time:__
* Lena: Make the laser shooters easier to see, refactoring and iron out bugs, sound affects
* Johanna: Make tests
* Erlend:  Implement proper respawning. Players can choose the direction they respawn in (and position if their spawn point is occupied)
* Martin: Implement Win condition and Lose condition, so that a game can actually finish.
* Henrik: Repair tiles


## 14.04.2020
__Attendance:__ Everyone

__On the meeting:__
* Visuals for backups, so the user knows where their backup is
* Next meeting will be at 10 o’clock this Thursday
* Johanna: Have made a new function for choosing different test maps
* Lena: Started adding background music and sound effects
* Erlend: Created a way to handle two players respawning to the same spot, visuals for backup
* MVP: The timer will start at the beginning of a card choosing fase, how much time you have depends on the difficulty 

__For next time:__
* Lena: Continue developing sounds, add pushers to map 2, check crash for map 2
* Johanna: map with pushers, crash map 2
* Erlend: Start timer immediately, visual for respawn position, 
* Martin: Implement 8 player support and further implementation of win/lose condition
* Henrik: help Martin


## 16.04.2020
__Attendance:__ Everyone

__On the meeting:__
* Lena pushed more sound effects to develop
* Erlend fixed timer to work according to the rules
* Together we found a fix for the problem where non quadratic maps
* Make more tests, do we need to make tests for front end

__For next time:__
* Lena: Sound effect for new phase/round, add pushers to map 2, finish power down
* Johanna: More maps, and add buttons for maps,, refactoring
* Erlend: bugfix x 10000
* Martin: bughunt
* Henrik: 


## 20.04.2020
__Attendance:__ Everyone

__On the meeting:__
* Martin went through menu screens and support for up to 8 players and win/lose condition.

__For next time:__
* Lena: Start writing Markdown documentation
* Johanna: merge my stageScelection screen with Martin’s
* Erlend: More bug fixing
* Martin: Make it possible to choose back to main menu or exit game on win/lose
* Henrik:


## 23.04.2020
__Attendance:__ Erlend, Henrik, Martin, Johanna

__On the meeting:__
* Johanna went through the new map selecetion screen.
* Talked about bugs, Erlend seemed to have fixed some

__For next time:__
* Johanna: Make demo presentation
* Martin: Make it possible to choose back to main menu on a win or loss
* Henrik: Bugs and checking code
* Erlend: Even more bug fixing.  Small refactor of conveyorbelts and how they work, and refactor how damage is taken/repaired
* Lena: Copy the Meeting logs from google document to .md

## 27.04.2020
__Attendance:__ Everyone

__On the meeting:__
* Johanna showed presentation, rest gave input
* Henrik: Everyone should go over classes and add documentation for classes and functions
* Johanna: Generally do clean up, use codacy to check
* Next Thursday the meeting will be held at 10 o'clock

__For next time:__ 
* Lena: Keep writing markdown, make tests for power down
* Erlend: Fix AI to not crash when there are no good cards left and testing.
* Everyone:
 * Check that you have commented your code
 * Check that your tests from previous iterations still work
 * Check for errors in code that you have authored on codacy
 * Move code from large classes (Player, LogicGrid, Game) to smaller classes or create new classes
 
 ## 30.04.2020
 __Attendance:__ Everyone
 
 __On the meeting:__
 * The goal is for the program to be done by the presentation on Monday
 * The documentation to be done for Thursday at the latest, to be sure that we do not have something breaking mere hours before the deadline

## 04.05.2020
__Attendance:__ Everyone

__On the meeting:__
* Johanna heled presentation
* We are as good as finished with the program
 * Most things that can be done would take too long
 
__For next time:__ 
* Try to look over all code and documentation, make sure that everything is ready for hand in
* Be ready by Thursday so we may ask group leaders for some last questions we might have, otherwise be done by then
