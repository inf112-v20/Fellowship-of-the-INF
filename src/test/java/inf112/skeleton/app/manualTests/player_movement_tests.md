### Testing that the player dies from positioning over a hole
How to test:
1. Run main() and click play
2. Move to coordinates (5, 2) <br>
   Input: right x5, up x2
3. The eyes of the player should now be red, indicating that the player is dead, and cannot continue moving.
<br>

### Testing that the player cannot move through a wall
How to test:
1. Run main() and click play
2. Move to coordinates (0, 1) <br>
   Input: up
3. Try to move through the wall to the right<br>
   You should see that the player stays on coordinates (0, 1)
4. For reference try moving down again to the starting point and moving right (where there is no wall). The player
should be able to move this time.