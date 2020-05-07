# ObligatoriskOppgave4

## Part 1: Team and Project
### Roles
We decided to continue the role delegation from earlier itterations as we felt the different roles where working, and did not feel the need to change anything.

### Project methodology
We have mainly followed the XP-methodology throughout the project, and it has worked quite well for us. Points from the methodology that were especially useful were:
* Iterations
* Pair-programming
* Continuous code review
* Reflections on previous iteration

The most useful aspect was definitely the use of iterations, which helped us to plan the project, distribute work, ensure a level of quality and urge us to reflect upon previous work.


### Reflection on our communcation
Early on in the project we put too much focus on keeping everyone up to date with everything. Meetings lasted far too long, as everyone had to talk about everything they had done since last time. We could have saved ourselves a lot of time and been a lot more productive early on if we had understood that everyone doesn’t need to know everything. A lot of things that were said in meetings should have been written in the documentation instead.


### Project retrospective
Overall we are satisfied with the results of the project. We have all become better programmers and communicators (project-wise) as a result of the work. As a result we have reached a list of things we’d do differently if we were to start a new project:
 * More thorough planning
 * Use effective working practices earlier:
    *	Pair-programming
    *	Role distribution 
    *	Codacy
    *	Reflections on previous work
    *	Proper user stories
    *	Continuous testing
 *	Better abstraction (Rules-class, Cleanup-class, separation between Grid and Game Map)
 *	Better use of Project board, issue tracking system


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
  ![alt text](https://github.com/inf112-v20/Fellowship-of-the-INF/blob/master/Deliverables/ObligatoryAssignment4/TestMapSelectionScreen_Screenshot.png "Screen shot")
  

### Meeting logs
[Meeting logs can be found in a seperate MD file called MeetingLogsIteration4.md](https://github.com/inf112-v20/Fellowship-of-the-INF/blob/master/Deliverables/ObligatoryAssignment4/MeetingLogsIteration4.md)

### Project board screenshot
![alt text](https://github.com/inf112-v20/Fellowship-of-the-INF/blob/master/Deliverables/ObligatoryAssignment4/projectBoardIteration4.png)

### Group comunication and dynamics compared to the beginning
As mentioned earlier, we felt the effects of the lockdown of the university, and many of the changes to the group's communication and workflow has been due to this event. Luckily for us had we already been mostly working alone, having 2 meetings a week where work was handed out to individuals or pairs, and the work being done between these meetings. It was not hard to move this to a remote work flow which worked much the same way. The largest difference being the meetings moving online, rather than face to face. This created some issues due to internet connection, and trying to figure out how to propperly convey information and explain concepts to each other.

What we did end up experiencing as time went by was a decrease in time spent in the actual meetings, at the same time as we felt we got more things done during them, going from above 2 hours per meeting, to 
consistently reaching about 1 hour per meeting. This might be due to us being less able to branch out into different conversations during a meeting. Anything that did not need to include everyone would be handled outside of the meeting between the two parts. We also had Johanna, the group leader, have a plan before each meeting, driving it forward. During the meeting would Lena be taking notes in a google document to have an accurate log from the meeting, which group members could check afterwards to check what was said. These logs can now be founf in the linked .md file above.

## Part 2: Requirements
### Requirements for Fourth Iteration
Due to being unable to meet and play multiplayer over LAN, our team decided to instead make an AI to play against. The remaining MVP's for the game, in addition to some "nice to haves".
---

### Have multiplayer against computer players which can have different levels of difficulty
**Usecase**: The user of the game wants to able to play against other players to make the game competitive.

**Acceptance criteria**
* The human player should be able to see how other players are doing in the game and what they are doing
   * They should be able to see which card other players are playing for a phase.
   * They should be able other players stats (current lives, damage, flags visited, spawnpoint). 
* You should be able to choose the level of difficulty before the game starts (all computer players are set to the same difficulty).
* Have at least 3 or more difficulties to choose between.
* The hardest difficulty should be close to "perfect", i.e., always choosing the best cards.
* Computer players should be able to evaluate their current situation and can choose to powerdown if they think they should do so.

**Tasks to complete**

* Implement 4 difficulties: easy, medium, hard, expert.
* Implement a dropdown list where you can choose the difficulty in the playerselection screen.
* Implement different methods for selecting cards for AI players.
* Implement a method that figures out how good a card is for a given phase.
* Implement a method that figures out how good 5 selected cards are for a round.
* Implement a method that find the best card/cards based on how good they are. 
* Implement a way to find out how far away the current goal/flag is.
* Implement a way to find out what position and direction a player would end up at the end of the phase after using a card.
* Implement a method that evaluates if they should powerdown or not based on how much damage they have taken. 
* Implement a scoreboard to keep track of other players stats (already implemented in previous iteration)
* Implement a gamelog that shows the cards played for a phase (already implemented in previous iteration)
* Implement a visual icon in the gamescreen for each player's spawnpoint that updates when the spawnpoint updates. 

---

### Update respawning to match the rules of RoboRally
**Usecase**: The user wants to play RoboRally with the proper ruleset.

**Acceptance criteria**
* Players should respawn at the end of the round if they died during it if they have more lives left.
* If several players died during a round then they should respawn in the order they died in.
* A player should be able to choose which direction they respawn in.
* A player should be able to choose an adjacent position (orthogonal or diagonal) if their spawnpoint is occupied by another player.
   * That position must be a valid position, i.e., it isn't occupied and it isn't an abyss or outside of the map.
* AI players should have some small differences in how they choose which direction/position they respawn in depending on their difficulty.

**Tasks to complete**
* Create a list of dead players that keep track of the order they died in.
* Add an instructional text for the human player when they are respawning to make them understand what to do.
* Visually show which direction you are respawning in before you have actually respawned.
* Show which position are available to respawn in (highligth them). 
* Implement a way for the human player to choose the direction to respawn in (keyboard input).
* Implement a way for the human player to choose a position to respawn in (buttons on gamescreen).
* Implement a method that figures out which direction/position is the best to respawn in for AI players for a given difficulty. 

---

### Countdown timer when there is only one player left choosing cards
**Usecase**: The user wants a timer for the last player choosing cards so they don't have to wait forever and put pressure on players that use a long time on choosing cards. 

**Acceptance criteria**
* The timer should only start when there is only one player left picking cards (this made more sense when we thought we were implementing LAN play as well. Since we dropped that and have AI players instead, which use less than a second to pick their cards, the timer just starts immediately when a new round starts. The ruleset says the timer should be 30s, but to account for this we upped it to 60s).
* The timer should stop if the last player locks in before that timer has run out.
* If the timer runs out then the open slots in the last player's register should be filled with random cards from the playerhand and be locked in automatically. The first phase should then start. 
* The timer should also stop when it runs out.
* The timer should be visible in the UI.

**Tasks to complete**
* Implement a list of players that are locked in and ready.
* Implement a way to draw a timer in the UI that updates each second and can be removed when not used.
* Implement a method that picks random cards from the playerhand and places them in open slots in the register.
* Implement a check for the last player if they have locked in after the timer has started. 

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

### Show the board lasers when they are active
**Usecase**: The user of the game wants to be able to see the board lasers when they are active, so that they can see when a robot gets hit by a laser.


**Acceptance criteria**
* Board lasers are:
   * only shown when active
   * shown with the correct cell (horizontal lasers are drawn as horizontal lasers etc.)
   * not drawn places that the laser beam does not reach (fex if blocked by a robot)

**Tasks to complete**

* Implement graphics for the board lasers so they are seen.
* Integrate the display of board lasers with the display of robot lasers, so they happen at the same time.
* Implement block functionality (the lasers can be blocked).

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

### Win/Lose condition
**Usecase**: The user of the game would like to know when the game has ended in order to know when to exit the application or start anew.

**Acceptance criteria**
* When all robots are dead, a popoup should tell the player this is the case.
* When the human controlled robot is dead, a popup should tell the player this is the case.
* When any player has won, a popup should tell the player this is the case
* On all the above, the player should be able to exit the application or start again from the main menu.

**Tasks to complete**
* Check when all players are dead.
* Check if the human player (player 1) is dead.
* Check if any player has visited all the flags.
* Create dialog box in accordance to which of the conditions has been fulfilled.

---

### Choose number of players
**Usecase**: The user of the game would like to choose how many players should be in the game in order to maximize the fun of the user.

**Acceptance criteria**
* After hitting play on the menu screen, another screen with different numbered buttons should appear.
* Clicking one of these buttons should make it so that the game only spawns in as many players as the button indicated.
* All maps should support 2 to 8 players.

**Tasks to complete**
* Make new sprites for all the buttons needed.
* Place down buttons on the new screen.
* Give the buttons a function, including sending amount of players to the map selection screen.

---

### Power Down
**Usecase**: The players can choose to power down for the round, they cannot chose cards, but repair all damage

**Acceptance criteria**
* During the setup of a round can a player decide to do power down instead of using cards
* If a player is in power down, it is healed for all it’s damage, but do not take any moves
* If a player is in powerdown, it should be registered as ready to start a round, but not be called upon to use cards
* UI: The players in power down should also be shown in the UI for the rounds and phases


**Tasks to complete**
* Implement a variable that is true when the player is in powerdown
* Make sure that the players in power down do not get called when the game retrieves cards for the rounds and phases
* Make a button for power down on UI
* Make the UI for the phases also show the players in power down

---

### Music/Sound effects
**Usecase**: The should be given more feedback through sound effects, and some polish through background music

**Acceptance criteria**
* The music should be fitting for the game, and play both duriing menues and the game it self
* The music should loop untill the game is done
* When the user presses a button in the menues they get feedback through sound
* When the player clicks on a card they get feedback through a sound effect
* When right clicking on a card, there is a different sound to differentiate between right and left clicking
* When lasers shoot, play a laser sound


**Tasks to complete**
* Find royalty free music and sound that fit the game
* Implement a looping background music
* Implement sound effects for selecting and deselecting cards
* Implement sound effects for using buttons in menues
* Implement sound effects for lasers

---

### Other known bugs
* When you press enter instead of clicking lock in to start a round, the cards executed in a phase arent shown properly in the GUI.



## Part 3: Code

### UML
![altText](https://github.com/inf112-v20/Fellowship-of-the-INF/blob/master/Deliverables/ObligatoryAssignment4/UML/SimplifiedUML.png "UML")
You can find more UML's in the UML folder in ObligatoryAssigment4.

### How to run the program
* Ensure maven install and build is complete. Then, run Main. A Menu Screen will pop up. Press the play button to start the game, and then the Stage 1 button from the stage selection screen.

### How to use the game
* You can select cards by left clicking them, and move them back by right clicking them. Once you have selected five cards, you can start a round by pressing the lock in button.
* For the next phase to execute, press ENTER, or to let the phases run automatically you can press P to toggle autorun on/off (default is off).
* You can move robot 1 around using UP, DOWN, LEFT RIGHT, SPACEBAR. LEFT and RIGHT rotate the robot to the left and right, while SPACEBAR rotates the robot 180*.  UP and DOWN move the robot forward and backward.
* You can move robot 2 around by using W, S, A, D, Z. They function in the same way as UP, DOWN, LEFT, RIGHT, SPACEBAR respectively.
* If robot 1 is on a conveyorbelt you can move it 1 cell in the direction of the conveyorbelt by pressing BACKSPACE.
* You can press L to make player 1 shoot a laser and activate all the board lasers.
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
  * You lose lives if you go off the board or if you walk into an abyss or if you have 10 or more damage. 
  * You have 3 lives before you die

### How to run automatic tests
* Go to the tests package, sideclick on the java package and select /Run 'All Tests'/

### How to run manual tests
* Read the md files in the ManualTests package in Test for instructions.
