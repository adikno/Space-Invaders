package game;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import display.Animation;
import display.AnimationRunner;
import display.CountDownAnimation;
import display.KeyPressStoppableAnimation;
import display.LevelNameIndicator;
import display.LivesIndicator;
import display.PauseScreen;
import display.ScoreIndicator;
import display.Sprite;
import display.SpriteCollection;
import geometry.Point;
import geometry.Rectangle;
import instruments.Ball;
import instruments.Block;
import instruments.BlockFormation;
import instruments.Paddle;
import instruments.Velocity;
import tools.BallRemover;
import tools.BlockRemover;
import tools.Collidable;
import tools.Counter;
import tools.HitListener;
import tools.ScoreTrackingListener;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Adi Knobel 209007087
 * @version "2.0, 17/04/18"
 */
public class GameLevel implements HitListener, Animation {
    /**
     * a default height for a block.
     */
    public static final int HEIGHT_BLOCK = 25;
    /**
     * a default width for a frame-block.
     */
    public static final int WIDTH_FRAME = 25;
    /**
     * height of a paddle.
     */
    public static final int HEIGHT_PADDLE = 20;

    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter remainingBlocks;
    private Counter ballsCounter;
    private Counter score;
    private Counter numOfLives;
    private Paddle paddle;
    private AnimationRunner runner;
    private biuoop.KeyboardSensor keyboard;
    private boolean running;
    private LevelInformation information;
    private List<Block> blocks;
    private long startTime;
    private BlockFormation formation;
    private List<Ball> balls;

    /**
     * This function is a constructor of class Game.
     * @param levelInformation the level information
     * @param ar the animation runner
     * @param key a keyboard sensor
     * @param livesCounter a lives counter
     * @param score the score counter
     */
    public GameLevel(LevelInformation levelInformation, AnimationRunner ar, KeyboardSensor key,
                     Counter livesCounter, Counter score) {
        this.information = levelInformation;
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.remainingBlocks = new Counter(this.information.numberOfBlocksToRemove());
        this.ballsCounter = new Counter();
        this.numOfLives = livesCounter;
        this.score = score;
        this.keyboard = key;
        this.runner = ar;
        this.running = true;
        this.blocks = new LinkedList<>();
        this.formation = new BlockFormation();
        this.balls = new LinkedList<>();
    }

    /**
     * adding a collidable object to the collection.
     * @param c a collidable object
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * adding a sprite object to the collection.
     * @param s sprite object
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Removing Collidable object from the game-environment.
     * @param c the collidable
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * Removing Sprite object from the Sprite list.
     * @param s the sprite
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * Shoot a user's ball.
     */
    public void shoot() {
        Ball ball = new Ball(this.environment, 4, new Point(this.paddle.getCollisionRectangle().getUpperLeft().getX()
                + this.paddle.getCollisionRectangle().getWidth() / 2,
                this.paddle.getCollisionRectangle().getUpperLeft().getY() - 3));
        ball.setVelocity(Velocity.fromAngleAndSpeed(0, 500));
        ball.addToGame(this);
        this.balls.add(ball);
        this.ballsCounter.increase(1);
    }

    /**
     * Shoot an alien's ball.
     * @param enemy the shooter alien
     */
    public void shootMe(Block enemy) {
        Ball ball = new Ball(this.environment, new Point(enemy.getCollisionRectangle().getUpperLeft().getX()
                + enemy.getWidth() / 2,
                enemy.getCollisionRectangle().getLowerLeft().getY() + 3));
        ball.setVelocity(Velocity.fromAngleAndSpeed(180, 500));
        this.balls.add(ball);
        this.addSprite(ball);
    }

    /**
     * Initialize a new game: create the Blocks, paddle, two Balls and add them to the game.
     */
    public void initialize() {
        BlockRemover blockRemover = new BlockRemover(this, this.remainingBlocks);
        BallRemover ballRemover = new BallRemover(this, this.ballsCounter);
        ScoreTrackingListener tracking = new ScoreTrackingListener(this.score);

        Block indicator = new Block(new Rectangle(new Point(0, 0),
                800, HEIGHT_BLOCK), Color.white);
        ScoreIndicator scoreIndicator = new ScoreIndicator(this.score);
        LivesIndicator livesIndicator = new LivesIndicator(this.numOfLives);
        LevelNameIndicator nameIndicator = new LevelNameIndicator(this.information.levelName());

        //adding the background to the sprites-list
        this.sprites.addSprite(this.information.getBackground());

        //creating the frame of the screen
        Block deathBlock = new Block(new Rectangle(new Point(0, 600 + 10), 800, 10), Color.black);
        deathBlock.addToGame(this);
        deathBlock.addHitListener(ballRemover);

        indicator.addToGame(this);
        indicator.addHitListener(ballRemover);
        scoreIndicator.addToGame(this);
        livesIndicator.addToGame(this);
        nameIndicator.addToGame(this);

        //adding the shields to the game.
        List<Block> shields = buildShields();
        for (Block b: shields) {
            b.addHitListener(blockRemover);
            b.addHitListener(tracking);
            b.addToGame(this);
        }

        //adding the blocks from the level-information to the game
        this.blocks.addAll(this.information.blocks());
        for (Block b: this.blocks) {
            b.addHitListener(blockRemover);
            b.addHitListener(tracking);
            b.addToGame(this);
            this.formation.addBlock(b);
        }
        this.sprites.addSprite(this.formation);

        Point location = new Point((800 - this.information.paddleWidth()) / 2,
                600 - HEIGHT_PADDLE);
        this.paddle = new Paddle(new Rectangle(location, this.information.paddleWidth(), HEIGHT_PADDLE),
                this.information.paddleSpeed(), this.keyboard, Color.orange, this);
        this.paddle.addToGame(this);
        this.sprites.addPaddle(this.paddle);
    }

    /**
     * Build shields.
     * @return List of blocks composing three shields.
     */
    private List<Block> buildShields() {
        List<Block> shields = new LinkedList<>();
        for (int i = 90; i < 560; i += 225) {
            for (int j = 0, y = 500; j < 3; j++, y += 5) {
                for (int k = 0, x = i; k < 30; k++, x += 5) {
                    Block b = new Block(new Rectangle(new Point(x, y), 5, 5), Color.cyan, 0);
                    shields.add(b);
                }
            }
        }
        return shields;
    }

    /**
     * Run one turn of the level.
     */
    public void playOneTurn() {
        this.paddle.moveToCenter();
        // countdown before turn starts.
        this.runner.run(new CountDownAnimation(2, 3, this.sprites));
        // use our runner to run the current display -- which is one turn of the game.
        this.running = true;
        this.startTime = System.currentTimeMillis();
        //reset the alien's position
        this.formation.setStartTime(System.currentTimeMillis());
        this.runner.run(this);
    }

    /**
     * a getter.
     * @return the blockFormation object.
     */
    public BlockFormation getFormation() {
        return this.formation;
    }

    /**
     * Removing all the blocks from the game when it over.
     */
    public void removeAllBlocks() {
        for (Block block: this.blocks) {
            block.removeFromGame(this);
        }
    }

    /**
     * Return the remaining number of blocks.
     * @return the remaining number of blocks.
     */
    public int getRemainingBlocks() {
        return this.remainingBlocks.getValue();
    }

    /**
     * This method is called whenever the beingHit object is hit.
     * @param beingHit the block that is being hitted
     * @param hitter the Ball that's doing the hitting
     */
    public void hitEvent(Block beingHit, Ball hitter) {

    }

    /**
     * Draw all the sprites and notified all that time passed.
     * Check whether the animation runner should stop.
     * @param d the draw-surface
     * @param dt specifies the amount of seconds passed since the last call
     */
    public void doOneFrame(DrawSurface d, double dt) {
        if (this.formation.isReachedBound()) {
            this.running = false;
            this.numOfLives.decrease(1);
            this.formation.resetPlace();
            this.formation.setReachedBound();
            removeAllBalls();
        }
        if (this.paddle.beingHit()) {
            this.running = false;
            this.numOfLives.decrease(1);
            this.paddle.setBeingHit();
            this.formation.resetPlace();
            removeAllBalls();
        }
        if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
            long currTime = System.currentTimeMillis();
            if (currTime - this.startTime >= 350) {
                shoot();
                this.startTime = currTime;
            }
        }
        if (this.remainingBlocks.getValue() == 0) {
            this.running = false;
            removeAllBalls();
            this.formation.setAllSpeed();
            this.formation.resetPlace();
        }
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, new PauseScreen()));
        }
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed();
    }

    /**
     * removes all the balls from the game.
     */
    private void removeAllBalls() {
        this.ballsCounter.reset();
        for (Ball ball: this.balls) {
            ball.removeFromGame(this);
        }
    }

    /**
     * Return false if the game should keep running and true otherwise.
     * @return false if the game should keep running and true otherwise.
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * preparing the boolean members for reuse.
     */
    public void setShouldStop() {
        this.running = true;
    }
}