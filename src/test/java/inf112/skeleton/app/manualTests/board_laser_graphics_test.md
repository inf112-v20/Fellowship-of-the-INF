# Manual tests for the board laser graphics
### Setup
* Run Main.
* Press the Test button in the menu screen.
* Select the map "board_laser_graphics_test_map" from the dropdown menu
* Press the "Start Test" button

### Tests

#### Test for board lasers only shown when active
* Complete the setup
* Pick any cards and press the lock in button.
* *Assert: board lasers are only shown when lasers are active*


#### Test for lasers being shown with the correct cell
There are both double and single lasers, and each of them can face either north, south, east or west. Hence, hence we need to check all 8 types of lasers.
* Complete the setup
* Pick any cards and press the lock in button. (If you don't have the patience to wait for a phase to pass, you can repeatedly press B to activate the board laser graphics)
* Wait until the lasers are active
* *Assert: the south-facing vertical double laser sources are shooting vertical double lasers*
* *Assert: the north-facing vertical double laser sources are shooting vertical double lasers*
* *Assert: the west-facing horizontal double laser sources are shooting horizontal double lasers*
* *Assert: the east-facing horizontal double laser sources are shooting horizontal double lasers*
* *Assert: the south-facing vertical single laser sources are shooting vertical single lasers*
* *Assert: the north-facing vertical single laser sources are shooting vertical single lasers*
* *Assert: the west-facing horizontal single laser sources are shooting horizontal single lasers*
* *Assert: the east-facing horizontal single laser sources are shooting horizontal single lasers*


#### Test for lasers not being drawn where they do not reach
Reminder: PusherPiece and LaserSourcePiece extend WallPiece.
* Complete the setup
* Press B repeatedly to activate the board laser graphics.
* *Assert: horizontal lasers are blocked by vertical wall pieces*
* *Assert: vertical lasers are blocked by horizontal wall pieces*
* *Move robot 1 around and assert that the robot is blocking the path of lasers that hit it*

###Test for lasers being drawn where they do reach
* Complete the setup.
* Press B repeatedly to activate the board laser graphics.
* *Assert: None of the tiles north of player 1 (apart from those with robots on them) block the path of the laser*

