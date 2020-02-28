# ObligatoriskOppgave2

## Deloppgave1:Project and project structure
For the first sprint had we chosen roles for just two of our members, as we did not think it was necesarry for more at that point. For this second sprint have we given out more roles, as we see it as necesary at this point.

### Roles
* Johanna: Group leader
* Martin: Contact Manager, Copyright Manager
* Erlend: Test Manager
* Henrik: Code Integrity and Tidiness
* Lena: Markdown Writer, Rules Lawyer

### Role Description
* Group leader: Keeps an overview of the project and the work of all the other members of the group.
* Contact Manager: Main source of communication between the group, the professor, and TAs.
* Copyright Manager: Keeps track of copyrighted material that we use, and that we do not infringe on anything.
* Test Manager: Makes sure that tests are made
* Code Integrity and Tidiness: Makes sure that the code that is written is readable and clean.
* Markdown Writer: Main writer of the Markdown documents, and makes sure that eveything is written for the assignments.
* Rules Lawyer: Main responsible for knowing the rules of the original RoboRally game.

### Retroperspective
In the first itteration were there little actual coding, which lead to an uneven spread of commits among the members. For this itteration have we done a lot more coding towards the mvp.
We have also had to sometimes move the extra meeting based on different reasons, such as waiting for more information, or other external reasons, f.ex. the moving of lectures.
There is some insecurities among members, where they do not know or understand what some of the others are doing. This might be solved by creating a comprehensive uml, or maybe more pair programming. 

### Meeting Report

#### 13.02.2020
__Attendence__: Everyone

__Summary__: 
* Researching the game by playing it in board game format. 
* Recieved feedback on compulsory1 from TAs.

__Work for next time__: 
* Create more roles
* Create iteration plan
* Johanna: BoardPiece objects and implement objectsGenerator

#### 18.02.2020
__Attendence__: Martin, Erlend, Johanna, Lena

__Summary__: 
* Assigned new roles for team members.
* Further planning of development towards compulsory2.
* Comming to an agreement regarding the MVP for the next itteration

__Work for next time:__
* Take a closer look at the hand outs
* Lena: Implement Cards, ProgramCard, and ENUM ProgramCardType
* Martin: Implement playerPiece interaction with the board, ex. collision and falling off map
* Johanna: Work on Map class

#### 20.02.2020
__Attendence__: Everyone

__Summary__:
* The PieceGrid and BoardPiece classes approved and pushed to develop
* Card Classes approved and pushed to develop
* Assignment of tasks: 
  * Lena: Further implement card and deck
  * Erlend: Create images for cards
  * Henrik: Implement menu screen screen and gameScreen
  * Martin: Further implements player collision
  * Johanna: Extract more boardPieces from tmx file

__Work for next time:__
* Lena: implement deck and GameDeck
* Martin: Squash some bugs regarding collision mechanics, and implement death mechanic
* Erlend: Create the card images
* Johanna: Create bridge between backend and frontend

#### 24.02.2020
__Attendence__: Everyone

__Summary__:
* Erlend and Lena cooperated regarding the use of the Card and Deck classes.
* Martin and Johanna pairprogrammed to connect different backends, connect player movement to map

__Work for next time:__
* Lena: Start looking at tests for card and decks, and work on MarkDown file
* Martin: Squash bugs
* Henrik: Continues work on main menu screen
* Johanna: Prepares presentation

#### 27.02.2020
__Attendence__: Everyone

__Summary__: 
* Went through presentation for next week
* Merging backend and frontend

__Work for next time:__
* General refactoring for the deadline
* Martin: Starts implementing tests for player
* Erlend: Create new UI class
* Henrik: Fine tuning of main menu screen, implement switching screen functionality
* Lena: Keep implementing Card and Deck tests
* Johanna: Greate tests for the GridObjects package, finsh implementation of Lock In button, make UML

## Deloppgave2: Requirements
### Requirements for Second Iteration
* Move player by choosing cards
* Collision with walls
* Death when falling off board

### How to reach these requirements:
First, we need to represent the board and the pieces on the board.
* Map
  * Data structure that encompasses the requirement for a game board
  * 2D grid with lists of BoardPieces in each position
  * Height and Width
* BoardPiece
  * An abstract class that all objects on the board extend
  * Position (make class)
  * ID (tmx file id)
* DirectionedPiece
  * Extends BoardPiece
  * Has a direction
  * Can change direction
* PlayerPiece
  * Extends MoveablePiece
  * Appearance changes based on player state
* WallPiece
  * Prevents a moveable piece from moving across cell
_Sidenote: all “board objects”, ie things that lie on the board are called SomethingPiece to underline the fact that they are pieces on the board._

Then, we need to represent the role of the player (note, Player /= PlayerPiece). This is of course not a complete description of the requirements for Player, just the requirements for this iteration.
* Player
  * The class that represents the “programmer” in the game
  * Must take movement input, and move PlayerPiece accordingly.
  * Check for valid input
  * Variables: 
  PlayerPiece, 
  PlayerNumber, 
  A type of player state (dead, alive etc), 

* Card
  * Abstract class that represents a single card
  * CardImage (visual representation of card)
* ProgramCard
  * Class representing a program card
  * CardType (enum class)
  * CardPriority
* CardType
  * ENUM class
  * MOVE1
  * MOVE2
  * etc.
* Deck
  * Abstract class for a set of cards
  * Ability to add cards
  * Ability to shuffle cards
  * Ability to deal cards
  * Ability to move all card from a different deck into this one
* GameDeck
  * A collection of a drawDeck and discardDeck
  * Ability to create a deck according to the rules of the game
  * Method to draw a hand of cards from the drawDeck 
  
 Finally, we need a way to show all this to the user.
* Game
  * The "bridge" between frontend and backend
  * Has the Map object corresponding to the state of the game
  * Has the Player object representing the player in the game
  * Can recieve input from GameScreen
  * Can make the player execute input

* Menu Screen
  * The first screen that the user sees when running the program
  * Has an EXIT buttton that closes the program
  * Has a play button, which closes the menu screen, and opens the game screen
 
* Game Screen
  * Shows the board, with objects on it
  * Shows the cards available to the player
  * Allows the user to select 5 cards, and execute them
  * Also allows the user to move around the board with UP, DOWN, LEFT, RIGHT

### Known bugs
* The CardButtons hitbox do not entirely match the images on screen
* The CardTests send nullPointerException because the ProgramCard class uses gdx to create an image. The tests run if you comment out line 15 in ProgramCard.java where the createImage(); method is called

## Deloppgave3: Code

### How to run the program

### How to run tests
* To run the CardTests, you have to go to the ProgramCard.java class and comment out line 15 where the createImage() method is called, as this method will throw a nullPointerException if not run in the program as a whole. 

### How to run manual tests
