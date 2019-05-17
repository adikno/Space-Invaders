package instruments;

import biuoop.DrawSurface;
import display.Sprite;
import game.GameLevel;
import geometry.Point;
import geometry.Rectangle;
import tools.Collidable;
import tools.HitListener;
import tools.HitNotifier;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Adi Knobel 209007087
 * @version "1.0, 17/04/18"
 */
public class Block implements Collidable, Sprite, HitNotifier, Comparable {
    private Rectangle block;
    private Color fill;
    private int numOfHits;
    private List<HitListener> hitListeners;
    private Image img;
    private double speed;
    private GameLevel game;
    private double initialSpeed;
    private Point initialPosition;

    /**
     * This function is a constructor of class Block.
     * @param rec a rectangle object holds details about the size and the place of the block
     * @param c the color of the block
     */
    public Block(Rectangle rec, Color c) {
        this.block = rec;
        this.fill = c;
        this.numOfHits = -1;
        this.hitListeners = new ArrayList<>();
        this.img = null;
    }

    /**
     * This function is a constructor of class Block.
     * @param rec a rectangle object holds details about the size and the place of the block
     * @param img the image of the block
     * @param numOfHits the number of hits till the block will be removed
     * @param speed the speed of the block
     */
    public Block(Rectangle rec, Image img, int numOfHits, int speed) {
        this.block = rec;
        this.img = img;
        this.fill = null;
        this.hitListeners = new ArrayList<>();
        this.numOfHits = numOfHits;
        this.speed = speed;
        this.initialSpeed = speed;
        this.initialPosition = rec.getUpperLeft();
    }

    /**
     * This function is a constructor of class Block.
     * @param rec a rectangle object holds details about the size and the place of the block
     * @param color the color of the block
     * @param numOfHits the number of hits till the block will be removed
     */
    public Block(Rectangle rec, Color color, int numOfHits) {
        this.block = rec;
        this.fill = color;
        this.hitListeners = new ArrayList<>();
        this.numOfHits = numOfHits;
    }

    /**
     * a getter.
     * @return the initial position of the block, (an alien).
     */
    public Point getInitialPosition() {
        return initialPosition;
    }

    /**
     * a setter.
     * @param gameLevel the game.
     */
    public void setGame(GameLevel gameLevel) {
        this.game = gameLevel;
    }

    /**
     * a getter.
     * @return the color of the block.
     */
    public Color getColor() {
        return this.fill;
    }

    /**
     * a getter.
     * @return the height of the block
     */
    public double getHeight() {
        return this.block.getHeight();
    }

    /**
     * a getter.
     * @return the width of the block
     */
    public double getWidth() {
        return this.block.getWidth();
    }

    /**
     * Return the current number of hits the block can absorb.
     * @return the current number of hits the block can absorb.
     */
    public int getNumOfHits() {
        return this.numOfHits;
    }

    /**
     * a getter.
     * @return the speed of the block.
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Return the "collision shape" of the object.
     * @return the "collision shape" of the object

     */
    public Rectangle getCollisionRectangle() {
        return this.block;
    }

    /**
     * calculates the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     * (while considering a possible deviation of Line.DEVIATION in the calculation of the collision point).
     * @param collisionPoint the collision point
     * @param currentVelocity the current velocity of the ball
     * @param hitter the Ball that's doing the hitting.
     * @return the new velocity expected after the hit.
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        //if the shooter is an alien and the beingHit is an alien - don't kill.
        if (hitter.getColor() == Color.red && this.fill == null) {
            return currentVelocity;
        }
        notifyHit(hitter);
        return currentVelocity;
    }

    /**
     * draw the sprite to the screen in accordance to the current numOfHits and the instructions in the file.
     * @param d a draw surface
     */
    public void drawOn(DrawSurface d) {
        if (this.numOfHits != 1) {
            d.setColor(this.fill);
            this.block.fillRec(d);
        } else {
            d.drawImage((int) this.block.getUpperLeft().getX(), (int) this.block.getUpperLeft().getY(), this.img);
        }
    }

    /** notify the sprite that time has passed.
     * @param dt specifies the amount of seconds passed since the last call
     */
    public void timePassed(double dt) {
        Point newUpperLeft = new Point(this.block.getUpperLeft().getX() + this.speed * dt,
                this.block.getUpperLeft().getY());
        this.block = new Rectangle(newUpperLeft, this.block.getWidth(), this.block.getHeight());
    }

    /**
     * a setter for the rectangle composed the block.
     * @param rec a rectangle
     */
    public void setBlock(Rectangle rec) {
        this.block = rec;
    }

    /**
     * adds the block to the game.
     * @param gameLevel a game object.
     */
    public void addToGame(GameLevel gameLevel) {
        game.addCollidable(this);
        game.addSprite(this);
        this.game = gameLevel;
    }

    /**
     * Removing the block from the game.
     * @param gameLevel the game
     */
    public void removeFromGame(GameLevel gameLevel) {
        this.removeHitListener(gameLevel);
        this.hitListeners = new LinkedList<>();
        gameLevel.getFormation().removeBlock(this);
        gameLevel.removeCollidable(this);
        gameLevel.removeSprite(this);
    }

    /**
     * Adding a hit-listener to the block.
     * @param hl new hit-listener.
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * Removing a hit-listener from the block.
     * @param hl old hit-listener.
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * Notify all the hit-listeners that a hit event happened.
     * @param hitter the Ball that's doing the hitting.
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
        hitter.removeFromGame(this.game);
    }

    /**
     * changes the direction of the block's movement.
      */
    public void changeDirection() {
        this.speed *= -1;
    }

    /**
     * increases the block's speed.
     */
    public void setSpeed() {
        this.speed *= 1.1;
    }

    /**
     * reset the block's speed to it's original value.
     */
    public void resetSpeed() {
        this.speed = this.initialSpeed;
    }

    /**
     * shoot a bullet out of the block (the alien).
     */
    public void shoot() {
        this.game.shootMe(this);
    }

    /**
     * compareTo method.
     * @param o an object
     * @return 0 when the objects are equals, 1 otherwise.
     */
    @Override
    public int compareTo(Object o) {
        if (o == this) {
            return 0;
        }
        return 1;
    }
}
