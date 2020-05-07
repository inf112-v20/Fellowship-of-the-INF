# Manual tests for conveyorbelts
#### Setup 
* Run Main.
* Press the Test Stage button in the menu screen.
* Choose "conveyorbelt_test_map" in the dropdown list and press start test.
* (When given a position to move the robot to, note that (0,0) is bottom left and (11,11) is top right)
* Use arrows to move robot 1 and WASD to move robot 2. 


### Tests
#### 1. Test for moving robot on a conveyorbelt
* Complete setup.
* Move robot 1 onto a conveyorbelt
* Press BACKSPACE while robot 1 is on a conveyorbelt.
* Confirm that robot 1 move one tile in the direction of the conveyorbelt. If the conveyorbelt is also an turn the robot 
should also turn 90* in the direction of the turn.
*(Using backspace on expressbelts works, but it will only move the player one time. Only at the end of phases will it move
a player two times.)

#### 2. Test for moving robot on a expressbelt
* Complete setup.
* Move robot 1 onto an expressbelt that has another expressbelt in front of it. 
* Press powerdown. 
* Confirm that robot 1 is moved once by the first expressbeltpiece, 
and one more time by the second expressbeltpiece at the end of the phase. 

#### 3. Test for moving robot through a conveyorbelt turn
* Complete setup.
* Move robot 1 to (10, 2) (two tiles in front of it's spawn position)
* Press BACKSPACE two times.
* Move robot 1 to (10, 3)
* Press BACKSPACE once. 
* Confirm that robot 1 is only rotated 90* to the left when moving *through* the turn (i.e. starting at (10,2)),
but is not rotated when its only moved from the turnpiece (i.e. starting at (10,3)).

#### 4. Test for moving robot through a conveyorbelt turn with two inputs
* Complete setup.
* Move robot 1 to (2, 3)
* Press BACKSPACE two times.
* Move robot 1 to (3, 2)
* Press BACKSPACE two times. 
* Confirm that robot 1 is only rotated 90* to the right when moving *through* the turn (i.e. starting at (4,2))
 but not when its only moving straight through the turnpiece (i.e. starting at (3,3)).
 
 #### 5. Test for moving robots on a conveyorbelt with a robot in front not on a conveyorbelt
 * Complete setup.
 * Move robot 1 to (6,1).
 * Move robot 2 to (5,1).
 * Press BACKSPACE.
 * Move robot 2 away from (5,1)
 * Press BACKSPACE
 * Confirm that robot 1 is not moved by the conveyorbelt when pressing BACKSPACE while robot 2 is standing in front, 
 but is moved by the conveyorbelt when robot 2 is moved away.
 
#### 6. Test for moving robots on a conveyorbelt with a robot in front also on a conveyorbelt
* Complete setup.
* Move robot 1 onto a conveyorbelt
* Move robot 2 in front of robot 1 on a conveyorbelt that is not pointing towards robot 1. 
* Press powerdown. 
* Confirm that both robots are moved 1 tile in the direction of the conveyorbelt piece
 they were standing on at the end of phase.
 * You can press ENTER or P when the phase is done to test this several times. 

#### 7. Test for moving robots on a conveyorbelt that end up on the same cell
* Complete setup.
* Move robot 1 to (2,1).
* Move robot 2 to (0,1).
* Press powerdown
* Confirm that neither robot 1 or robot 2 is moved at the end of the phase.

#### 8. Test for moving robots on direct facing conveyorbelts
* Complete setup.
* Move robot 1 to (6,5).
* Move robot 2 to (6,4).
* Press powerdown
* Confirm that neither robot 1 or robot 2 is moved at the end of the phase.

#### 9. Test for moving robot on a expressbelt once before robot on normal conveyorbelts are moved
* Complete setup.
* Move robot 1 to (1,2).
* Move robot 2 to (0,1).
* Press powerdown
* Confirm that robot 1 is moved first and robot 2 is not moved at the end of the phase.

#### 10. Test for moving robots on a expressbelt once before crashing
* Complete setup.
* Move robot 1 to (9,11).
* Move robot 2 to (7,9).
* Press powerdown
* Confirm that both robot 1 and robot 2 is moved once, and neither is moved a second time,
 since they will crash if they do. 

#### 11. Tests for checking player order doesn't matter
* Pick any test that moves robot 1 and robot 2 at the end of a phase (e.g test 9)
* Swap position of robot 1 and robot 2.
* Confirm that the outcome of the tests are the same. 



