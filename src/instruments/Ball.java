package instruments;

import biuoop.DrawSurface;
import display.Sprite;
import game.GameEnvironment;
import game.GameLevel;
import geometry.Line;
import geometry.Point;
import tools.CollisionInfo;

import java.awt.Color;

/**Ball is a moving circle with a center and a size.
 * @author Adi Knobel 209007087
 * @version "2.0, 17/04/18"
 */
public class Ball implements Sprite {
    private int size;
    private Color color;
    private Point center;
    private Velocity velocity;
    private GameEnvironment gameEnv;
    private Line trajectory;
    private Velocity tempV;
    /**
     * This function is a constructor of class Ball.
     *
     * @param place is the center of ball
     * @param r      is the radios of the ball
     * @param c  is the color of the ball
     */
    public Ball(Point place, int r, Color c) {
        this.center = place;
        this.size = r;
        this.color = c;
        this.gameEnv = new GameEnvironment();
    }

    /**
     * This function is a constructor of class Ball.
     *
     * @param x     is the 'x' value of the center
     * @param y     is the 'y' value of the center
     * @param r     is the radios of the ball
     * @param c is the color of the ball
     */
    public Ball(double x, double y, int r, Color c) {
        this.center = new Point(x, y);
        this.size = r;
        this.color = c;
    }

    /**
     * This function is a constructor of class Ball.
     *
     * @param environment the area that the ball can bounce in
     * @param r  is the radios of the ball
     * @param place the center of the ball
     */
    public Ball(GameEnvironment environment, int r, Point place) {
        this.gameEnv = environment;
        this.size = r;
        this.center = place;
        this.color = Color.white;
    }

    /**
     * A constructor.
     * @param environment the area that the ball can bounce in
     * @param place the center of the ball.
     */
    public Ball(GameEnvironment environment, Point place) {
        this.gameEnv = environment;
        this.size = 5;
        this.center = place;
        this.color = Color.red;
    }

    /**
     * This function returns the 'x' value of the center of the ball.
     *
     * @return the 'x' value of the center of the ball.
     */
    public double getX() {
        return this.center.getX();
    }

    /**
     * This function returns the 'y' value of the center of the ball.
     *
     * @return the 'y' value of the center of the ball.
     */
    public double getY() {
        return this.center.getY();
    }

    /**
     * This function returns the color of the ball.
     *
     * @return the color of the ball.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * This function returns the velocity of the ball.
     *
     * @return the velocity of the ball.
     */
    public Velocity getTempV() {
        return this.tempV;
    }

    /**
     * This function draws the ball on the given DrawSurface.
     *
     * @param surface a DrawSurface
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle((int) this.getX(), (int) this.getY(), this.size);
        surface.setColor(Color.black);
        surface.drawCircle((int) this.getX(), (int) this.getY(), this.size);
    }

    /**
     * This function set the velocity of the ball.
     *
     * @param v the velocity of the ball
     */
    public void setVelocity(Velocity v) {
        this.velocity = v;
    }

    /**
     * The function check whether the ball will hit a collidable object at his next move.
     * if so - move the center of the ball to "almost" the hit point and update it's velocity,
     * if not - move it one step forward.
     * In case the use didn't invoke setVelocity before the function - throws exception.
     */
    public void moveOneStep() {
        try {
            //creating a line starting at current location,
            //and ending where the velocity will take the ball if no collisions will occur
            this.trajectory = new Line(this);
            //details about the imminent collision if there is one
            CollisionInfo currCollision = this.gameEnv.getClosestCollision(this.trajectory);
            /*
            if there is a collision expected - update the ball's center and velocity accordingly
             */
            if (currCollision != null) {
                //changing the velocity accordingly
                this.velocity = currCollision.collisionObject().hit(this, currCollision.collisionPoint(),
                        this.velocity);
            } else {
                this.center = this.tempV.applyToPoint(this.center);
            }
        } catch (Exception e) {
            System.out.println("Error: The velocity isn't defined.");
        }
    }
    /**
     notify the sprite that time has passed.
     @param dt specifies the amount of seconds passed since the last call
      */
    public void timePassed(double dt) {
        this.tempV = new Velocity(this.velocity.getDx() * dt, this.velocity.getDy() * dt);
        this.moveOneStep();
    }

    /**
     * adding the ball to the game.
     * @param game a game object
     */
    public void addToGame(GameLevel game) {
        game.addSprite(this);
    }

    /**
     * Removing the ball from the game.
     * @param game the game
     */
    public void removeFromGame(GameLevel game) {
        game.removeSprite(this);
    }
}