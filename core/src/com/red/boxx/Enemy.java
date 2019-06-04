package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

/**
 * The type Enemy.
 * Enemies have a starting position.
 * Enemies make a move every turn.
 */
public class Enemy extends Actor {

    /**
     * Instantiates a new Enemy.
     *
     * @param startingX the starting x
     * @param startingY the starting y
     * @param image     the image
     */
    Enemy(int startingX, int startingY, Texture image) {
        super(startingX, startingY, image);
        initEnemy();
    }

    private void initEnemy() {
    }

    /**
     * Move.
     *
     * @param xDisplacement the x displacement
     * @param yDisplacement the y displacement
     */
    void move(int xDisplacement, int yDisplacement) {
        int newX = getX() + xDisplacement;
        int newY = getY() + yDisplacement;
        setX(newX);
        setY(newY);
    }

}
