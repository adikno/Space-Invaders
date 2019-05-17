package game;

import display.Sprite;
import geometry.Point;
import geometry.Rectangle;
import instruments.BackGround;
import instruments.Block;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A class that holds information about a level in the game.
 */
public class Level implements LevelInformation {
    private final int speed;
    private int battle;

    /**
     * A constructor.
     * @param battle the number of the battle.
     * @param speed the initial speed of the aliens's movement.
     */
    public Level(int battle, int speed) {
        this.battle = battle;
        this.speed = speed;
    }

    /**
     * Return the speed of the paddle.
     *
     * @return the speed of the paddle
     */
    public int paddleSpeed() {
        return 400;
    }

    /**
     * Return the width of the paddle.
     *
     * @return the width of the paddle.
     */
    public int paddleWidth() {
        return 80;
    }

    /**
     * Return the level-name.
     *
     * @return the level-name.
     */
    public String levelName() {
        return "Battle no:" + this.battle;
    }

    /**
     * Returns a sprite with the background of the level.
     *
     * @return a sprite with the background of the level.
     */
    public Sprite getBackground() {
        Block block = new Block(new Rectangle(new Point(0, 0), 800, 600), Color.black);
        List<Sprite> sprites = new LinkedList<>();
        sprites.add(block);
        return new BackGround(sprites);
    }

    /**
     * A list of Blocks that make up this level.
     *
     * @return a list of Blocks that make up this level
     */
    public List<Block> blocks() {
        List<Block> blocks = new LinkedList<>();
        Image image = null;
        try {
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("enemy.png");
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println("failed reading file.");
        }
        for (int i = 0, x = 0, y = 25; i < 10; i++, x += 50) {
            Block b = new Block(new Rectangle(new Point(x, y), 40, 30), image, 1, this.speed);
            blocks.add(b);
            for (y = 65; y < 200; y += 40) {
                Block b2 = new Block(new Rectangle(new Point(x, y), 40, 30), image, 1, this.speed);
                blocks.add(b2);
            }
            y = 25;
        }
        return blocks;
    }

    /**
     * Return the number of blocks that should be removed before the level is considered to be "cleared".
     *
     * @return the number of blocks that should be removed before the level is considered to be "cleared".
     */
    public int numberOfBlocksToRemove() {
        return 50;
    }
}
