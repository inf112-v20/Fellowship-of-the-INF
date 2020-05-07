# Manual tests for the delay between movement of robots
#### Setup
* Run Main.
* Press the Play button in the menu screen.
* Press the 8 button, choosing 8 players
* Select the first map on the map selection screen

### Tests
#### Test for delay
* Complete setup
* Select any of the five cards that do not cause your robot to die.
* Press the lock in button.
* *Assert: the robots execute the moves with a one second delay.*


#### Test for delay order
* Complete setup
* Select any of the five cards that do not cause your robot to die.
* Press the lock in button.
* *Assert: the robots execute the moves in the correct order.*
Note: since the delay between moves is short, you may want to observe a couple of phases before concluding
 that the robots are executing their moves in the correct order.
