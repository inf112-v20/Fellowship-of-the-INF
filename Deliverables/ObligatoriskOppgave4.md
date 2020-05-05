# ObligatoriskOppgave4

## Part 1: Team and Project
### Roles
We decided to continue the role delegation from earlier itterations as we felt the different roles where working, and did not feel the need to change anything.

### Project methodology and retrospective

### Problems we've had
* The current situation in the world has made it challenging working together as a team, when we have been unable to see each other in person at all during this itteration. This has led to what may be less than ideal comunication, compared to what we would have been able to do otherwise. Despite this, we feel that we have been able to do the best in a bad situation, using the tools that using discord has provided us, by being able to, among other things, share a live feed of our own screen to the rest of the group, to show our code, and explain what we have been doing.
* In the code it self, we have been experiencing bloating in certain classes, the player.java class among them. It works at the moment, but idealy it would have been devided up into at least two classes, where one of them would take care of rules, while the other would handle player information and behavior.

### How we tackled the points of improvement
* Actually reflect upon the previous iteration before planning the next one.
  * We discussed this in the first meeting of iteration 4 and reached a few key points:
     * It is more difficult to work with the code base now, as it is so large. Therefore clean and well-documented code is even more important now.
     * Testing should be done continuously throughout an iteration. To make this possible, manual tests can't be dependent on commenting in/out lines of code.
     * Communication has become more difficult now that the university is closed. Being reachable on Discord and being present at meetings is important.
* Plan meetings better, so that everyone is available at the agreed upon time.
  * At the end of each meeting we all agree on the time of the next meeting (usually at 14.00 on Mondays and Thursdays).
* Testing should be done either before or immediately after implementation, not toward the end of an iteration.
  * In order for this to be possible we had to remove our very impracticle system of commenting out/in lines of code to run certian tests. We created a practical UI where the appropriate test map could be selected, and made it simple for developers to add a test map to the list. With this new system in place, manual testing was easy to do before or immediately after implementation.
  ![alt text](https://github.com/inf112-v20/Fellowship-of-the-INF/blob/master/Deliverables/TestMapSelectionScreen_Screenshot.png "Screen shot")
  

### Communication and group dynamics


### Meeting logs
[Meeting logs can be found in a seperate MD file called MeetingLogsIteration4.md] (https://github.com/inf112-v20/Fellowship-of-the-INF/blob/master/Deliverables/MeetingLogsIteration4.md)

## Part 2: Requirements
### Requirements for Fourth Iteration
Due to being unable to meet and play multiplayer over LAN, our team decided to instead make an AI to play against. The remaining MVP's for the game, in addition to some "nice to haves".
---

### Have pushers on the board
**Usecase**: The user of the game wants to be able to have pushers on the board, as they are part of the board elements in a roborally game.


**Acceptance criteria**
* Pushers are only active when they are supposed to be:
  * Either a pusher is active on odd, numbered phases or on even numbered phases.
* The user must be able to see the difference between the types of pushers, so that one can tell when a pusher will be active.
* Pushers work like walls, so a player should not be able to walk through a pusher.
* Pushers should push players in the direction the pusher is facing.
* Pushers should not be able to push players that cannot be pushed in the direction of the push (fex if there is a wall blocking the way)
* A player pushed by a pusher should be able to push other players that are in the way.
* Pushers should only be active during their designated time in the phase.

**Tasks to complete**

* Implement graphics for the pushers.
* Implement push functionality of pushers.
* Implement activation time (only during spesific time in phase, and only when even/odd phase number).
* Implement wall-functionality, so that players cannot walk through pushers.

---

### Functional repair sites (single-wrench fields)
**Usecase**: The user of the game would like the robots standing on single-wrench repair fields to discard damage
tokens in accordance to the rules of Roborally.


**Acceptance criteria**

* A robot standing on a single-wrench field at the end of a **round** (after 5 phases) discards 1 damage token.
* If a robot does not currently have any damage tokens, then nothing happens (damage can't be negative).
* UI has to be updated accordingly.


**Tasks to complete**

* Register when a robot is standing on a singe-wrench field at the end of a round.
* Discard damage tokens according to ruleset.
* Update UI (damage tokens).

---

### Other known bugs
* When you press enter instead of clicking lock in to start a round, the cards executed in a phase arent shown properly in the GUI.



## Part 3: Code

### UML

### How to run the program
* Ensure maven install and build is complete. Then, run Main. A Menu Screen will pop up. Press the play button to start the game, and then the Stage 1 button from the stage selection screen.

### How to use the game
* You can select cards by left clicking them, and move them back by right clicking them. Once you have selected five cards, you can start a round by pressing the lock in button.
* For the next phase to execute, press ENTER.
* You can move robot 1 around using UP, DOWN, LEFT RIGHT, SPACEBAR. LEFT and RIGHT rotate the robot to the left and right, while SPACEBAR rotates the robot 180*.  UP and DOWN move the robot forward and backward.
* You can move robot 2 around by using W, S, A, D, Z. They function in the same way as UP, DOWN, LEFT, RIGHT, SPACEBAR respectively.
* If robot 1 is on a conveyorbelt (not express) you can move it 1 cell in the direction of the conveyorbelt by pressing BACKSPACE.
   * Will not handle collision from the conveyorbelt move. 
* You can hold down TAB to see stats of all the players in the game.
* You can press ESC to quickly resize the game to a small window. 
* Pressing the buttons 1-6 changes some values of the player:
   * 1: Add 1 damage
   * 2: Remove 1 damage
   * 3: Add 1 life
   * 4: Remove 1 life
   * 5: Add 1 flag
   * 6: Remove 1 flag 

* Things to note:
  * You lose lives if you go off the board or if you walk into an abyss
  * You have 3 lives before you die
  * If you die you need to restart the program to play again

### How to run automatic tests
* Go to the tests package, sideclick on the java package and select /Run 'All Tests'/

### How to run manual tests
* Read the md files in the ManualTests package in Test for instructions.
