package display;

import biuoop.DrawSurface;
import instruments.Paddle;

import java.util.LinkedList;

/**
 * @author Adi Knobel 209007087
 * @version "2.0, 17/04/18"
 */
public class SpriteCollection {
    private LinkedList<Sprite> list = new LinkedList<>();
    private Paddle paddle;

    /**
     * adding a sprite object to the collection.
     * @param s a sprite object
     */
    public void addSprite(Sprite s) {
        list.add(s);
    }

    /**
     * add a paddle to the collection.
     * @param p the paddle
     */
    public void addPaddle(Paddle p) {
        this.paddle = p;
    }
    /**
     * Removing sprite from the list.
     * @param s a sprite.
     */
    public void removeSprite(Sprite s) {
        list.remove(s);
    }

    /**
     * call timePassed() on all sprites.
     */
    public void notifyAllTimePassed() {
        LinkedList<Sprite> sprites = new LinkedList<>(list);
        double dt = 1;
        dt /= 60;
        for (Sprite sp: sprites) {
            sp.timePassed(dt);
        }
        this.paddle.timePassed(dt);
    }

    /**
     * call drawOn(d) on all sprites.
     * @param d a draw surface
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite obj: this.list) {
            obj.drawOn(d);
        }
    }
}
