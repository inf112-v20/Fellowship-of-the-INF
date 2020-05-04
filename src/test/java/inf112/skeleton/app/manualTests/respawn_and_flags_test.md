# Manual tests respawning and flags
### Setup
* Run Main.
* Press the Test button in the menu screen
* Choose "respawn_test_map" in the dropdown list and press start test. 


#### 1. Test for updating spawnpoint and flag
* Each robot has a small image in the lower left corner of the tile their spawnpoint is.
* If a robot stands on a flag or a repair site at the end of the phase their spawnpoint is updated to that position.
* Press the powerdown button.
* Confirm that the spawnpoint image for robot 1 is updated to flag 1 at the end of phase 1.
* Since flag 1 was also the flag robot 1 was supposed to get he also picked up that flag. The UI should be updated by 
highlighting the first flag.
* If you press ENTER after each phase you can go through all the phases and see that the spawnpoint image for each 
 robot is updated properly.(You can also press P to run the phases automatically). You can use TAB to see that all the
 other robots also picked up the flag.
 * At the end of phase 5 robot 1's spawnpoint image should update to flag 3 and all the spawnpoint images at flag 1
 should update properly as well. Since flag 3 is not the next flag for robot 1 to get the UI should not  highlight 
 the second flag. 
 
 #### 2. Test for respawning
 * Continue on from test 1 by powering down for round 2.
 * Play through all the phases in round 2 pressing ENTER or toggling on autostart by pressing P
 * All robots now has flag 3 as their spawnpoint.
 * Robot 1 was the first to die in that round so he is the one that respawns first.
 * You should be able to choose the direction you want to respawn in by using left or right arrow, but you cant
 move robot 1 (i.e. using up or down arrow). By pressing R you confirm the direction and the robot (robot 2) that died after
 robot 1 should then respawn after you have made your decision. They will choose one of the 8 adjacent position of their spawpoint since 
 robot 1 has already respawned on their spawpoint (which position they choose is depedent on their difficulty).
 
 
 * Continue on with powering down for round 3 and playing through it like before.
 * This time robot 3 will die first, then robot 4, then robot 1 (robot 2 may die before robot 1 if they choose to respawn to the
 left of robot 1).
 * Since robot 3 died first he is the one that will respawn on the spawnpoint, then robot 4 chooses its position to respawn in.
 * You should be able to choose one of the 8 adjacent cells which should be highlighted in the gamescreen. Invalid spawnpoints 
 (occupied positions, abysses etc.) are not highlighted. Leftclick the cell to respawn in (leftclick another cell if you want
 to change your decision) and choose the direction as before. Then press R to respawn. 
 




 

