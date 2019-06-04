package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

/**
 * The type Player.
 */
public class Player extends Actor {

    /**
     * The Name.
     */
    private String name;

    /**
     * Instantiates a new Player.
     *
     * @param name      the name
     * @param startingX the starting x
     * @param startingY the starting y
     * @param image     the image
     */
    Player(String name, int startingX, int startingY, Texture image) {
        super(startingX, startingY, image);

        initPlayer(name);
    }

    private void initPlayer(String name) {
        setName(name);
    }

    /**
     * Move.
     *
     * @param xDisplacement the x displacement
     * @param yDisplacement the y displacement
     */
    private void move(int xDisplacement, int yDisplacement) {
        int newX = getX() + xDisplacement;
        int newY = getY() + yDisplacement;

        setX(newX);
        setY(newY);
    }

    /**
     * Move.
     *
     * @param direction the direction
     * @param board     the board
     */
    void move(Direction direction, Board board) {

        Board.logger.log(Logger.LoggingType.DEBUG, "move",
                "Trying to move player.");

        // Deal with the case where the player collides with a wall
        if (this.wallCollisions(direction, board)) {
            // The player doesn't move
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player can't move there, a wall is in the way!");
            return;
        }

        // Else deal with the case where the player collides with a box
        if (boxCollisions(direction, board)) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player can't move there, a box is in the way!");
            return;
        }

        // Else move the player
        if (direction == Direction.LEFT) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player is moving LEFT!");
            this.move(-1, 0);
        } else if (direction == Direction.RIGHT) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player is moving RIGHT!");
            this.move(1, 0);
        } else if (direction == Direction.UP) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player is moving UP!");
            this.move(0, -1);
        } else if (direction == Direction.DOWN) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player is moving DOWN!");
            this.move(0, 1);
        }

    }

    /**
     * Move.
     *
     * @param direction the direction
     * @param board     the board
     */
    public void move(Direction direction, TwoPlayerBoard board) {

        Board.logger.log(Logger.LoggingType.DEBUG, "move",
                "Trying to move player.");

        // Deal with the case where the player collides with a wall
        if (this.wallCollisions(direction, board)) {
            // The player doesn't move
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player can't move there, a wall is in the way!");
            return;
        }

        // Else deal with the case where the player collides with a box
        if (boxCollisions(direction, board)) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player can't move there, a box is in the way!");
            return;
        }

        if (playerCollisions(direction, board)) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player can't move there, a player is in the way!");
            return;
        }

        if (player2Collisions(direction, board)) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player can't move there, a player is in the way!");
            return;
        }

        // Else move the player
        if (direction == Direction.LEFT) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player is moving LEFT!");
            this.move(-1, 0);
        } else if (direction == Direction.RIGHT) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player is moving RIGHT!");
            this.move(1, 0);
        } else if (direction == Direction.UP) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player is moving UP!");
            this.move(0, -1);
        } else if (direction == Direction.DOWN) {
            Board.logger.log(Logger.LoggingType.DEBUG, "move",
                    "The player is moving DOWN!");
            this.move(0, 1);
        }

    }

    @Override
    public boolean boxCollisions(Direction direction, Board board) {

        Board.logger.log(Logger.LoggingType.DEBUG, "boxCollisions",
                "Checking whether the player is colliding with a box...");

        if (direction == Direction.LEFT) {
            for (Box box : board.boxes) {
                if (this.isLeftCollision(box)) {
                    Board.logger.log(Logger.LoggingType.DEBUG, "boxCollisions",
                            "There is a box to the LEFT of the player...");
                    return !box.move(direction, board);
                }
            }
            return false;
        } else if (direction == Direction.RIGHT) {
            for (Box box : board.boxes) {
                if (this.isRightCollision(box)) {
                    Board.logger.log(Logger.LoggingType.DEBUG, "boxCollisions",
                            "There is a box to the RIGHT of the player...");
                    return !box.move(direction, board);
                }
            }
            return false;
        } else if (direction == Direction.UP) {
            for (Box box : board.boxes) {
                if (this.isTopCollision(box)) {
                    Board.logger.log(Logger.LoggingType.DEBUG, "boxCollisions",
                            "There is a box to the TOP of the player...");
                    return !box.move(direction, board);
                }
            }
            return false;
        } else /* if (dir == Direction.DOWN) */ {
            for (Box box : board.boxes) {
                if (this.isBottomCollision(box)) {
                    Board.logger.log(Logger.LoggingType.DEBUG, "boxCollisions",
                            "There is a box to the BOTTOM of the player...");
                    return !box.move(direction, board);
                }
            }
            Board.logger.log(Logger.LoggingType.DEBUG, "boxCollisions",
                    "There are no boxes blocking the player, returning.");
            return false;
        }

    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    private void setName(String name) {
        this.name = name;
    }

}
