# Manual tests for conveyorbelts
####Setup 
* Go to line 88 in the class StageSelectionScreen, it's in the screens package. Comment out line 88 and uncomment line 96.
* Go to Phase.java and uncomment line 77 to make robot 1 and 2 stand still during the phase to make testing easier. 
* Run Main.
* Press the Play button in the menu screen.
* Press the Test Stage button in the stage selection screen.


###Tests
#### Test for moving robot on a conveyorbelt
* Complete setup.
* Move robot 1 onto a conveyorbelt
* Press BACKSPACE while robot 1 is on a conveyorbelt.
* Assert: robot 1 is moved one cell in the direction of the conveyorbelt.

#### Test for moving robot on a expressbelt
* Complete setup.
* Move robot 1 onto an expressbelt that has another expressbelt infront
* Pick any 5 cards and lock in, or press ENTER if you have already have locked in cards . 
* Assert: robot 1 is moved once by the first expressbeltpiece, and one more time by the second expressbeltpiece at the end of the phase. 

#### Test for moving robot through a conveyorbelt turn
* Complete setup.
* Move robot 1 to (10, 2)
* Press BACKSPACE two times.
* Move robot 1 to (10, 3)
* Press BACKSPACE once. 
* Assert: robot 1 is only rotated 90* to the left when moving *through* the turn but not when its only moved from the turnpiece.

#### Test for moving robot through a conveyorbelt turn with two inputs
* Complete setup.
* Move robot 1 to (3, 3)
* Press BACKSPACE two times.
* Move robot 1 to (4, 2)
* Press BACKSPACE two times. 
* Assert: robot 1 is only rotated 90* to the right when moving *through* the turn but not when its only moving straight through the turnpiece.

#### Test for moving robots on a conveyorbelt with a robot in front also on a conveyorbelt
* Complete setup.
* Move robot 1 onto a conveyorbelt
* Move robot 2 in front of robot 1 on a conveyorbelt that is not pointing towards robot 1. 
* Pick any 5 cards and lock in, or press ENTER if you have already have locked in cards . 
* Assert: both robots are moved 1 cell in the direction of the conveyorbelt piece they were standing on at the end of phase.

#### Test for moving robots on a conveyorbelt with a robot in front not on a conveyorbelt
* Complete setup.
* Move robot 1 to (6,1).
* Move robot 2 to (5,1).
* Press BACKSPACE.
* Move robot 2 away from (5,1)
* Press BACKSPACE
* Assert: robot 1 is not moved when pressing BACKSPACE while robot 2 is standing in front, but is moved when robot 2 is moved away.

#### Test for moving robots on a conveyorbelt that end up on the same cell
* Complete setup.
* Move robot 1 to (2,1).
* Move robot 2 to (0,1).
* Pick any 5 cards and lock in, or press ENTER if you have already have locked in cards . 
* Assert: neither robot 1 or robot 2 is moved at the end of the phase.

#### Test for moving robots on direct facing conveyorbelts
* Complete setup.
* Move robot 1 to (6,5).
* Move robot 2 to (6,4).
* Pick any 5 cards and lock in, or press ENTER if you have already have locked in cards . 
* Assert: neither robot 1 or robot 2 is moved at the end of the phase.

#### Test for moving robot on a expressbelt once before robot on normal conveyorbelts are moved
* Complete setup.
* Move robot 1 to (1,2).
* Move robot 2 to (0,1).
* Pick any 5 cards and lock in, or press ENTER if you have already have locked in cards . 
* Assert: robot 1 is moved first and robot 2 is not moved at the end of the phase.

#### Test for moving robots on a expressbelt once before crashing
* Complete setup.
* Move robot 1 to (9,11).
* Move robot 2 to (7,9).
* Pick any 5 cards and lock in, or press ENTER if you have already have locked in cards . 
* Assert: robot 1 and robot 2 is moved once, and is not moved a second time since they will crash if they do. 

#### Tests for checking player order doesn't matter
* Pick any test that moves robot 1 and robot 2 at the end of the phase
* Swap position of robot 1 and robot 2.
* Assert: the outcome of the tests are the same. 



