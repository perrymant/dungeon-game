package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;


/**
 * The boxes are a movable object.
 * The box cannot be moved in a direction if there is an obstacle behind it.
 */
public class Box extends Actor {

    /**
     * Instantiates a new Box.
     *
     * @param startingX the starting x
     * @param startingY the starting y
     * @param image     the image
     */
    Box(int startingX, int startingY, Texture image) {
        super(startingX, startingY, image);

        initBox();
    }

    private void initBox() {
    }

    private void move(int xDisplacement, int yDisplacement) {
        int newX = getX() + xDisplacement;
        int newY = getY() + yDisplacement;

        setX(newX);
        setY(newY);
    }

    /**
     * Move boolean.
     *
     * @param direction the direction
     * @param board     the board
     * @return the boolean
     */
    boolean move(Direction direction, Board board) {

        Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                "Trying to move a box in position (" + this.getX() + ", " + this.getY() + ")");

        // Deal with the case where the box collides with a wall, box, zombie or player
        if (wallCollisions(direction, board) ||
                boxCollisions(direction, board) ||
                playerCollisions(direction, board) ||
                zombieCollisions(direction, board) ||
                invisiWallCollisions(direction, board)) {
            // The box doesn't move
            Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                    "The box is being stopped by an actor, returning.)");
            return false;
        } else {
            if (direction == Direction.LEFT) {
                Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                        "Moving the box LEFT!");
                this.move(-1, 0);
            } else if (direction == Direction.RIGHT) {
                Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                        "Moving the box RIGHT!");
                this.move(1, 0);
            } else if (direction == Direction.UP) {
                Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                        "Moving the box UP!");
                this.move(0, -1);
            } else if (direction == Direction.DOWN) {
                Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                        "Moving the box DOWN!");
                this.move(0, 1);
            }
            return true;
        }

    }

    /**
     * Move boolean.
     *
     * @param direction the direction
     * @param board     the board
     * @return the boolean
     */
    public boolean move(Direction direction, TwoPlayerBoard board) {

        Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                "Trying to move a box in position (" + this.getX() + ", " + this.getY() + ")");

        // Deal with the case where the box collides with a wall, box, zombie or player
        if (wallCollisions(direction, board) ||
                boxCollisions(direction, board) ||
                playerCollisions(direction, board) ||
                player2Collisions(direction, board) ||
                zombieCollisions(direction, board)) {
            // The box doesn't move
            Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                    "The box is being stopped by an actor, returning.)");
            return false;
        } else {
            if (direction == Direction.LEFT) {
                Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                        "Moving the box LEFT!");
                this.move(-1, 0);
            } else if (direction == Direction.RIGHT) {
                Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                        "Moving the box RIGHT!");
                this.move(1, 0);
            } else if (direction == Direction.UP) {
                Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                        "Moving the box UP!");
                this.move(0, -1);
            } else if (direction == Direction.DOWN) {
                Board.logger.log(Logger.LoggingType.DEBUG, "boxMove",
                        "Moving the box DOWN!");
                this.move(0, 1);
            }
            return true;
        }

    }

}
