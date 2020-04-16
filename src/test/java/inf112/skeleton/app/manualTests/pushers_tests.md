# Manual tests for the pushers
### Setups

#### Setup nr. 1 TODO: map 1
* Press the Play button in the menu screen.
* Press the Test Stage button in the stage selection screen.

#### Setup nr. 2
TODO: map 2

#### Setup nr. 3


### Tests

#### Test for pushing in correct direction pt1
* Complete setup nr. 3
* Pick cards so that you do not change position in the next phase (rotate cards).
* Press 0 , then the lock in button.
* Wait for one phase to be executed
* *Assert: player one is being pushed in circles by the pushers.*


#### Test for pushing in correct direction pt2
* Complete setup nr. 4
* Pick cards so that you do not change position in the next phase (rotate cards).
* Press 0 , then the lock in button.
* Wait for one phase to be executed
* *Assert: player one is being pushed in circles by the pushers.*

#### Test odd-numberes pushers are only active during odd-numbered rounds
* Complete setup nr. 5
* Pick cards so that you do not change position in the next phase (choose rotate cards).
* Press 0 , then the lock in button.
* Wait for one phase to be executed
* *Assert: player one is being pushed in circles by the pushers.*

#### Test even-numberes pushers are only active during even-numbered rounds
* Complete setup nr. 6
* Pick cards so that you do not change position in the next phase (choose rotate cards).
* Press 0 , then the lock in button.
* Wait for one phase to be executed
* *Assert: player one is being pushed in circles by the pushers.*


#### Test for only being pushed once by pushers per phase
* Complete setup nr. 2 (map 2)
* Pick cards so that you do not change position.
* Press 0 , then the lock in button.
* Wait for one phase to be executed
* *Assert: player 1 has only been pushed once by the pushers.*

#### Test for blocking-functionality of pushers
* Complete setup nr. 2
* Press DOWN key once.
* *Assert: Robot 1 could not go through the pusher.*


* Press UP key once.
* Press LEFT key once:
* Press UP key once.
* *Assert: Robot 1 could not go through the pusher.*


* Press DOWN key twice.
* *Assert: Robot 1 could not go through the pusher.*


* Press RIGHT key once.
* Press UP key once.
* Press LEFT key once.
* Press DOWN key once.
* *Assert: Robot 1 could not go through the pusher.*


* Press UP key twice.
* *Assert: Robot 1 could not go through the pusher.*


* Press RIGHT key once.
* Press UP key twice.
* *Assert: Robot 1 could not go through the pusher.*


* Press RIGHT key once.
* Press UP key once.
* Press LEFT key once:
* Press UP key once.
* *Assert: Robot 1 could not go through the pusher.*


* Press DOWN key four times.
* *Assert: Robot 1 could not go through the pusher.*

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


