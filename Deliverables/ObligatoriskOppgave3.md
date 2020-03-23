# ObligatoriskOppgave2

## Part 1:Team and Project
###Roles
The roles we delegated at the last iteration have worked quite well. The Code Integrity and Tidiness role has become obsolete as we now use Codacy to improve code quality.
In the first iteration de decided to follow a 

###Project methodology and retrospective
In the first iteration we decided to use XP, with an emphasis on good communication. These are some of the main points we wanted to implement:
* **Iterations** - *At the beginning of each iteration we agree on which points of the MVP we will complete in this iteration.* This has been extreemely helpful when planning the project, as everyone then has a clear idea of what needs to be done. In this iteration we had to split the remaining MVP over this iteration and the next one, as the next is the final iteration.
* **Work delegation** - *Work delegation will happen mainly at meetings, and if someone gets done with their job quickly they can pick an undelegated card from the TODO column in the project board.* Agreeing on what each person is going to do for the next meeting has been very helpful, as when job delegation happens at meetings everone knows what everyone is working on. This prevents team members from working on overlapping elements of the project, and means that we always know who made what when we need to ask someone how something works.
* **Project board** - *The project board will have a column for backlog, TODO, In Progress, Testing and Review. We start a new project board for each iteration." We realised that starting a new project board for each iteration was not a good idea. It gives us a better overview if the same project board is maintained throughout the entire project. So, at the beginning of this iteration we simply made the project board for the second iteration into the project board for the entire project and worked with it from there. While the project board has been useful for getting an overview of the tasks that need to be done, it has not really been used for delegation of tasks as this mainly happens at meetings.
* **Branches** - *We only push to the main branch when we complete an iteration, and implementations that have been reviewed by the team at a meeting are pushed to the development branch. While working on an implementation, each person works on their own branch.* 

###Points of improvement

###Kommunication

###Meeting logs
Meeting logs can be found in a seperate MD file called MeeintLogsIteration3.


## Part 2: Requirements
### Requirements for Second Iteration
* Move player by choosing cards
* Collision with walls
* Death when falling off board

### How to reach these requirements:


### Known bugs
* When you press enter instead og clicking lock in to start a round, the cards executed in a phase arent shown properly in the GUI.
* Pieces on the conveyorbelt do not move simlutaneously


## Deloppgave3: Code

### How to run the program
* Ensure maven install and build is complete. Then, run Main. A Menu Screen will pop up. Press the play button to start the game.
* You can select cards by left clicking them, and move them back by right clicking them. Once you have selected five cards, you can execute them by pressing the lock in button.
* You can also move the player around using UP, DOWN, LEFT RIGHT. 
* Things to note:
  * The player has a direction (facing north at start), but the game screen does not show it.
  * When using the arrow keys, you always turn in the direction you move. This is not the case when using the cards (for example the MoveBack card)
  * You die if you go off the board or if you walk into an abyss
  * If you die you need to restart the program to play again

### How to run automatic tests
* Go the tests package, sideclick on the java package and select /Run 'All Tests'/

### How to run manual tests
* Read the md file in the ManualTests package in Test for instructions.
