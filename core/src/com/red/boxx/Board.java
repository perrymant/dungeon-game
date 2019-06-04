package com.red.boxx;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

/**
 * This is where the main game logic goes
 * At render-time, this class scans through a map and places the objects in the correct location.
 * At render-time, this class updates where the enemies are located.
 * This class checks the wining and losing conditions
 */
public class Board {

    /**
     * The Walls.
     */
    ArrayList<Wall> walls = new ArrayList<Wall>();
    /**
     * The Boxes.
     */
    ArrayList<Box> boxes = new ArrayList<Box>();
    /**
     * The Bomb boxes. TODO
     */
    public ArrayList<BombBox> bombBoxes = new ArrayList<BombBox>();
    /**
     * The Areas.
     */
    ArrayList<Area> areas = new ArrayList<Area>();
    /**
     * The Ghosts.
     */
    ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
    /**
     * The Zombies.
     */
    ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    /**
     * The Player.
     */
    Player player;
    /**
     * The Challenge.
     */
    ChallengeType challenge;

    /**
     * The Map.
     */
    String map;
    /**
     * The Player name.
     */
    String playerName;
    /**
     * The Wall image.
     */
    Texture wallImage;
    /**
     * The Area image.
     */
    Texture areaImage;
    /**
     * The Player image.
     */
    Texture playerImage;
    /**
     * The Box image.
     */
    Texture boxImage;
    /**
     * The Ghost image.
     */
    Texture ghostImage;
    /**
     * The Zombie image.
     */
    Texture zombieImage;
    /**
     * The Bomb box image.
     */
    Texture bombBoxImage;

    /**
     * The Width.
     */
    int width;
    /**
     * The Height.
     */
    int height;

    /**
     * The constant logger.
     */
    static Logger logger = new Logger(false);

    /**
     * Instantiates a new Board.
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
     * @param challenge    the challenge
     */
    Board(String map,
          Texture wallImage,
          Texture areaImage,
          Texture boxImage,
          String playerName,
          Texture playerImage,
          Texture ghostImage,
          Texture zombieImage,
          Texture bombBoxImage,
          ChallengeType challenge) {

        logger.log(Logger.LoggingType.DEBUG, "boxWallOrBoxCollisions",
                "Creating a board with the source file: " + map);

        this.challenge = challenge;
        this.map = map;
        this.playerName = playerName;
        this.wallImage = wallImage;
        this.areaImage = areaImage;
        this.playerImage = playerImage;
        this.boxImage = boxImage;
        this.ghostImage = ghostImage;
        this.zombieImage = zombieImage;
        this.bombBoxImage = bombBoxImage;

        int scanCreatorX = 0;
        int scanCreatorY = 0;
        Wall tempWall;
        Box tempBox;
        Area tempArea;
        Ghost tempGhost;
        Zombie tempZombie;

        String level = map;

        // Scans the map, and places objects on the Game Board.
        for (int i = 0; i < level.length(); i++) {
            char item = level.charAt(i);

            switch (item) {
                case '\n':
                    scanCreatorY += 1;
                    if (width < scanCreatorX) {
                        width = scanCreatorX;
                    }
                    scanCreatorX = 0;
                    break;
                case '#':
                    tempWall = new Wall(scanCreatorX, scanCreatorY, wallImage);
                    walls.add(tempWall);
                    scanCreatorX += 1;
                    break;
                case '$':
                    tempBox = new Box(scanCreatorX, scanCreatorY, boxImage);
                    boxes.add(tempBox);
                    scanCreatorX += 1;
                    break;
                case '%':
                    tempBox = new Box(scanCreatorX, scanCreatorY, boxImage);
                    boxes.add(tempBox);
                    tempArea = new Area(scanCreatorX, scanCreatorY, areaImage);
                    areas.add(tempArea);
                    scanCreatorX += 1;
                    break;
                case '|':
                    scanCreatorX += 1;
                    break;
                case '.':
                    tempArea = new Area(scanCreatorX, scanCreatorY, areaImage);
                    areas.add(tempArea);
                    scanCreatorX += 1;
                    break;
                case '@':
                    this.player = new Player(playerName, scanCreatorX, scanCreatorY, playerImage);
                    scanCreatorX += 1;
                    break;
                case '+':
                    scanCreatorX += 1;
                    break;
                case 's':
                    scanCreatorX += 1;
                    break;
                case 'e':
                    scanCreatorX += 1;
                    break;
                case 'i':
                    scanCreatorX += 1;
                    break;
                case 'm':
                    scanCreatorX += 1;
                    break;
                case 'S':
                    scanCreatorX += 1;
                    break;
                case 'M':
                    scanCreatorX += 1;
                    break;
                case 'c':
                    scanCreatorX += 1;
                    break;
                case 'C':
                    scanCreatorX += 1;
                    break;
                case 'r':
                    scanCreatorX += 1;
                    break;
                case 'n':
                    scanCreatorX += 1;
                    break;
                case 'R':
                    scanCreatorX += 1;
                    break;
                case 'g':
                    tempGhost = new Ghost(scanCreatorX, scanCreatorY, ghostImage);
                    ghosts.add(tempGhost);
                    scanCreatorX += 1;
                    break;
                case 'z':
                    tempZombie = new Zombie(scanCreatorX, scanCreatorY, zombieImage);
                    zombies.add(tempZombie);
                    scanCreatorX += 1;
                    break;
                case ' ':
                    scanCreatorX += 1;
                    break;
                default:
                    break;
            }

            height = scanCreatorY;
        }

        logger.log(Logger.LoggingType.DEBUG, "BoardLog",
                "Created board with WIDTH " + width + " and HEIGHT " + height + ".");

    }

    /**
     * Move all Enemies .
     */
    public void enemiesMoveAll() {
        for (Ghost e : ghosts) {
            e.moveRandom(this);
        }
        for (Zombie e : zombies) {
            e.moveRandom(this);
        }
    }

    /**
     * Check win condition boolean.
     *
     * @return the boolean
     */
    public boolean checkWinCondition() {

        if (this.challenge == ChallengeType.TRAPZOMBIES) {
            int zombieCnt = this.zombies.size();
            int trappedZombies = 0;
            for (Zombie zombie : this.zombies) {
                if (!zombie.canMove(this)) {
                    trappedZombies++;
                }
            }
            return trappedZombies == zombieCnt;
        } else /* if (this.challenge == ChallengeType.PLACEBOXES) */ {
            int boxCnt = this.boxes.size();
            int completedBoxes = 0;
            for (Box box : this.boxes) {
                for (Area area : this.areas) {
                    if (box.getX() == area.getX() && box.getY() == area.getY()) {
                        completedBoxes++;
                    }
                }
            }
            return completedBoxes == boxCnt;
        }

    }

    /**
     * Check loss condition boolean.
     *
     * @return the boolean
     */
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
        return false;
    }

    /**
     * Reset board.
     *
     * @return the board
     */
    public Board reset() {
        return new Board(this.map,
                this.wallImage,
                this.areaImage,
                this.boxImage,
                this.playerName,
                this.playerImage,
                this.ghostImage,
                this.zombieImage,
                this.bombBoxImage,
                this.challenge);
    }

    /**
     * The enum Challenge type.
     */
    public enum ChallengeType {
        /**
         * Trapzombies challenge type.
         */
        TRAPZOMBIES,
        /**
         * Placeboxes challenge type.
         */
        PLACEBOXES
    }

}
