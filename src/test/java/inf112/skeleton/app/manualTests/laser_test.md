# How to control the player:
- Use direction buttons (UP, DOWN, LEFT, RIGHT)
- LEFT and RIGHT only changes direction
- UP: Move forward one tile
- DOWN: Back up one tile

### Player receives damage for positioning over a single static laser field (laser that has a wall as source)
How to test:
1. Run main() and click "Play".
2. Choose Stage 1
3. Move to coordinates (9, 0) facing NORTH.
4. For now, in order to take damage from a laser you must initialize a phase. To do so, select "Move 1" as the first
card in your hand, the rest doesn't matter.
5. Click "Lock in".
6. You should now have a damage token.

### Player receives damage for positioning over a double static laser field (laser that has a wall as source)
How to test:
1. Run main() and click "Play".
2. Choose Stage 1
3. Move to coordinates (6, 2) facing NORTH.
4. For now, in order to take damage from a laser you must initialize a phase. To do so, select "Move 1" as the first
card in your hand, the rest doesn't matter.
5. Click "Lock in".
6. You should now have two damage tokens.