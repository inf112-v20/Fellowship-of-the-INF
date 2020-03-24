# ObligatoriskOppgave2

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

####Show muliple robots on the board
**Usecase**
hihih

**Acceptance cirteria**
sddsd
**Tasks to complete**
sds



    Vise spillbrett
    Vise en spiller på brettet
        Spiller vises bare posisjonen den skal være på, når den skal være der
    Vise flere spillere
        Det er mulig å se forskjell på spillere
        Spillere skal ikke vises hvis de ikke er med i spillet
    Kunne programmere robot
        Man kan velge fra tildelt kort
    Ha “gjenstander” på brettet
    Ha en roborally kortstokk
    Dele ut kort
        Et kort kan ikke deles ut til flere spillere samtidig
        Spillere for utdelt færre kort om de har tilsvarende skade
    Låse kort
    Win / lose condition
        Spillet avsluttes hvis noen har besøkt alle flaggene i riktig rekkefølge
        Spillet avsluttes hvis alle spillerne har mistet alle livene sine
    Funksjonelle lasere
        Vise lasere på slutten av hver fase
        Stoppe lasere ved vegger
        Gi skade til roboter som blir truffet av laser
    La roboter skyte laser
        Laser skal skytes fra “hodet” til roboten i retning roboten står
        Laser skytes fra roboten på slutten av hver fase
    Plassere flagg
        Roboter kan flytte andre roboter hvis de står i veien
        Men ikke hvis vegger vil forhindre det
    Låse kort ut i fra skade tatt
    Oppdatering av backup/respawnplass
    Besøke flagg
        Må besøkes i riktig rekkefølge
    Powerdown
        Mulighet for å annonsere powerdown
        Være i powerdown
        Komme tilbake igjen fra powerdown


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
