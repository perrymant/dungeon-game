package com.red.boxx;

import com.badlogic.gdx.graphics.Texture;

/**
 * The type Two player board.
 */
public class TwoPlayerBoard extends Board {

    /**
     * The Player 2.
     */
    Player player2;
    /**
     * The Player 2 name.
     */
    private String player2Name;
    /**
     * The Player 2 img.
     */
    private Texture player2Img;

    /**
     * Instantiates a new Two player board.
     *
     * @param map          the map
     * @param wallImage    the wall image
     * @param areaImage    the area image
     * @param boxImage     the box image
     * @param playerName   the player name
     * @param player2Name  the player 2 name
     * @param playerImage  the player image
     * @param player2Image the player 2 image
     * @param ghostImage   the ghost image
     * @param zombieImage  the zombie image
     * @param bombBoxImage the bomb box image
     * @param challenge    the challenge
     */
    TwoPlayerBoard(String map,
                   Texture wallImage,
                   Texture areaImage,
                   Texture boxImage,
                   String playerName,
                   String player2Name,
                   Texture playerImage,
                   Texture player2Image,
                   Texture ghostImage,
                   Texture zombieImage,
                   Texture bombBoxImage,
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


        this.player2Name = player2Name;
        this.player2Img = player2Image;

        logger.log(Logger.LoggingType.DEBUG, "boxWallOrBoxCollisions",
                "Creating a two player board with the source file: " + map);

        int scanCreatorX = 0;
        int scanCreatorY = 0;

        String level = map;         // Hardcoded map at moment, later on we read .tmx files for the map

        // Scans the map, and places objects on the Game Board.
        for (int i = 0; i < level.length(); i++) {
            char item = level.charAt(i);
            switch (item) {
                case '\n':
                    scanCreatorY += 1;
                    scanCreatorX = 0;
                    break;
                case '+':
                    this.player2 = new Player(player2Name, scanCreatorX, scanCreatorY, player2Image);
                    scanCreatorX += 1;
                    break;
                default:
                    scanCreatorX += 1;
                    break;
            }
        }

    }

    @Override
    public boolean checkLossCondition() {
        for (Enemy enemy : this.ghosts) {
            if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                return true;
            }
        }
        for (Enemy enemy : this.zombies) {
            if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                return true;
            }
        }
        for (Enemy enemy : this.ghosts) {
            if (enemy.getX() == player2.getX() && enemy.getY() == player2.getY()) {
                return true;
            }
        }
        for (Enemy enemy : this.zombies) {
            if (enemy.getX() == player2.getX() && enemy.getY() == player2.getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TwoPlayerBoard reset() {
        return new TwoPlayerBoard(this.map,
                this.wallImage,
                this.areaImage,
                this.boxImage,
                this.playerName,
                this.player2Name,
                this.playerImage,
                this.player2Img,
                this.ghostImage,
                this.zombieImage,
                this.bombBoxImage,
                this.challenge);
    }

}
