New Classes:
    BlockFormation - In charge of coordinate the aliens group's movement.
    Level - A class that implements 'levelInformation' interface from the last assignment -
            In charge of creates all the blocks (the aliens) and other information of the level.

Other classes (from ass7):

Display package:
    AnimationRunner - run animations.
    CountDownAnimation - count down animation.
    HighScoresAnimation - high-scores animation.
    EndScreen - display 'game over' and the score on the screen at the end of a game.
    HighScoresTable - manage the high-scores table file.
    KeyPressStoppableAnimation - holds a key-pressed-stoppable animation, in charge of prevent skipping on screen
                                 when the player pressed too long press.
    LevelNameIndicator - displays the level name on the screen.
    LivesIndicator - displays the lives on the screen.
    MenuAnimation - the menu animation.
    PauseScreen - the pause screen.
    ScoreIndicator - displays the current score on the screen.
    ScoreInfo - union information about a score.
    ShowHiScoresTask - shows the high scores animation.
    SpriteCollection - holds a collection of all the draw-able objects in the game.

Game package:
    GameEnvironment - holds a collection of all the collide-able objects in the game.
    GameLevel - initializes the game.
    RunGameTask - run the game until it ends.

Geometry package:
    Circle - a circle.
    Line - a line.
    Point - a point.
    Rectangle - a rectangle.

Instruments package:
    BackGround - holds a list of sprites constructed the background of the game.
    Ball - the bullets in the game. (a red one can't kill aliens)
    Block - the frame, the aliens and the shields.
    Paddle - the paddle.
    Selection - selection for the menu.
    Velocity - the velocity of a ball.

Tools package:
    BallRemover - in charge of removing balls after they hit something / the level stopped.
    BlockRemover - in charge of removing blocks after they have being hit.
    CollisionInfo - a collision information.
    Counter - a counter.
    ScoreTrackingListener - in charge of updating the score during the game.

Ass7Game - The main class

Interfaces (all from ass7):
    Animation - Class that can run until something happens.
    Menu - Class that execute tasks in accordance to the user's selections.
    Sprite - A draw-able Object - makes object visual on the screen.
    Task - Hold a class that have to do something.
    LevelInformation - Hold a description of a level in the game
    Collidable - A collide-able object - a ball will notice them and change his direction when a hit event happens.
    HitListener - In charge of notice whenever a hit event happens.
    HitNotifier - In charge of updating the relevant objects when a hit event happens.

Brief description of how I implemented the following:
 (a) the Aliens formation -
        I added a speed-member to the Block class, which I initialized at the beginning of the game,
        and increased it every time the aliens change direction and at the end of each level.
        In addition, I implemented the 'timePassed' function in Block's class, by moving the block speed * dt units
        to the current direction.
        Meanwhile, I implemented a sprite called 'BlockFormation' that hold a list of lists of block.
        (Each list represent a different column of aliens.)
        Using 'timePassed' function, I checked when an external column of aliens reach to a wall and when it happens
        I (1) - increase the aliens's speed, (2) - change their direction, (3) push them down a bit.
        When the user looses a life, the 'gameLevel' class invoke 'resetPlace' method of 'blockFormation'. this method
        in charge of reset all the aliens to their original position using a member called 'initialPosition' in class
        block.

 (b) the shields -
        I implemented a method in 'GameLevel' class called 'buildShields', inside this method I built three shields,
        each of them constructed by 90 tiny blocks (3 rows of 30 block each).

 (c) shots by aliens -
        I implemented a method in 'GameLevel' class called 'shootMe' that gets a block (an alien), and creates a red ball
        from the alien's position with an angle of 180 degrees.
        This method is invoked from 'BlockFormation' class every 0.5 seconds by a random column of aliens.
        That happens inside 'timePassed' method that in every invocation:
        (1) Makes a list of the lowest aliens from all the columns.
        (2) Using 'startTime' member and the 'System.currentTimeMillis()' method, sends a random node from the
         lowest-aliens-list to 'shootMe' method every 0.5 seconds.

 (d) shots by player -
        I implemented a method in 'GameLevel' class called 'shoot' that invoked whenever the player pressed the space-key.
        Inside this method a white ball is created from the paddle's position with an angle of 0 degrees.