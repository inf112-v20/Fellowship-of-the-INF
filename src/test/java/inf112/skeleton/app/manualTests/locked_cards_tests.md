# Manual tests for congs
#### Setup 
* Comment out line 77 in Phase.java if it isn't already.  
* Run Main.
* Press the Play button in the menu screen.
* Press the Stage 1 button in the stage selection screen.


### Tests
#### Test1 for locking cards and unlocking cards
* Complete setup.
* Pick any 5 cards that won't kill robot 1 and lock in.
* Press 1 on your keyboard to take damage so that you have at least 5 or more damage.
* Press 2 to remove damage.
* Assert: The cards are locked(red outline) from right to left when having 5 or more damage.
A card is unlocked(white outline) from left to right when removing 1 damage. 

#### Test2 for locking cards
* Complete setup.
* Pick any 5 cards that won't kill robot 1 and lock in.
* Press 1 on your keyboard to take damage so that you have at least 5 or more damage so that you have some locked cards.
* Press ENTER five times to play through all the phases and start the next round.  
* Assert: The locked cards from last round are still in the register at the correct position. 
You can't remove them (i.e rightclick them).

#### Test3 for locking and unlocking cards
* Do the same as test 2. 
* Select the given cards for round 2 so that you have 5 selected cards and press lock in.
* Press 2 to remove damage so that all the selected cards are unlocked
* Press ENTER five times to play through all the phases and start the next round.
* Assert: At the start of round 3 there should be no locked cards and register is empty.   





