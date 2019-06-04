package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * The type Zombie.
 */
class Zombie extends Enemy {

    /**
     * Instantiates a new Zombie.
     *
     * @param startingX the starting x
     * @param startingY the starting y
     * @param image     the image
     */
    Zombie(int startingX, int startingY, Texture image) {
        super(startingX, startingY, image);

    }

    /**
     * Move random boolean.
     *
     * @param board the board
     * @return the boolean
     */
    boolean moveRandom(Board board) {
        Random r = new Random();
        int n;
        boolean triedLeft = false;
        boolean triedRight = false;
        boolean triedUp = false;
        boolean triedDown = false;


        // Keep trying until the enemy can actually move
        while (!triedLeft || !triedRight || !triedUp || !triedDown) {
            n = r.nextInt(4);
            if (n == 0) {
                triedLeft = true;
                if (move(Direction.LEFT, board)) {
                    return true;
                }
            } else if (n == 1) {
                triedRight = true;
                if (move(Direction.RIGHT, board)) {
                    return true;
                }
            } else if (n == 2) {
                triedUp = true;
                if (move(Direction.UP, board)) {
                    return true;
                }
            } else {
                triedDown = true;
                if (move(Direction.DOWN, board)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Can move boolean.
     *
     * @param board the board
     * @return the boolean
     */
    boolean canMove(Board board) {

        if (!this.wallCollisions(Direction.LEFT, board) &&
                !this.boxCollisions(Direction.LEFT, board) &&
                !this.zombieCollisions(Direction.LEFT, board)) {
            return true;
        } else if (!this.wallCollisions(Direction.RIGHT, board) &&
                !this.boxCollisions(Direction.RIGHT, board) &&
                !this.zombieCollisions(Direction.RIGHT, board)) {
            return true;
        } else if (!this.wallCollisions(Direction.UP, board) &&
                !this.boxCollisions(Direction.UP, board) &&
                !this.zombieCollisions(Direction.UP, board)) {
            return true;
        } else if (!this.wallCollisions(Direction.DOWN, board) &&
                !this.boxCollisions(Direction.DOWN, board) &&
                !this.zombieCollisions(Direction.DOWN, board)) {
            return true;
        }

        return false;
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
        if (this.boxCollisions(direction, board)) {
            // The enemy doesn't move
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The enemy can't move there, a box is in the way!");
            return false;
        }
        if (this.zombieCollisions(direction, board)) {
            // The enemy doesn't move
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The enemy can't move there, a zombie is in the way!");
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
