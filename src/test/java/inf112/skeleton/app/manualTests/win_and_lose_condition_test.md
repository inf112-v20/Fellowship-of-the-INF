# Manual test of Win condition and lose condition
### Setup:
1. Run main()
2. Click the "Test" button. You should now be in the testing screen. <br>
   From the dropdown menu, choose the map "win_condition_test_map" for testing win condition. <br>
   Choose the map "lose_condition_test_map" for testing lose condition. <br>
   Click the "Start Test" button.
   
#### Win condition test:
1. Proceed to visit all flags using the arrow keys.<br>
2. Since flags are only visited at the end of a phase, use the enter key on the keyboard 
   to start only one phase after you've placed yourself on the correct flag.<br>
   Flags need to be visited in ascending order.
3. When a correct flag is visited, the UI should highlight a new flag from left to right until
   all flags are visited.<br>
   Also, the spawn point (shown by a little icon of the player's robot on the respawn tile) should be changed
   even though the robot visited the wrong flag.
   On the same premise, the robot should be repaired.
4. After all flags have been visited in the correct order, a dialog box should pop up and say which player has won.
5. You can now choose one of the two options to Exit the application, or return to the Main Menu for another test or game.

#### Lose condition test:
1. Proceed to intentionally moving the robot into an abyss using the arrow keys.
2. This wil trigger a respawn: Choose direction using the arrow keys and press R to respawn.
3. After dying, one of the three lives (indicated by blue circles) in the UI should fade to indicate 
   two less respawns are available. <br>
   The robot should also have two damage tokens upon respawning, as shown in the UI.
4. After respawning, repeat steps 1-3 until all lives are depleted and the game over screen shows up.
5. Choose one of the options to either return to the main menu, exit the application, or to keep watching the AI robots.<br>
   There is no point to keep watching the AI as they are dealt no cards on the test maps.
6. Alternatively you can push all the robots off the map first to trigger the game over dialog box which should not
   show the option for keep watching.