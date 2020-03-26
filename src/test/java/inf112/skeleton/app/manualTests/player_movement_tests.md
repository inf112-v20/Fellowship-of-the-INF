# How to control the player:
- Use direction buttons (UP, DOWN, LEFT, RIGHT)
- LEFT and RIGHT only changes direction
- UP: Move forward one tile
- DOWN: Back up one tile

### Testing that the player dies from positioning over a hole
How to test:
1. Run main() and click "Play".
2. Choose Stage 1
3. Move to coordinates (5, 2).
4. The eyes of the player should now be red, indicating that the player is dead, and cannot continue moving.
<br>

### Testing that the player cannot move through a wall
How to test:
1. Run main() and click "Play".
2. Choose Stage 1
3. Move to coordinates (0, 1).
4. Try to move through the wall to the right<br>
   You should see that the player stays on coordinates (0, 1)
5. For reference, try moving down again to the starting point and moving right (where there is no wall). The player
should be able to move this time.

### Test: Player cannot move through a wall that is connected to another wall
We discovered that the player could move through a wall if it was connected to another wall, so we needed to fix that.<br>
How to test:
1. Run main() and click "Play".
2. Choose Stage 1
3. Move to coordinates (2, 3).
4. Try to move through both walls<br>
   The player should not be able to move through any of them. 