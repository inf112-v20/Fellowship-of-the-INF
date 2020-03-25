# ObligatoriskOppgave3

## Part 1:Team and Project
### Roles
The roles we delegated at the last iteration have worked quite well. The Code Integrity and Tidiness role has become obsolete as we now use Codacy to improve code quality.
In the first iteration de decided to follow a 

### Project methodology and retrospective
In the first iteration we decided to use XP, with an emphasis on good communication. These are some of the main points we wanted to implement:
* **Iterations** - *At the beginning of each iteration we agree on which points of the MVP we will complete in this iteration.* This has been extreemely helpful when planning the project, as everyone then has a clear idea of what needs to be done. In this iteration we had to split the remaining MVP over this iteration and the next one, as the next is the final iteration. We feel like we did a good job in planning this iteration, but did not spend enough time reviewing the previous one.
* **Work delegation** - *Work delegation will happen mainly at meetings, and if someone gets done with their job quickly they can pick an undelegated card from the TODO column in the project board.* Agreeing on what each person is going to do for the next meeting has been very helpful, as when job delegation happens at meetings everone knows what everyone is working on. This prevents team members from working on overlapping elements of the project, and means that we always know who made what when we need to ask someone how something works.
* **Project board** - *The project board will have a column for backlog, TODO, In Progress, Testing and Review. We start a new project board for each iteration.* We realised that starting a new project board for each iteration was not a good idea. It gives us a better overview if the same project board is maintained throughout the entire project. So, at the beginning of this iteration we simply made the project board for the second iteration into the project board for the entire project and worked with it from there. While the project board has been useful for getting an overview of the tasks that need to be done, it has not really been used for delegation of tasks as this mainly happens at meetings. Additionally we have not been very good at updating the project board once tasks have been completed. This is something we will work on in the next iteration.
* **Branches** - *We only push to the main branch when we complete an iteration, and implementations that have been reviewed by the team at a meeting are merged into the development branch. While working on an implementation, each person works on their own branch.* We have relaxed parts of the review requirements, as waiting to push bugfixes and refactoring only slowed down our working process. We found it more important that the development branch was as clean as it could be, so if a bugfix or refactoring did not heavily influence the game's functionality or readability, team members pushed these changes and notified the rest of the team on discord. Apart from this change, our original idea of how to use branches has worked well for the entire team.
* **Pair programming** - *We will use pair programming to improve code quality and to build the skills of team members.* We did some pair programming at the beginning of this iteration, but after the university closed this stopped. As we saw great benifits from pair programming we hope to find a solution for how to do this remotely in the next iteration.
* **Communication is key** - We have from the beginnig emphasised the importance of good communication, and this has of course been affected by the closing of the university. See further comments in the "Communicaton" section.

### Points of improvement
HERE WE ARE SUPPOSED TO CHOOSE THREE POINTS OF IMPROVEMENT MENTIONED IN THE RETROSPECTIVE. THESE NEED TO BE AGREED UPON IN THE MEETING ON THURSDAY.
* Start doing pair programming again, remote of course
* Get better at using the project board
* Actually reflect upon the previous iteration before planning the next one.
* Plan meetings better, so that everyone is available at the agreed upon time.
* Testing should be done either before or immediately after implementation, not toward the end of an iteration.

### Communication and group dynamics
Since we no longer have physical meetings the quality of the communication has decreased, but the meetings have become somewhat more focused. During online meetings people make more of an effort to make the communication clear and consise as only one person can make themselves hear at a time. It is also harder for memebers to branch off into their own conversation, so everyone is part of the same conversation during the entire meeting. Unfortunately some members have problems with the internet quality in their home, and therefore sometimes have difficulties in participating in the online meetings.


### Meeting logs
Meeting logs can be found in a seperate MD file called MeetingLogsIteration3.


## Part 2: Requirements
### Requirements for Third Iteration
These are the MVP's we picked to complete in this iteration:

---

### Have computer controlled robots moving around on the board
**Usecase**: The user of the game would like to have computer controlled robots in the game, so that they have something to play against.


**Acceptance criteria**

* The user must be able to see the other robots on the board.
* The user must be able to differentiate the robots on the board.
* The computer-robots must use the first five cards of the hand they are handed each round. 
* Robots cannot occupy the same position on the board.


**Tasks to complete**

* Implement graphics for the robots
* Implement spawn points for the computer-robots.
* Implement selection of cards for the computer-robots.
* Implement the section of a phase in which the robots move.

---

### Lock cards based on damage obtained
**Usecase**: The user of the game wants the selected cards of a player to be locked based on obtained damage in accordance with the rules of Roborally.


**Acceptance criteria**

* Lock cards from right to left only after taking 5 or more damage.
* Unlock cards from left to right when removing damage. 
* The user must be able to see which cards are locked.
* The user must be unable to remove or unselect the locked cards.


**Tasks to complete**

* Implement graphics for locked cards. 
* Disable buttons for locked cards at round start.
* Prevent locked cards from being sent back to the gamedeck at round end. 
---

###  Robots are moved when standing on a conveyor belt at the end of a phase
**Usecase**: The user of the game would like the robots standing on conveyor belts to be moved in accordance of the rules of Roborally.


**Acceptance criteria**
* Robots standing on an express conveyor belt during the *"conveyor belts move"* part of the phase, will be moved one cell in the direction of the conveyor belt.
* Robots still standing on an express conveyor belt will once again be moved one cell, and at the same time robots standing on normal conveyor belt will move one cell in the direction of the conveyor belt. 
* A robot standing on a corner piece of a conveyor belt, will be rotated 90* in addition to being moved.
* The user can see the robots being moved by the conveyor belts.
* Conveyorbelts should not move a robot if it will crash into another robot.


**Tasks to complete**

* Create a case for every type of conveyorbelt in the tileset file so that every type can be used.
* Determine if a player is standing on a conveyorbelt.
* Determine for some types of conveyorbelts if it should turn a robot left or right.
* Implement a way to check if a robot is going to end up in the same cell as an other robot from the movement of the conveyorbelt.
   * Don't move any robots that will do this
* Create a recursive call for moving robots standing in front of a robot on a conveyorbelt, but only if the robot in front is moving on the same path. Prevents the robot in the back crashing into the robot in front before that robot has had a chanced to be moved. 
* Update visually everytime someone is moved by a conveyorbelt (should change to be updated simultaneously).



---

### Robots are rotated when standing on cogs at the end of a phase
**Usecase**: The user of the game would like the robots standing on cogs to be rotated in accordance of the rules of Roborally.


**Acceptance criteria**

* A robot standing on a clockwise cog during the *"cogs rotate"* part of the phase, will be rotated 90* clockwise.
* A robot standing on an anti-clockwise cog during the *"cogs rotate"* part of the phase, will be rotated 90* anti-clockwise.
* The user needs to see that the robot has been rotated.


**Tasks to complete**

* Determine if a robot is standing on a cog
* Rotate player in the correct direction
* Update visually everytime someone is rotated by cog (should change to be updated simultaneously).


---

### Have functional lasers on the board
**Usecase**: The user of the game would like robots to obtain one damage, in accordance to the rules of Roborally, when standing in the path of lasers.


**Acceptance criteria**

* Give one damage to a robot when it is standing in the way of a single-rayed laser during the *"lasers fire"* part of the phase. 
*  Give two damage to a robot when it is standing in the way of a double-rayed laser during the *"lasers fire"* part of the phase. 
* Laser rays should be stopped by a wall or a player standing in the way, so that multiple robots cannot be
damaged by the same laser.
* Laser rays should only be visible during the *"lasers fire"* part of the phase, i.e. that the lasers are 
not activated during any other sequence.


**Tasks to complete**

* Register when a robot is standing in the path of a laser
* Robots that are standing in the path of a laser takes damage accordingly, and UI is updated accordingly (in regards
to damage tokens).
* Intercept/"cut off" the rest of the laser when it hits a robot or wall.
* Implement visual queues for lasers during laser sequence in a phase.

---

### Robots can push eachother
**Usecase**: When the user of the board programms their robot to move into the position of another robot, the other robot should be pushed to the neighbouring cell.


**Acceptance criteria**

* It should not be possible to push a player that is being prevented from being moved in a direction by a wall.
* The pushed robot should be pushed in the direction the pushing robot is moving.
* A robot can only be pushed by another robot that is directly next to it, without any hindering barriers inbetween.
* A robot that is pushed into the position of another robot, should push that next robot too. Ie if many robots are lined up, then is a robot is pushed on one end of the line, all the robots in the path of the push are pushed too.
* It should be possible to push a robot 
    * off the board.
    * into an abyss.
    * in power down mode. (Power down mode will be implemented in the next iteration).
    * onto, and off, a conveyor belt.
    * across multiple cells, ie if the pusher uses a MOVE3 card, then it should be possible to push another robot three spaces.


**Tasks to complete**

* Implement a type of recursive function that pushes all robot standing in the way of a pushed robot until there are no more robots in the chain to push.
* Implement the graphics of the push so that the user can see that a robot is being pushed.
* Restructure how backend changes to the board are made. A robot pushing another cannot occupy the position of the robot being pushed until that robot has been moved to it's next position.
* Resturcture how frontend changes to the board are made. The pushing robot and the pushed robot need to move at the same time so that it is clear that one is pushing the other.

**Known related bugs**: When moving near a player that has died, a stack overflow error occurs. This bug can be recreated by pressing the UP key once after completing the "Test for pushing robot into abyss" in the maunal tests for robot pushing.



---

### Delay between moves being executed by robots
**Usecase**: The user would like to see the moves done by the robots happen one after the other, so that it is clear what order the moves were done in.


**Acceptance criteria**

* The moves shown should correspond to the moves that were executed in the backend.
* The moves should be shown in correct order.
* The delay between moves should be about one second.
* Moves that happen simultaneously should be shown simultaneously.


**Tasks to complete**

* Create a system that generates a list of moves that need to be shown graphically.
    * A Move object that contains information about a robot before and after a move is executed:
    * A MovesToExetuteSimultaneously object, which is a type of Array List that contains Move objects that have to be executed simultaneously in the frontend.
    * A type of Queue that ensures the MovesToExetuteSimultaneously objects are executed in the order they are generated.
* Add a delay when executing the front-end moves.
* Integrate this new way to show moves with the existing system for showing keyboard controlled moves.

---

### MVP...
**Usecase**: 


**Acceptance criteria**

* 


**Tasks to complete**

* 





### How to reach these requirements:


### Other known bugs
* When you press enter instead of clicking lock in to start a round, the cards executed in a phase arent shown properly in the GUI.
* Pieces on the conveyorbelt do not move simlutaneously.
* A robot standing on a conveyorbeltpiece that is also a laserpiece will not be moved by the conveyorbelt.


## Deloppgave3: Code

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
* Pressing the buttons 1-6 changes some values of the player (unsure if NUMPAD numbers works):
   * 1: Add 1 damage
   * 2: Remove 1 damage
   * 3: Add 1 life
   * 4: Remove 1 life
   * 5: Add 1 flag
   * 6: Remove 1 flag 

* Things to note:
  * You die if you go off the board or if you walk into an abyss
  * If you die you need to restart the program to play again

### How to run automatic tests
* Go to the tests package, sideclick on the java package and select /Run 'All Tests'/

### How to run manual tests
* Read the md files in the ManualTests package in Test for instructions.
