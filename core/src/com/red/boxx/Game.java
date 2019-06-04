package com.red.boxx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;


/**
 * Game: This is a big class!
 * This class is concerned with creating the sprites and rendering them.
 * This class contains the sound control.
 * This class adds text to the display.
 * This class adds the fog functionality.
 * This class can adjust the Enemy speed.
 * This class contains the hard-coded boards.
 *
 */
public class Game extends ApplicationAdapter {
    private SpriteBatch batch;

    private Texture wallImg;
    private Texture areaImg;
    private Texture boxImg;
    private Texture playerImg;
    private Texture player2Img;
    private Texture ghostImg;
    private Texture zombieImg;
    private Texture exitImg;
    private Texture moveControlsImg;
    private Texture helpImg;

    private ArrayList<Sprite> wallSprites = new ArrayList<Sprite>();
    private ArrayList<Sprite> areaSprites = new ArrayList<Sprite>();
    private ArrayList<Sprite> boxSprites = new ArrayList<Sprite>();
    private ArrayList<Sprite> enemySprites = new ArrayList<Sprite>();
    private ArrayList<Sprite> menuSprites = new ArrayList<Sprite>();

    private Sprite playerSprite;
    private Sprite player2Sprite;

    private Board board;

    private long startTime = 0;
    private long baseTimeBetweenEnemyMoves = 1000000000;
    private long timeBetweenEnemyMoves = 1000000000; // 1 billion nanoseconds = 1 sec.

    private ArrayList<Board> spBoards = new ArrayList();
    private ArrayList<Board> mpBoards = new ArrayList();

    private int spActiveLevel = 1;
    private int mpActiveLevel = 1;

    private boolean inSettings = false;
    private boolean inMainMenu = true;
    private boolean inRules = false;

    private float volume = 0.5f;

    private boolean fogMode = false;
    private int fogRadius = 5;

    private MenuBoard mbStart;
    private MenuBoard spMenuBoard;
    private MenuBoard mpMenuBoard;
    private MenuBoard settingsMenuBoard;
    private MenuBoard rulesMenuBoard;

    private int settingsMBPlayerX = 8;
    private int settingsMBPlayerY = 5;
    private int rulesMBPlayerX = 8;
    private int rulesMBPlayerY = 7;

    private int totalSteps = 0;
    private int stepsThisLevel = 0;

    private Sound gameSound;
    private Sound inLevelSound;
    private Sound inMenuSound;
    private Sound winSound;
    private Sound looseSound;
    private Sound boxGoalSound;
    private long gameSoundID;

    //Sound completeLevelSound;

    @Override
    public void create() {

        Gdx.graphics.setWindowedMode(1200, 600); // Think about compatibility with Android screen sizes

        inLevelSound = Gdx.audio.newSound(Gdx.files.internal("background2.mp3"));
        //https://soundimage.org/fantasywonder/
        inMenuSound = Gdx.audio.newSound(Gdx.files.internal("Our-Mountain_v003_Looping.mp3"));

        winSound = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx3.wav"));
        looseSound = Gdx.audio.newSound(Gdx.files.internal("sfx/loose.wav"));
        boxGoalSound = Gdx.audio.newSound(Gdx.files.internal("sfx/sfx1.wav"));

        gameSound = inMenuSound;
        gameSoundID = gameSound.loop();
        gameSound.setVolume(gameSoundID, volume);
        batch = new SpriteBatch();

        timeBetweenEnemyMoves = baseTimeBetweenEnemyMoves;

        wallImg = new Texture("temp/wall.png");
        areaImg = new Texture("temp/area.png");
        boxImg = new Texture("temp/box.png");
        playerImg = new Texture("temp/player.png");
        player2Img = new Texture("temp/player2.png");
        ghostImg = new Texture("temp/ghost.png");
        zombieImg = new Texture("temp/monster.png");
        exitImg = new Texture("temp/exit.png");
        moveControlsImg = new Texture("temp/controls-menu.png");
        helpImg = new Texture("temp/help.png");

        startTime = TimeUtils.nanoTime();

        MenuBoard mbStart = defineLevels();

        board = mbStart;

        recreateAllSprites();

    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(255 / 255f, 204 / 255f, 153 / 255f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (inSettings) {
            volumeControl();
            enemySpeed();
            fogToggle();
        }

        boolean inMenuBefore;

        inMenuBefore = board instanceof MenuBoard;

        detectMovements();
        detectShortcuts();

        if (TimeUtils.timeSinceNanos(startTime) > timeBetweenEnemyMoves) {
            board.enemiesMoveAll();
            startTime = TimeUtils.nanoTime();
        }

        checkForBoardChange();

        boolean inMenuAfter;

        inMenuAfter = board instanceof MenuBoard;

        if (inMenuBefore != inMenuAfter) {
            resetSound();
        }

        recreateAllSprites();
        if (board instanceof MenuBoard || !fogMode) {
            renderSpritesNormally();
        } else {
            Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            renderSpritesWithFog();
        }
        writeMenuFont();

    }

    private void resetSound() {
        if (board instanceof MenuBoard) {
            gameSound.stop();
            gameSound = inMenuSound;
            gameSoundID = gameSound.loop();
            gameSound.setVolume(gameSoundID, volume);
        } else {
            gameSound.stop();
            gameSound = inLevelSound;
            gameSoundID = gameSound.loop();
            gameSound.setVolume(gameSoundID, volume * 0.5f);
        }
    }

    private void checkForBoardChange() {
        // Check if the game has ended
        if (board.checkWinCondition()) {
//             WIN sound
            winSound.play(volume);

            if (board instanceof MenuBoard) {
                handleMenuWin();
            } else if (board instanceof TwoPlayerBoard) {
                handleTwoPlayerWin();
                resetTheBoard();
            } else {
                handleSinglePlayerWin();
                resetTheBoard();
            }

        } else if (board.checkLossCondition()) {
//            LOSE sound
            looseSound.play(volume);
            resetTheBoard();
        }
    }

    private void resetTheBoard() {
        if (board instanceof MenuBoard) {
            board = ((MenuBoard) board).reset();
        } else if (board instanceof TwoPlayerBoard) {
            board = ((TwoPlayerBoard) board).reset();
        } else {
            board = board.reset();
        }
    }

    private void handleMenuWin() {
        MenuBoard menuBoard = (MenuBoard) board;

        if (menuBoard.exitingVia().action == Exit.Action.SP_MENU) {
            board = spMenuBoard;
            resetTheBoard();
            inMainMenu = false;
        } else if (menuBoard.exitingVia().action == Exit.Action.MP_MENU) {
            board = mpMenuBoard;
            resetTheBoard();
            inMainMenu = false;
        } else if (menuBoard.exitingVia().action == Exit.Action.SP_START) {
            board = spBoards.get(0);
            resetTheBoard();
            spActiveLevel = 1;
        } else if (menuBoard.exitingVia().action == Exit.Action.SP_CONTINUE) {
            board = spBoards.get(spActiveLevel - 1);
            inSettings = false;
            inRules = false;
        } else if (menuBoard.exitingVia().action == Exit.Action.MP_START) {
            board = mpBoards.get(0);
            resetTheBoard();
            mpActiveLevel = 1;
        } else if (menuBoard.exitingVia().action == Exit.Action.MP_CONTINUE) {
            board = mpBoards.get(mpActiveLevel - 1);
            inSettings = false;
            inRules = false;
        } else if (menuBoard.exitingVia().action == Exit.Action.SETTINGS) {
            settingsMenuBoard.changeExitStateTo(Exit.Action.NULL);
            board = settingsMenuBoard;
            resetSettingsBoard();
            inSettings = true;
            inMainMenu = false;
            inRules = false;
        } else if (menuBoard.exitingVia().action == Exit.Action.MAIN_MENU) {
            board = mbStart;
            resetTheBoard();
            inSettings = false;
            inMainMenu = true;
            inRules = false;
        } else if (menuBoard.exitingVia().action == Exit.Action.RULES) {
            rulesMenuBoard.changeExitStateTo(Exit.Action.NULL);
            board = rulesMenuBoard;
            resetTheBoard();
            inSettings = false;
            inMainMenu = false;
            inRules = true;
        } else if (menuBoard.exitingVia().action == Exit.Action.EXIT) {
            inMainMenu = false;
            dispose();
            System.exit(0);
        }

    }

    private void writeMenuFont() {
        if (board instanceof MenuBoard) {
            BitmapFont font = new BitmapFont();
            batch.begin();

            // Find the various exit buttons and write their text above them
            MenuBoard menuBoard = (MenuBoard) board;

            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();

            for (Exit exit : menuBoard.exits) {
                float xPos = ((w * exit.getX()) / board.width) - (float) 0.5 * (w / board.width);
                float yPos = ((h * exit.getY()) / board.height) + (float) 1.5 * (h / board.height);

                if (exit.action == Exit.Action.SP_MENU) {
                    font.draw(batch, "Single Player", xPos, yPos);
                } else if (exit.action == Exit.Action.MP_MENU) {
                    font.draw(batch, "Multi-Player", xPos, yPos);
                } else if (exit.action == Exit.Action.SP_START) {
                    font.draw(batch, "New Game", xPos, yPos);
                } else if (exit.action == Exit.Action.SP_CONTINUE) {
                    font.draw(batch, "Continue Game", xPos, yPos);
                } else if (exit.action == Exit.Action.MP_START) {
                    font.draw(batch, "New Game", xPos, yPos);
                } else if (exit.action == Exit.Action.MP_CONTINUE) {
                    font.draw(batch, "Continue Game", xPos, yPos);
                } else if (exit.action == Exit.Action.SETTINGS) {
                    font.draw(batch, "    Settings", xPos, yPos);
                } else if (exit.action == Exit.Action.MAIN_MENU) {
                    font.draw(batch, "Main Menu", xPos, yPos);
                } else if (exit.action == Exit.Action.RULES) {
                    font.draw(batch, "      Help", xPos, yPos);
                } else if (exit.action == Exit.Action.EXIT) {
                    font.draw(batch, "       Exit", xPos, yPos);
                }
            }

            if (inSettings) {

                ArrayList<String> settings = new ArrayList<String>();

                settings.add("    Volume");
                settings.add("Hard Mode");
                settings.add("Enemy Speed");

                int i = 0;
                for (Box box : settingsMenuBoard.boxes) {
                    for (Area area : settingsMenuBoard.areas) {
                        if (box.getY() == area.getY() && box.getX() == area.getX()) {
                            float xPos = ((w * box.getX()) / board.width) - (float) 0.5 * (w / board.width);
                            float yPos = ((h * box.getY()) / board.height) + (float) 1.5 * (h / board.height);
                            font.draw(batch, settings.get(i), xPos, yPos);
                            i++;
                        }
                    }
                }
                float onXPos = ((w * board.areas.get(8).getX()) / board.width) - (float) 1.75 * (w / board.width);
                float onYPos = ((h * board.areas.get(8).getY()) / board.height) + (float) 0.6 * (h / board.height);
                float offXPos = ((w * board.areas.get(8).getX()) / board.width) + (float) 1.25 * (w / board.width);
                float offYPos = ((h * board.areas.get(8).getY()) / board.height) + (float) 0.6 * (h / board.height);
                font.draw(batch, "On", onXPos, onYPos);
                font.draw(batch, "Off", offXPos, offYPos);

            } else if (inMainMenu) {
                float xPos = ((w * 21) / board.width);
                float yPos = ((h * 9.5f) / board.height);
                font.draw(batch, "Total Steps: " + totalSteps, xPos, yPos);
            }
            batch.end();
        }
    }

    private void handleTwoPlayerWin() {
        mpActiveLevel++;
        board = mpBoards.get(mpActiveLevel - 1);
    }

    private void handleSinglePlayerWin() {
        spActiveLevel++;
        board = spBoards.get(spActiveLevel - 1);
    }

    private void renderSpritesNormally() {
        // Only do if statement if not in menu
        // Only render sprite if distance < cutoff
        // render all sprites
        batch.begin();
        for (Sprite sprite : wallSprites) {
            sprite.draw(batch);
        }
        for (Sprite sprite : areaSprites) {
            sprite.draw(batch);
        }
        for (Sprite sprite : boxSprites) {
            sprite.draw(batch);
        }

        playerSprite.draw(batch);
        if (board instanceof TwoPlayerBoard) {
            player2Sprite.draw(batch);
        }
        if (board instanceof MenuBoard) {
            for (Sprite sprite : menuSprites) {
                sprite.draw(batch);
            }
        }
        for (Sprite sprite : enemySprites) {
            sprite.draw(batch);
        }
        if (inMainMenu) {
            drawMoveHelper();
        }
        if (inRules) {
            drawRules();
        }
        batch.end();
    }

    private void renderSpritesWithFog() {
        // Only do if statement if not in menu
        // Only render sprite if distance < cutoff
        // render all sprites
        batch.begin();
        for (Sprite sprite : wallSprites) {
            if (withinDistance(sprite)) {
                sprite.draw(batch);
            }
        }
        for (Sprite sprite : areaSprites) {
            if (withinDistance(sprite)) {
                sprite.draw(batch);
            }
        }
        for (Sprite sprite : boxSprites) {
            if (withinDistance(sprite)) {
                sprite.draw(batch);
            }
        }

        playerSprite.draw(batch);
        if (board instanceof TwoPlayerBoard) {
            player2Sprite.draw(batch);
        }
        for (Sprite sprite : enemySprites) {
            if (withinDistance(sprite)) {
                sprite.draw(batch);
            }
        }

        batch.end();
    }

    private boolean withinDistance(Sprite sprite) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float spriteAx = (board.width * playerSprite.getX() / w);
        float spriteAy = (board.height * playerSprite.getY() / h);
        float spriteBx = (board.width * sprite.getX() / w);
        float spriteBy = (board.height * sprite.getY() / h);
        float distance = (float) Math.pow(Math.pow(spriteAx - spriteBx, 2) + Math.pow(spriteAy - spriteBy, 2), 0.5);
        if (distance < fogRadius) {
            return true;
        }
        if (board instanceof TwoPlayerBoard) {
            spriteAx = (board.width * player2Sprite.getX() / w);
            spriteAy = (board.height * player2Sprite.getY() / h);
            spriteBx = (board.width * sprite.getX() / w);
            spriteBy = (board.height * sprite.getY() / h);
            distance = (float) Math.pow(Math.pow(spriteAx - spriteBx, 2) + Math.pow(spriteAy - spriteBy, 2), 0.5);
            return distance < fogRadius;
        }
        return false;
    }

    private void drawMoveHelper() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        Sprite sprite = new Sprite(moveControlsImg);
        sprite.setSize(w * 5 / board.width, h * 2 / board.height);
        float xPos = ((w * 1) / board.width);
        float yPos = ((h * 8) / board.height);
        sprite.setPosition(xPos, yPos);
        sprite.draw(batch);
    }

    private void drawRules() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        Sprite sprite = new Sprite(helpImg);
        sprite.setSize((float) (w * (17.5) / board.width), h * 13 / board.height);
        float xPos = ((w * 14) / board.width);
        float yPos = ((h * 1) / board.height);
        sprite.setPosition(xPos, yPos);
        sprite.draw(batch);
    }

    private void detectMovements() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            board.player.move(Direction.LEFT, board);
            totalSteps++;
            stepsThisLevel++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            board.player.move(Direction.RIGHT, board);
            totalSteps++;
            stepsThisLevel++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            board.player.move(Direction.DOWN, board);
            totalSteps++;
            stepsThisLevel++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            board.player.move(Direction.UP, board);
            totalSteps++;
            stepsThisLevel++;
        }

        if (board instanceof TwoPlayerBoard) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                ((TwoPlayerBoard) board).player2.move(Direction.LEFT, board);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                ((TwoPlayerBoard) board).player2.move(Direction.RIGHT, board);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                ((TwoPlayerBoard) board).player2.move(Direction.DOWN, board);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                ((TwoPlayerBoard) board).player2.move(Direction.UP, board);
            }
        }
    }

    private void detectShortcuts() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetTheBoard();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            board = mbStart;
            resetTheBoard();
            inSettings = false;
            inRules = false;
            inMainMenu = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (board instanceof TwoPlayerBoard) {
                settingsMenuBoard.changeExitStateTo(Exit.Action.MP_CONTINUE);
            } else if (board instanceof MenuBoard) {
                settingsMenuBoard.changeExitStateTo(Exit.Action.NULL);
            } else {
                settingsMenuBoard.changeExitStateTo(Exit.Action.SP_CONTINUE);
            }
            board = settingsMenuBoard;
            resetSettingsBoard();
            inSettings = true;
            inRules = false;
            inMainMenu = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            if (board instanceof TwoPlayerBoard) {
                rulesMenuBoard.changeExitStateTo(Exit.Action.MP_CONTINUE);
            } else if (board instanceof MenuBoard) {
                rulesMenuBoard.changeExitStateTo(Exit.Action.NULL);
            } else {
                rulesMenuBoard.changeExitStateTo(Exit.Action.SP_CONTINUE);
            }
            board = rulesMenuBoard;
            resetRulesBoard();
            inSettings = false;
            inRules = true;
            inMainMenu = false;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        wallImg.dispose();
        areaImg.dispose();
        boxImg.dispose();
        playerImg.dispose();
        player2Img.dispose();
        ghostImg.dispose();
        gameSound.dispose();
    }

    // calculates where all the sprites are on the map
    private void recreateAllSprites() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        wallSprites = new ArrayList<Sprite>();
        boxSprites = new ArrayList<Sprite>();
        areaSprites = new ArrayList<Sprite>();
        enemySprites = new ArrayList<Sprite>();
        menuSprites = new ArrayList<Sprite>();

        for (Wall wall : board.walls) {
            Sprite sprite = new Sprite(wall.getImage());
            sprite.setSize(w / board.width, h / board.height);
            float xPos = ((w * wall.getX()) / board.width);
            float yPos = ((h * wall.getY()) / board.height);
            sprite.setPosition(xPos, yPos);
            wallSprites.add(sprite);
        }
        for (Box box : board.boxes) {
            Sprite sprite = new Sprite(box.getImage());
            sprite.setSize(w / board.width, h / board.height);
            float xPos = ((w * box.getX()) / board.width);
            float yPos = ((h * box.getY()) / board.height);
            sprite.setPosition(xPos, yPos);
            boxSprites.add(sprite);
        }
        playerSprite = new Sprite(board.player.getImage());
        playerSprite.setSize(w / board.width, h / board.height);
        float xPos = ((w * board.player.getX()) / board.width);
        float yPos = ((h * board.player.getY()) / board.height);
        playerSprite.setPosition(xPos, yPos);

        if (board instanceof TwoPlayerBoard) {
            player2Sprite = new Sprite(((TwoPlayerBoard) board).player2.getImage());
            player2Sprite.setSize(w / board.width, h / board.height);
            float p2XPos = ((w * ((TwoPlayerBoard) board).player2.getX()) / board.width);
            float p2YPos = ((h * ((TwoPlayerBoard) board).player2.getY()) / board.height);
            player2Sprite.setPosition(p2XPos, p2YPos);
        }
        for (Enemy enemy : board.ghosts) {
            Sprite sprite = new Sprite(enemy.getImage());
            sprite.setSize(w / board.width, h / board.height);
            float eXPos = ((w * enemy.getX()) / board.width);
            float eYPos = ((h * enemy.getY()) / board.height);
            sprite.setPosition(eXPos, eYPos);
            enemySprites.add(sprite);
        }
        for (Enemy enemy : board.zombies) {
            Sprite sprite = new Sprite(enemy.getImage());
            sprite.setSize(w / board.width, h / board.height);
            float eXPos = ((w * enemy.getX()) / board.width);
            float eYPos = ((h * enemy.getY()) / board.height);
            sprite.setPosition(eXPos, eYPos);
            enemySprites.add(sprite);
        }
        for (Area area : board.areas) {
            Sprite sprite = new Sprite(area.getImage());
            sprite.setSize(w / board.width, h / board.height);
            float aXPos = ((w * area.getX()) / board.width);
            float aYPos = ((h * area.getY()) / board.height);
            sprite.setPosition(aXPos, aYPos);
            areaSprites.add(sprite);
        }
        if (board instanceof MenuBoard) {
            for (Exit exit : ((MenuBoard) board).exits) {
                if (exit.action != Exit.Action.NULL) {
                    Sprite sprite = new Sprite(exit.getImage());
                    sprite.setSize(w / board.width, h / board.height);
                    float aXPos = ((w * exit.getX()) / board.width);
                    float aYPos = ((h * exit.getY()) / board.height);
                    sprite.setPosition(aXPos, aYPos);
                    menuSprites.add(sprite);
                }
            }
        }
    }

    private void resetSettingsBoard() {
        board.player.setX(settingsMBPlayerX);
        board.player.setY(settingsMBPlayerY);
    }

    private void resetRulesBoard() {
        board.player.setX(rulesMBPlayerX);
        board.player.setY(rulesMBPlayerY);
    }

    private void volumeControl() {
        int volumeBoxPos = board.boxes.get(0).getX() - 11;

        switch (volumeBoxPos) {
            case 0:
                volume = 0.0f;
                break;
            case 1:
                volume = 0.1f;
                break;
            case 2:
                volume = 0.3f;
                break;
            case 3:
                volume = 0.5f;
                break;
            case 4:
                volume = 0.6f;
                break;
            case 5:
                volume = 0.8f;
                break;
            case 6:
                volume = 1.0f;
                break;
        }
        gameSound.setVolume(gameSoundID, volume);
    }

    private void enemySpeed() {
        // Enemy box
        int enemyBoxPos = board.boxes.get(2).getX() - 11;
        float multiplier;

        switch (enemyBoxPos) {
            case 0:
                multiplier = 0.5f;
                break;
            case 1:
                multiplier = 0.8f;
                break;
            case 2:
                multiplier = 1.0f;
                break;
            case 3:
                multiplier = 1.25f;
                break;
            case 4:
                multiplier = 1.5f;
                break;
            case 5:
                multiplier = 1.75f;
                break;
            case 6:
                multiplier = 2.0f;
                break;
            default:
                multiplier = 1.0f;
                break;
        }
        timeBetweenEnemyMoves = (long) (baseTimeBetweenEnemyMoves / multiplier);
    }

    private void fogToggle() {
        // Fog box
        int fogBoxPos = board.boxes.get(1).getX() - 14;
        switch (fogBoxPos) {
            case 0:
                fogMode = true;
                break;
            case 1:
                fogMode = false;
                break;

        }
    }

    private MenuBoard defineLevels() {

        mbStart = new MenuBoard(FileIO.read("levels/menu-board1.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                exitImg,
                Board.ChallengeType.PLACEBOXES);

        spMenuBoard = new MenuBoard(FileIO.read("levels/menu-board2.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                exitImg,
                Board.ChallengeType.PLACEBOXES);

        mpMenuBoard = new MenuBoard(FileIO.read("levels/menu-board3.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                exitImg,
                Board.ChallengeType.PLACEBOXES);

        settingsMenuBoard = new MenuBoard(FileIO.read("levels/settings-menu.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                exitImg,
                Board.ChallengeType.PLACEBOXES);

        rulesMenuBoard = new MenuBoard(FileIO.read("levels/rules-menu.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                exitImg,
                Board.ChallengeType.PLACEBOXES);


        TwoPlayerBoard mp1 = new TwoPlayerBoard(FileIO.read("levels/multi-players1.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                "Bloggs",
                playerImg,
                player2Img,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.TRAPZOMBIES);


        TwoPlayerBoard mp2 = new TwoPlayerBoard(FileIO.read("levels/multi-players2.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                "Bloggs",
                playerImg,
                player2Img,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.TRAPZOMBIES);
        TwoPlayerBoard mp3 = new TwoPlayerBoard(FileIO.read("levels/multi-players3.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                "Bloggs",
                playerImg,
                player2Img,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.TRAPZOMBIES);

        Board sp1 = new Board(FileIO.read("levels/level01.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.PLACEBOXES);

        Board sp2 = new Board(FileIO.read("levels/level02.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.PLACEBOXES);

        Board sp3 = new Board(FileIO.read("levels/level03.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.PLACEBOXES);

        Board sp4 = new Board(FileIO.read("levels/level04.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.PLACEBOXES);

        Board sp5 = new Board(FileIO.read("levels/level05.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.PLACEBOXES);

        Board sp6 = new Board(FileIO.read("levels/level06.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.TRAPZOMBIES);

        Board sp6b = new Board(FileIO.read("levels/level06b.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.TRAPZOMBIES);

        Board sp6c = new Board(FileIO.read("levels/level06c.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.TRAPZOMBIES);

        Board sp7 = new Board(FileIO.read("levels/level07.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.PLACEBOXES);

        Board sp8 = new Board(FileIO.read("levels/level08.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.PLACEBOXES);

        Board sp9 = new Board(FileIO.read("levels/level09.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.PLACEBOXES);

        Board sp10 = new Board(FileIO.read("levels/level10.txt"),
                wallImg,
                areaImg,
                boxImg,
                "Joe",
                playerImg,
                ghostImg,
                zombieImg,
                boxImg,
                Board.ChallengeType.TRAPZOMBIES);

        spBoards.add(sp1);
        spBoards.add(sp2);
        spBoards.add(sp3);
        spBoards.add(sp4);
        spBoards.add(sp5);
        spBoards.add(sp6);
        spBoards.add(sp6b);
        spBoards.add(sp6c);
        spBoards.add(sp7);
        spBoards.add(sp8);
        spBoards.add(sp9);

        mpBoards.add(mp1);
        mpBoards.add(mp2);
        mpBoards.add(mp3);
        return mbStart;
    }

}