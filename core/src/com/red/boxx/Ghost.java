package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * The type Ghost.
 */
class Ghost extends Enemy {

    /**
     * Instantiates a new Ghost.
     *
     * @param startingX the starting x
     * @param startingY the starting y
     * @param image     the image
     */
    Ghost(int startingX, int startingY, Texture image) {
        super(startingX, startingY, image);

    }

    /**
     * Move random.
     *
     * @param board the board
     */
    void moveRandom(Board board) {
        Random r = new Random();
        int n;
        boolean movedFlag = false;

        // Keep trying until the enemy can actually move
        while (!movedFlag) {

            n = r.nextInt(4);
            if (n == 0) {
                if (move(Direction.LEFT, board)) {
                    movedFlag = true;
                }
            } else if (n == 1) {
                if (move(Direction.RIGHT, board)) {
                    movedFlag = true;
                }
            } else if (n == 2) {
                if (move(Direction.UP, board)) {
                    movedFlag = true;
                }
            } else {
                if (move(Direction.DOWN, board)) {
                    movedFlag = true;
                }
            }
        }
    }

    /**
     * Move boolean.
     *
     * @param direction the direction
     * @param board     the board
     * @return the boolean
     */
    private boolean move(Direction direction, Board board) {

        Board.logger.log(Logger.LoggingType.DEBUG, "move",
                "Trying to move enemy.");

        // Deal with the case where the enemy collides with a wall
        if (this.wallCollisions(direction, board)) {
            // The enemy doesn't move
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The enemy can't move there, a wall is in the way!");
            return false;
        }

        // Else move the enemy
        if (direction == Direction.LEFT) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The enemy is moving LEFT!");
            this.move(-1, 0);
        } else if (direction == Direction.RIGHT) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The enemy is moving RIGHT!");
            this.move(1, 0);
        } else if (direction == Direction.UP) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The enemy is moving UP!");
            this.move(0, -1);
        } else if (direction == Direction.DOWN) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The enemy is moving DOWN!");
            this.move(0, 1);
        }

        return true;

    }

}
