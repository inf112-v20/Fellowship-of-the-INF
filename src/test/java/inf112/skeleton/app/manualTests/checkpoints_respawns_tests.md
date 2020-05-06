# Manual tests for checkpoints and respawns  
## Setup  
* Run Main
* Press the Play button on the menu screen
* Press the 2 player button
* Select the second map


### Comment
* The player has 3 lives
* You loose lives by taking too much damage, falling into pits, or walking off the map
* If you run out of lives during the test, you need to close the window, then do the setup again.


## Respawn test 1
* Use arrow keys to steer the player1 robot into an abbys tile, or outside of map
* The player should respawn back at their respawn tile
* One of the lives, the large green circles on the UI, should have gone dark
* The player should have taken 2 damage, two triangles should have lit up on the UI


## Checkpoint test
* Look at you cards. See if you have any move1, move2, or move3 cards.
* If you have, then move on with test, otherwise close window and restart to get new cards.
* Move robot to stand a number of tiles away from the flag with the number 1
* Choose the card that will make the robot to move onto the respawn point
* Choose 4 more cards (these do not matter)
* Press the "Lock in" Button
* The first flag on the UI should now be filled in


## Respawn test 2
* Finish the round bu pressing the enter key after every phase
  * You should have a new set of cards and be back in the setup phase of the game to run this test
* Use arrow keys to once again loose a life by walking into pit or off the map
* You should now respawn at the flag


## Wrong checkpoint test
* Close window
* Do Setup
* Repeat checkpoint test, but with flag 2 or 3
* The flag on the UI should not be filled in
* Try respawn test 2
* You should respawn back at the first respawn marker.
