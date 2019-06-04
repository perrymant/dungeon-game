package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

/**
 * Exits have locations on the board.
 * Exits are the portals from:
 *  - one level to another;
 *  - one menu to another;
 *  - a level to a menu;
 *  - a menu to a level.
 */
class Exit extends Actor {

    /**
     * The Action.
     */
    Action action;

    /**
     * Instantiates a new Exit.
     *
     * @param x      the x
     * @param y      the y
     * @param image  the image
     * @param action the action
     */
    Exit(int x, int y, Texture image, Action action) {
        super(x, y, image);
        initArea();
        this.action = action;
    }

    private void initArea() {
    }

    /**
     * The enum Action.
     */
    public enum Action {
        /**
         * Sp menu action.
         */
        SP_MENU,
        /**
         * Mp menu action.
         */
        MP_MENU,
        /**
         * Sp start action.
         */
        SP_START,
        /**
         * Sp continue action.
         */
        SP_CONTINUE,
        /**
         * Mp start action.
         */
        MP_START,
        /**
         * Mp continue action.
         */
        MP_CONTINUE,
        /**
         * Settings action.
         */
        SETTINGS,
        /**
         * Main menu action.
         */
        MAIN_MENU,
        /**
         * Exit action.
         */
        EXIT,
        /**
         * Rules action.
         */
        RULES,
        /**
         * Null action.
         */
        NULL
    }

}