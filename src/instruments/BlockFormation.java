package instruments;

import biuoop.DrawSurface;
import display.Sprite;
import geometry.Point;
import geometry.Rectangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * In charge of the aliens's movement.
 */
public class BlockFormation implements Sprite {
    private List<List<Block>> blocks;
    private long startTime;
    private boolean reachedBound;

    /**
     * a constructor.
     */
    public BlockFormation() {
        this.blocks = new LinkedList<>();
        this.reachedBound = false;
    }

    /**
     * a getter.
     * @return true if the aliens reach for the shields.
     */
    public boolean isReachedBound() {
        return this.reachedBound;
    }

    /**
     * a setter for reachBound member.
     */
    public void setReachedBound() {
        this.reachedBound = !this.reachedBound;
    }

    /**
     * Increase all the aliens's speed.
     */
    public void setAllSpeed() {
        List<List<Block>> cpyList = new LinkedList<>(this.blocks);
        for (List<Block> list: cpyList) {
            for (Block block: list) {
                block.setSpeed();
            }
        }
    }

    /**
     * adding a block to the formation.
     * @param b a block
     */
    public void addBlock(Block b) {
        if (!this.blocks.isEmpty()) {
            for (List<Block> list: this.blocks) {
                if (list.get(0).getCollisionRectangle().getUpperLeft().getX()
                        == b.getCollisionRectangle().getUpperLeft().getX()) {
                    list.add(b);
                    return;
                }
            }
        }
        List<Block> temp = new LinkedList<>();
        temp.add(b);
        this.blocks.add(temp);
    }

    /**
     * removes a given block from the formation.
     * @param b a block.
     */
    public void removeBlock(Block b) {
        List<List<Block>> cpyList = new LinkedList<>(this.blocks);
        for (int i = 0; i < cpyList.size(); i++) {
            for (int j = 0; j < cpyList.get(i).size(); j++) {
                if (cpyList.get(i).get(j).equals(b)) {
                    cpyList.get(i).remove(cpyList.get(i).get(j));
                }
            }
            if (cpyList.get(i).isEmpty()) {
                cpyList.remove(cpyList.get(i));
            }
        }
        this.blocks = cpyList;
    }

    /**
     * a setter.
     * @param start the start-time of the game.
     */
    public void setStartTime(long start) {
        this.startTime = start;
    }
    /**
     * draw the sprite to the screen.
     *
     * @param d a draw surface
     */
    public void drawOn(DrawSurface d) {

    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt specifies the amount of seconds passed since the last call
     */
    public void timePassed(double dt) {
        List<List<Block>> cpyList = new LinkedList<>(this.blocks);
        List<Block> lowest = new LinkedList<>();
        boolean shouldChange = false;
        //checks if the aliens reach for the end of the screen.
        for (List<Block> list: cpyList) {
            for (Block b : list) {
                if ((b.getSpeed() > 0 && b.getCollisionRectangle().getUpperRight().getX() >= 800)
                        || (b.getSpeed() < 0 && b.getCollisionRectangle().getUpperLeft().getX() <= 0)) {
                    shouldChange = true;
                }
            }
            //saving a list of the lowest aliens from all the columns
            lowest.add(list.get(list.size() - 1));
            if (list.get(list.size() - 1).getCollisionRectangle().getLowerLeft().getY() >= 500) {
                this.reachedBound = true;
            }
        }
        //change the aliens speed, direction and position when they reach to the end of the screen.
        if (shouldChange) {
            for (List<Block> list: cpyList) {
                for (Block b : list) {
                    b.setSpeed();
                    b.changeDirection();
                    Point newUpperLeft = new Point(b.getCollisionRectangle().getUpperLeft().getX(),
                            b.getCollisionRectangle().getUpperLeft().getY() + 20);
                    b.setBlock(new Rectangle(newUpperLeft, b.getCollisionRectangle().getWidth(),
                            b.getCollisionRectangle().getHeight()));
                }
            }
        }
        //shoot a bullet from a random alien every 0.5 seconds.
        if (System.currentTimeMillis() - this.startTime > 500) {
            Random rand = new Random();
            this.startTime = System.currentTimeMillis();
            if (this.blocks.size() > 1) {
                lowest.get(rand.nextInt(this.blocks.size())).shoot();
            } else if (this.blocks.size() == 1) {
                lowest.get(0).shoot();
            }
        }
    }

    /**
     * reset all the aliens's position to the start point.
     */
    public void resetPlace() {
        for (List<Block> list: this.blocks) {
            for (Block block: list) {
                Point newUpperLeft = block.getInitialPosition();
                Rectangle rectangle = new Rectangle(newUpperLeft, block.getWidth(),
                        block.getHeight());
                block.setBlock(rectangle);
                block.resetSpeed();
            }
        }
    }
}
