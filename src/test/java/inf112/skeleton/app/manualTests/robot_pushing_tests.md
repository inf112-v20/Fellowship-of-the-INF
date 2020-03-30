# Manual tests for the *robots pushing robots* functionality
### Setups

#### Setup nr. 1
* Go to line 82 in the class StageSelectionScreen, it's in the screens package. Comment out line 82 and uncomment line 84.
* Run Main.
* Press the Play button in the menu screen.
* Press the Test Stage button in the stage selection screen.

#### Setup nr. 2
* Go to line 84 in the class StageSelectionScreen, it's in the screens package. Comment out line 84 and uncomment line 86.
* Run Main.
* Press the Play button in the menu screen.
* Press the Test Stage button in the stage selection screen.

#### Setup nr. 3
* Go to line 86 in the class StageSelectionScreen, it's in the screens package. Comment out line 86 and uncomment line 88.
* Run Main.
* Press the Play button in the menu screen.
* Press the Test Stage button in the stage selection screen.

### Tests

#### Test for pushing single robot
* Complete setup nr. 1
* Press the UP key once.
* *Assert: the yellow robot has been pushed one space upward by the pink robot.*

#### Test for pushing robot off board
* Complete setup nr. 1
* Press the UP key four times
* *Assert: the yellow robot has been pushed off the board by the pink robot.*

#### Test for pushing robot by backing up into it
* Complete setup nr. 1
* Press the DOWN key once
* *Assert: the green robot has been pushed one space down by the pink robot.*

#### Test for pushing robot into abyss
* Complete setup nr. 1
* Press the DOWN key twice
* *Assert: the green robot has been pushed into the abyss by the rink robot.*

#### Test for pushing robot into wall
* Complete setup nr. 1
* Press the RIGHT key once.
* Press the UP key once.
* *Assert: the pink robot did not push the blue robot.*

#### Test for non-pushing movement
* Complete setup nr. 1
* Press the LEFT key once.
* Press the UP key twice.
* Press the DOWN key twice.
* *Assert: none of the robots have been pushed by the pink robot.*

#### Test for pushing multiple robots
* Complete setup nr. 2.
* Press the UP key once.
* *Assert: the pink robot has pushed all the other robots one cell upwards.*

#### Test for pushing multiple robots into wall
* Complete setup nr. 2.
* Press the UP key three times.
* *Assert: the line of robots has not been pushed past the wall.*

#### Test for pushing multiple robots into wall
* Complete setup nr. 3.
* Press the UP key six times.
* *Assert: all of the other robots have been pushed off the board by the pink robot.*

