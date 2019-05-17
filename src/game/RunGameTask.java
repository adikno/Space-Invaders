package game;

import biuoop.DialogManager;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import display.AnimationRunner;
import display.EndScreen;
import display.HighScoresTable;
import display.KeyPressStoppableAnimation;
import display.ScoreInfo;
import display.Task;
import tools.Counter;

import java.io.File;
import java.io.IOException;


/**
 * in charge of running a level-set.
 */
public class RunGameTask implements Task<Void> {
    private AnimationRunner runner;
    private KeyboardSensor keyboard;
    private Counter score;
    private HighScoresTable table;
    private GUI gui;
    private File filename;

    /**
     * a constructor.
     * @param runner an animation runner
     * @param keyboard a keyboard sensor
     * @param score a score counter
     * @param filename a file of high-scores
     * @param table a high-score table
     * @param gui gui
     */
    public RunGameTask(AnimationRunner runner, KeyboardSensor keyboard, File filename,
                       HighScoresTable table, GUI gui, Counter score) {
        this.runner = runner;
        this.keyboard = keyboard;
        this.score = score;
        this.table = table;
        this.gui = gui;
        this.filename = filename;
    }

    /**
     * In charge of run the levels.
     * @return null as return value
     */
    public Void run() {
        Counter livesCounter = new Counter(3);
        int i = 1;
        int speed = 70;
        while (livesCounter.getValue() > 0) {
            LevelInformation level = new Level(i, speed);
            GameLevel game = new GameLevel(level, this.runner, this.keyboard, livesCounter, this.score);
            game.initialize();

            while (livesCounter.getValue() > 0 && game.getRemainingBlocks() > 0) {
                game.playOneTurn();
            }

            if (livesCounter.getValue() == 0) {
                game.removeAllBlocks();
            }
            i++;
            speed *= 1.2;
        }
        this.runner.run(new KeyPressStoppableAnimation(this.keyboard,
                KeyboardSensor.SPACE_KEY, new EndScreen(this.score, livesCounter)));
        if (this.table.getRank(this.score.getValue()) <= this.table.size()) {
            DialogManager dialog = this.gui.getDialogManager();
            String name = dialog.showQuestionDialog("Name", "What is your name?", "");
            this.table.add(new ScoreInfo(name, this.score.getValue()));
            try {
                this.table.save(this.filename);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        this.score.reset();
        return null;
    }
}
