# How to control the player:
- Use direction buttons (UP, DOWN, LEFT, RIGHT)
- LEFT and RIGHT only changes direction
- UP: Move forward one tile
- DOWN: Back up one tile

### Player robot is repaired (discarding 1 damage token) when standing on a repair site at the end of a round
How to test:
1. Run main() and click "Play".
2. Choose Stage 1
3. Choose 5 cards and lock in.
4. Move to coordinates (9, 1) in order to take damage from a laser. You can use the directional buttons in between phases
to move your robot.
5. Now that you have a damage token, make sure at the *end of phase 5* (end of the first round) that you are standing
on coordinates (11, 0) (bottom right corner of the map).
6. The robot should now be repaired and the damage token is discarded.