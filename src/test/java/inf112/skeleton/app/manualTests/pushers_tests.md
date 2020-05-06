# Manual tests for the pushers
### Setups

#### Setup nr. 1
* Run Main.
* Press the Test button in the menu screen.
* Select the map "pushers_test_map_1" from the dropdown menu
* Press the "Start Test" button

#### Setup nr. 2
* Run Main.
* Press the Test button in the menu screen.
* Select the map "pushers_test_map_2" from the dropdown menu
* Press the "Start Test" button

#### Setup nr. 3
* Run Main.
* Press the Test button in the menu screen.
* Select the map "pushers_test_map_3" from the dropdown menu
* Press the "Start Test" button

#### Setup nr. 4
* Run Main.
* Press the Test button in the menu screen.
* Select the map "pushers_test_map_4" from the dropdown menu
* Press the "Start Test" button

### Tests

#### Test for pusher pushing two robots
* Complete setup nr. 1
* Pick cards so that you do not change position in the next phase (rotate cards). 
* Execute as many phases (by pressing ENTER) as rotation cards you selected.
* *Assert: player one was pushed by the pusher, which resulted in player 2 also being pushed*


#### Test for pushing in correct direction pt1
* Complete setup nr. 3
* Pick cards so that you do not change position in the next phase (rotate cards). If you run out of rotation cards, select move cards last.
* Press the lock in button.
* Execute as many phases (by pressing ENTER) as rotation cards you selected.
* *Assert: player one is being pushed in circles by the pushers.*


#### Test for pushing in correct direction pt2
* Complete setup nr. 4
* Pick cards so that you do not change position in the next phase (rotate cards).
* Press the lock in button.
* Wait for one phase to be executed
* *Assert: player one is being pushed in circles by the pushers.*

The point of having pt 1 and 2, is that once both of these tests has been complete, the pushing ability of all the possible pusher pieces has been tested.

#### Test odd-numberes pushers are only active during odd-numbered rounds
* Complete setup nr. 5
* Pick cards so that you do not change position in the next phase (choose rotate cards).
* Press lock in button.
* Wait for one phase to be executed
* *Assert: has been pushed by the odd-numbered pusher.*
* Execute another phase (by pressing ENTER), so that an even-numbered phase is executed.
* *Assert: player one has not been pushed by the odd-numbered pusher.*

If you have picked enough rotation cards you can keep executing phases and see that the robot is only being pushed during odd-numbered phases as there are only odd-numbered pushers.


#### Test even-numbered pushers are only active during even-numbered rounds
* Complete setup nr. 6
* Pick cards so that you do not change position in the next phase (choose rotate cards).
* Press the lock in button.
* Wait for one phase to be executed
* *Assert: player one has not been pushed by the even-numbered pusher.*
* Execute another phase (by pressing ENTER), so that an even-numbered phase is executed.
* *Assert: player one has been pushed by the even-numbered pusher.*
 
 If you have picked enough rotation cards you can keep executing phases and see that the robot is only being pushed during even-numbered phases as there are only even-numbered pushers.


#### Test for only being pushed once by pushers per phase
* Complete setup nr. 2
* Pick cards so that you do not change position.
* Press the lock in button.
* Wait for one phase to be executed
* *Assert: player 1 has only been pushed once by the pushers.*


#### Test for blocking-functionality of pushers
* Complete setup nr. 2
* Press DOWN key once.
* *Assert: Robot 1 could not go through the pusher.*


* Press UP key once.
* Press LEFT key once:
* Press UP key once.
* *Assert: Robot 1 could not go through the pusher.*


* Press DOWN key twice.
* *Assert: Robot 1 could not go through the pusher.*


* Press RIGHT key once.
* Press UP key once.
* Press LEFT key once.
* Press DOWN key once.
* *Assert: Robot 1 could not go through the pusher.*


* Press UP key twice.
* *Assert: Robot 1 could not go through the pusher.*


* Press RIGHT key once.
* Press UP key twice.
* *Assert: Robot 1 could not go through the pusher.*


* Press RIGHT key once.
* Press UP key once.
* Press LEFT key once:
* Press UP key once.
* *Assert: Robot 1 could not go through the pusher.*


* Press DOWN key four times.
* *Assert: Robot 1 could not go through the pusher.*

