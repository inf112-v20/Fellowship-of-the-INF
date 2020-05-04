# How to control the player:
- Use direction buttons (UP, DOWN, LEFT, RIGHT)
- LEFT and RIGHT only changes direction
- UP: Move forward one tile
- DOWN: Back up one tile

### Setup
* Run Main.
* Press the Test button in the menu screen
* Choose "laser_test_map" in the dropdown list and press start test. 

### Lasers test
* Rotate robot 1 to the right so he faces east.
* Press powerdown to start round 1.
* Every robot should shoot a laser at the end of phase in the direction they're facing. Board lasers should also fire.
* Confirm at the end of phase 1 that robot 1 shoots a laser towards robot 2 and robot 2 stops the laser. If you press
TAB to bring up the scoreboard you can see that robot 2 has taken 1 damage.
* Rotate 1 left so he faces north and move both robot 1 and robot 2 north one tile (up arrow and W are the buttons to
move the robots respectively).
* Press ENTER
* The wall laser should hit robot 2 and stop there, so robot 2 takes 1 damage while robot doesn't take any
(use TAB to check that robot 2 has 2 damage and robot 1 has 0).
* Move robot 1 north one more tile.
* Move robot 2 behind the wall (where flag 1 is).
* Press ENTER
* Robot 1 is hit by a double laser and should take 2 damage. Robot 2 is hiding behind the wall so the laser shouldn't
hit him. Robot 1 should now have 2 damage and robot 2 should also still have 2 damage.