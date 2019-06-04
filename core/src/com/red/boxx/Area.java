package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

/**
 * The area class is the special specific final position inside the level
 */
class Area extends Actor {

    /**
     * Instantiates a new Area.
     *
     * @param x     the x
     * @param y     the y
     * @param image the image
     */
    Area(int x, int y, Texture image) {
        super(x, y, image);

        initArea();
    }

    private void initArea() {

    }

}


