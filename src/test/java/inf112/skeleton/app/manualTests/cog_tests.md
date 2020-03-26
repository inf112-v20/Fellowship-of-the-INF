# Manual tests for cogs
####Setup 
* Go to Phase.java and uncomment line 77 to make robot 1 stand still during the phase to making testing easier
* Run Main.
* Press the Play button in the menu screen.
* Press the Stage 1 button in the stage selection screen.


###Tests
#### Test for rotating robot on a clockwise turning cog
* Complete setup.
* Move robot 1 onto a clockwise turning cog (green arrows).
* Pick any 5 cards and lock in, or press ENTER if you have already have locked in cards. 
* Assert: robot 1 is rotated 90* to the right at the end of the phase.

#### Test for rotating robot on an anticlockwise turning cog
* Complete setup.
* Move robot 1 onto a anticlockwise turning cog (red arrows)
* Pick any 5 cards and lock in, or press ENTER if you have already have locked in cards. 
* Assert: robot 1 is rotated 90* to the left at the end of the phase. 

