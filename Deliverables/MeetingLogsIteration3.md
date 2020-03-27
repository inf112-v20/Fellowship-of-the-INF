# Meeting Logs for the Third Iteration

## 03.03.2020
__Attendence__: Martin, Erlend, Johanna, Lena, Henrik

__Summary__: 
* Discussed how we should distribute the remaining MVP's over this iteration and the next.
* Decided that it was important to start with introducing more robots on the board before we started implementing phases and rounds.

__Work for next time:__
* Make robot graphics (Lena)
  * Robot graphics needs to face the correct direction
  * Show a dead, alive and robot that has won
  * Must be 300px*300px
  * Robots must be numbered.
* Rework Deck to only be an ArrayList<ProgramCard> (Lena)
* Implement the framework for phases and rounds (Johanna)
* Implement consisten scaling of the MainMenu (Henrik)
* Make a test map (Henrik)
* Check out dispose (Erlend)
* Continue developing the game UI (Erlend)


## 05.03.2020
__Attendence__: Everyone

__Summary__: 
* Emphasised the fact that everyone needs to check discord regularly, perhaps download the app on their phone so they get notifications.
* Lena showed us her fantastic robot graphics!
* Erlend took us through his new UI elements, and afterwards merged it into develop
* Johanna took us through the new classes for Game, Round and Phase, then merged it into dev.
* Added new tasks to project board.

__Work for next time:__
* Implement conveyor belts and cog wheels (Erlend)
* Implement spawn points and flags (Lena)
* Implement delay, so that all the robots don't move simultaneously (Johanna)
* Learn about Codacy, and integrate it into the project (Martin)
* Implement lasers (Henrik)


## 09.03.2020
__Attendence__: Everyone

__Summary__: 
* Reviewed the implementation of respawns, conveyor belts, cog wheels and delay bewteen robot moves, and merged them into dev.
* Spent time fixing some problems with git.
* There were some problems with the merging of Erlend's branch into dev, so this was not completed at the meeting.


__Work for next time:__
* Create some new maps, and finish flag interation (Lena)
* Create a map selection screen (basically new buttons on the main menu screen) (Martin)
* Fix bugs and merge conflicts between Erlend's branch and dev (Johanna)
* Implement damage from lasers (Henrik)


## 12.03.2020
__Attendence__: Everyone

__Summary__: 
* Reviewed flag interaction.
* Martin showed us how Codacy works.
* Lena drew our attention to some serious bugs in the LogicGrid class.

__Work for next time:__
* Implement pushing of robots (Johanna)
* Fix the wall-bug discussed during the meeting (Martin)
* Implement more types of conveyorbelts and add conflict solution when robots push other robots on conveyorbelts. (Erlend)
* Fix bugs in LogicGrid and Flag. (Lena)
* Finish implementing damage from lasers (Henrik)


## 19.03.2020
__Attendence__: Everyone

__Summary__: 
* Lena went through her implementation of flags, and explained how respawns worked.
* Johanna talked about some bugs she found in the development branch
* Martin took us through the new main menu screen and new stage selection screen.
* Henrik explained us how his laser implementation worked.
* Erlend took us through his conveyorbelt implementaiton, and the new player 2 input keys.

__Work for next time:__
* Graphical blinking of lasers, so that they aren't viewed as active all the time. (Henrik)
* Implement ability to choose the number of players in the menu screen. (Martin)
* Finish implementing pushing (Johanna)
* Implement respawn after death, and fix problemns in her map. (Lena)
* Clean up the remaing bugs to do with the conveyorbelt and implement express belts. (Erlend)
* Generally work on debugging and cleaning up their code (Everyone)


## 23.03.2020
__Attendence__: Martin, Erlend, Johanna, Lena

__Summary__: 
* Martin showed us how the new PlayerSelectioScreen worked
* Johanna listed up the tasks that needed to be done for the hand in on Friday.
* We talked about how to use Codacy for code quality improvement. Realised that only Martin and Johanna were added as authors to the project on Codacy.

__Work for next time:__
* Make a first draft of the markdown file (Johanna)
* Make manual tests (Everyone)
* Make automatic tests (Everyone)
* Check if necessary tests have been made. (Erlend)


## 26.03.2020
__Attendence__: Everyone

__Summary__: 
 * Several tests have been made
 * Johanna explained how collision works
 * Lena had fixed respawns once again.
 * Decided to clean up the master branch, and keep TODOs on develop for the time being

__Work for next time:__
 * Finish making tests for the assignment
 * Clean up MarkDown
 * Ready to hand in assignment
