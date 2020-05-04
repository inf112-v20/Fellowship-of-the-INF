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

* The difficulties differ in how the AI players chooses their cards from the player hand deck for the next round:
    * Easy chooses random cards
    * Medium is greedy and will choose the best card for each phase
    * Hard chooses the cards that will get them to the position that has the least distance to the next goal flag
    (the distance is an estimate)
    * Expert chooses the cards that will get them to the position that has the least distance to the next goal flag
    (the distance is the actual distance)
* When checking how good a card is, it checks what position and direction the player will end up on at the end of the 
phase (after conveyorbelts, cogs etc. are activated) by using that car. It will not check if it will be pushed or not.
The card is given a score by how good that position and direction is relative to the next goal flag position. 
Players at every difficulty will avoid picking cards that will kill them (e.g. walking into pits) unless they don't have
any other option.
* Player 2 difficulty is medium, player 3 is hard, and player 4 is expert (easy difficulty is ignored since that is 
just randomly chosen cards, which is hard to test).


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
 

