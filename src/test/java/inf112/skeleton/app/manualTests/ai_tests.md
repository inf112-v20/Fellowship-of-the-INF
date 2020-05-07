# Manual tests for AI Players


#### Test for different difficulties. 
##### Some information
* Each AI player is given the same cards to easily demonstrate the difference between the difficulties.
* The cards are:
    * 2 Move 1
    * 1 Move 2
    * 1 Backup
    * 1 Rotate Right
    * 2 Rotate Left
    * 2 U Turn
* Each AI player's goal flag corresponds to their player number (which is the flag in front of them)
* Player 2 difficulty is medium, player 3 is hard, and player 4 is expert (easy difficulty is ignored since that is 
just randomly chosen cards, which is hard to test).
* Read the description in the top of AIPlayer.java to understand how the different difficulties work. 


### How to test:
1. Press play in the lower left corner
2. Choose "ai_test_map" in the dropdrown list
3. Press stage 1
4. Choose 5 cards(doesn't matter which) and press lock in.
5. For phase 1:
    * Player 2 thinks the best path is straight ahead and chooses Move 2 since that gets him closest to the next goal 
    for that phase
    * Player 3 thinks also the best path is straight ahead, but chooses Move 1 since he understands he needs to save 
    the Move 2 card for later to be able to cross the conveyorbelt
    * Player 4 knows the path ahead is a dead end, and the best path is to go back and around the walls
     and will therefore chooses Backup.
6. Press ENTER
7. For phase 2:
    * Player 2 is now stuck and chooses Rotate Left or Rotate Right since they are the best options in the current situation.
       (He will not choose Move 1 since that will put him in the same position and direction at the end of the phase
       after conveyorbelts move and therefore believes that card is useless). 
    * Player 3 chooses Move 1 again for the same reason as in phase 1.
    * Player 4 chooses Rotate left since he needs to go left to be able to get closer to the goal.
8. Press ENTER
9. For phase 3:
    * Player 2 is still stuck and will choose Rotate Left or Rotate Right
    * Player 3 chooses Move 2 to cross the conveyorbelt and not be pushed back at the end of phase
    * Player 4 chooses Move 1 to get closer to the goal.
10. Press ENTER
11. For phase 4:
    * Player 2 is still stuck and will choose Rotate Left or Rotate Right
    * Player 3 has no more movement cards to choose from so he chooses Rotate Left or Rotate Right
    * Player 4 chooses Rotate Right to change his direction towards the next goal flag.
12. Press Enter
13. For phase 5. 
    * Player 2 is still stuck and will choose U Turn
    * Player 3 has still no more movement cards to choose from so he chooses Rotate Left or Rotate Right
    * Player 4 chooses Move 2 since that will get him closest to the goal. 
 

