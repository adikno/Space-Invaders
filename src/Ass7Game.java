import biuoop.GUI;
import biuoop.KeyboardSensor;
import display.AnimationRunner;
import display.HighScoresAnimation;
import display.HighScoresTable;
import display.KeyPressStoppableAnimation;
import display.Menu;
import display.MenuAnimation;
import display.ShowHiScoresTask;
import display.Task;
import game.RunGameTask;
import tools.Counter;

import java.io.File;
/**
 * the main class.
 */
public class Ass7Game {
    /**
     * a default WIDTH for a screen.
     */
    public static final int WIDTH_SCREEN = 800;
    /**
     * a default height for a screen.
     */
    public static final int HEIGHT_SCREEN = 600;

    /**
     * the main method.
     * @param args an array of strings.
     */
    public static void main(String[] args) {

        //search for high scores table
        File filename = new File("highscores.txt");
        HighScoresTable scores = HighScoresTable.loadFromFile(filename);

        GUI gui = new GUI("Space Invaders", WIDTH_SCREEN, HEIGHT_SCREEN);
        KeyboardSensor sensor = gui.getKeyboardSensor();
        AnimationRunner runner = new AnimationRunner(gui, 60);

        /**
         * an anonymous class for exit-Task.
         */
        Task<Void> exitTask = new Task<Void>() {
            /**
             * in charg of execute the task.
             *
             * @return null
             */
            public Void run() {
                System.exit(0);
                return null;
            }
        };
        Counter score = new Counter();

        Task<Void> hiScoresTask = new ShowHiScoresTask(runner,
                new KeyPressStoppableAnimation(sensor, KeyboardSensor.SPACE_KEY, new HighScoresAnimation(scores)));

        Menu<Task<Void>> menu = new MenuAnimation(sensor);

        Task<Void> runGameTask = new RunGameTask(runner, sensor, filename, scores, gui, score);

        menu.addSelection("s", "Start game", runGameTask);
        menu.addSelection("h", "High Scores", hiScoresTask);
        menu.addSelection("q", "Quit", exitTask);

        while (true) {
            runner.run(menu);
            // wait for user selection
            Task<Void> task = menu.getStatus();
            if (task != hiScoresTask) {
                task.run();
            }
            hiScoresTask.run();
        }
    }
}
