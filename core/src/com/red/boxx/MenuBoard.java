package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * The type Menu board.
 */
public class MenuBoard extends Board {


    /**
     * The Exits.
     */
    ArrayList<Exit> exits = new ArrayList();
    /**
     * The Invisible walls.
     */
    ArrayList<Wall> invisiWalls = new ArrayList<Wall>();

    /**
     * The Exit image.
     */
    private Texture exitImage;

    /**
     * Instantiates a new Menu board.
     *
     * @param map          the map
     * @param wallImage    the wall image
     * @param areaImage    the area image
     * @param boxImage     the box image
     * @param playerName   the player name
     * @param playerImage  the player image
     * @param ghostImage   the ghost image
     * @param zombieImage  the zombie image
     * @param bombBoxImage the bomb box image
     * @param exitImage    the exit image
     * @param challenge    the challenge
     */
    MenuBoard(String map,
              Texture wallImage,
              Texture areaImage,
              Texture boxImage,
              String playerName,
              Texture playerImage,
              Texture ghostImage,
              Texture zombieImage,
              Texture bombBoxImage,
              Texture exitImage,
              ChallengeType challenge) {

        super(map,
                wallImage,
                areaImage,
                boxImage,
                playerName,
                playerImage,
                ghostImage,
                zombieImage,
                bombBoxImage,
                challenge);

        int scanCreatorX = 0;
        int scanCreatorY = 0;

        this.exitImage = exitImage;

        String level = map;         // Hardcoded map at moment, later on we read .tmx files for the map

        // Scans the map, and places objects on the Game Board.
        for (int i = 0; i < level.length(); i++) {
            char item = level.charAt(i);

            switch (item) {
                case '\n':
                    scanCreatorY += 1;
                    scanCreatorX = 0;
                    break;
                case 's':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.SP_MENU));
                    scanCreatorX += 1;
                    break;
                case 'e':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.EXIT));
                    scanCreatorX += 1;
                    break;
                case 'i':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.SETTINGS));
                    scanCreatorX += 1;
                    break;
                case 'm':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.MP_MENU));
                    scanCreatorX += 1;
                    break;
                case 'S':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.SP_START));
                    scanCreatorX += 1;
                    break;
                case 'M':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.MP_START));
                    scanCreatorX += 1;
                    break;
                case 'c':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.SP_CONTINUE));
                    scanCreatorX += 1;
                    break;
                case 'C':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.MP_CONTINUE));
                    scanCreatorX += 1;
                    break;
                case 'r':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.MAIN_MENU));
                    scanCreatorX += 1;
                    break;
                case 'n':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.NULL));
                    scanCreatorX += 1;
                    break;
                case 'R':
                    this.exits.add(new Exit(scanCreatorX, scanCreatorY, exitImage, Exit.Action.RULES));
                    scanCreatorX += 1;
                    break;
                case '|':
                    this.invisiWalls.add(new Wall(scanCreatorX, scanCreatorY, wallImage));
                    scanCreatorX += 1;
                    break;
                default:
                    scanCreatorX += 1;
                    break;
            }
        }

    }

    @Override
    public boolean checkWinCondition() {
        for (Exit exit : exits) {
            if (player.getX() == exit.getX() && player.getY() == exit.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exiting via exit.
     *
     * @return the exit
     */
    Exit exitingVia() {
        for (Exit exit : exits) {
            if (player.getX() == exit.getX() && player.getY() == exit.getY()) {
                return exit;
            }
        }
        return null;
    }

    /**
     * Change exit state to.
     *
     * @param action the action
     */
    void changeExitStateTo(Exit.Action action) {
        for (Exit exit : exits) {
            if (exit.action == Exit.Action.NULL ||
                    exit.action == Exit.Action.SP_CONTINUE ||
                    exit.action == Exit.Action.MP_CONTINUE) {
                exit.action = action;
                return;
            }
        }
    }

    @Override
    public MenuBoard reset() {
        return new MenuBoard(this.map,
                this.wallImage,
                this.areaImage,
                this.boxImage,
                this.playerName,
                this.playerImage,
                this.ghostImage,
                this.zombieImage,
                this.bombBoxImage,
                this.exitImage,
                this.challenge);
    }

}

