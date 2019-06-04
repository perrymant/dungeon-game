package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

/**
 * The type Wall.
 */
class Wall extends Actor {

    /**
     * Instantiates a new Wall.
     *
     * @param x     the x
     * @param y     the y
     * @param image the image
     */
    Wall(int x, int y, Texture image) {
        super(x, y, image);

        initWall();
    }

    private void initWall() {
    }
}
