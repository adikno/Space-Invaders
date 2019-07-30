# Space-Invaders
An implementation for the classic Space Invaders game.

The game was implemented based on my Arkanoid's infrastructure, since both have a lot in common. 
With the addition of some significant features and with the change of some small things, a fundamentally different game was created.

You can compile the program and generates a portable jar using the included makefile.

### Description of the implementation
A brief description of how I implemented the game:
1. the Aliens formation -
	- I added a speed-member to the Block class, which I initialized at the beginning of the game, and increased it every time the aliens change direction and at the end of each level.
	- In addition, I implemented the 'timePassed' function in Block's class, by moving the block speed*dt units to the current direction.
	- Meanwhile, I implemented a sprite called 'BlockFormation' that hold a list of lists of block. (Each list represent a different column of aliens.)
	- Using 'timePassed' function, I checked when an external column of aliens reach to a wall and when it happens I:
		- increase the aliens's speed.
		- change their direction.
		- push them down a bit.
	- When the user loses a life, the 'gameLevel' class invoke 'resetPlace' method of 'blockFormation'. This method in charge of reset all the aliens to their original position using a member called 'initialPosition' in class block.
	2. the shields -
		I implemented a method in 'GameLevel' class called 'buildShields', inside this method I built
		three shields, each of them constructed by 90 tiny blocks (3 rows of 30 block each).
	3. shots by aliens -
		I implemented a method in 'GameLevel' class called 'shootMe' that gets a block (an alien), and
		creates a red ball from the alien's position with an angle of 180 degrees.
		This method is invoked from 'BlockFormation' class every 0.5 seconds by a random column of
		aliens.
		That happens inside 'timePassed' method that in every invocation:
			a. Makes a list of the lowest aliens from all the columns.
			b. Using 'startTime' member and the 'System.currentTimeMillis()' method, sends a
			   random node from the lowest-aliens-list to 'shootMe' method every 0.5 seconds.
	4. shots by player -
	   I implemented a method in 'GameLevel' class called 'shoot' that invoked whenever the player
	   pressed the space-key.
	   Inside this method a white ball is created from the paddle's position with an angle of 0 degrees.
New Classes:
	1. BlockFormation - In charge of coordinate the aliens group's movement.
	2. Level - A class that implements 'levelInformation' interface from the last assignment –
	   In charge of creates all the blocks (the aliens) and other information of the level.

Other classes (from ass7):
Display package:
	1. AnimationRunner - run animations.
	2. CountDownAnimation - count down animation.
	3. HighScoresAnimation - high-scores animation.
	4. EndScreen - display 'game over' and the score on the screen at the end of a game.
	5. HighScoresTable - manage the high-scores table file.
	6. KeyPressStoppableAnimation - holds a key-pressed-stoppable animation, in charge of prevent
	   skipping on screen when the player pressed too long press.
	7. LevelNameIndicator - displays the level name on the screen.
	8. LivesIndicator - displays the lives on the screen.
	9. MenuAnimation - the menu animation.
	10. PauseScreen - the pause screen.
	11. ScoreIndicator - displays the current score on the screen.
	12. ScoreInfo - union information about a score.
	13. ShowHiScoresTask - shows the high scores animation.
	14. SpriteCollection - holds a collection of all the draw-able objects in the game.
Game package:
	1. GameEnvironment - holds a collection of all the collide-able objects in the game.
	2. GameLevel - initializes the game.
	3. RunGameTask - run the game until it ends.
Geometry package:
	1. Circle – represents a circle.
	2. Line – represents a line.
	3. Point - represents a point.
	4. Rectangle – represents a rectangle.
Instruments package:
	1. BackGround - holds a list of sprites that composed the background of the game.
	2. Ball - the bullets in the game. (a red one can't kill aliens)
	3. Block – in charge of representing the frame, the aliens and the shields.
	4. Paddle - the paddle.
	5. Selection - selection for the menu.
	6. Velocity - the velocity of a ball.
Tools package:
	1. BallRemover - in charge of removing balls after they hit something / the level stopped.
	2. BlockRemover - in charge of removing blocks after they have being hit.
	3. CollisionInfo - a collision information.
	4. Counter – in charge of tracking after score, lives etc.
	5. ScoreTrackingListener - in charge of updating the score during the game.
Ass7Game - The main class, in charge of build the menu and run it.

Interfaces (all from ass7):
	1. Animation - Class that can run until something happens.
	2. Menu - Class that execute tasks in accordance to the user's selections.
	3. Sprite - A draw-able Object - makes object visual on the screen.
	4. Task - Hold a class that have to do something.
	5. LevelInformation - Hold a description of a level in the game
	6. Collidable - A collide-able object - a ball will notice them and change his direction when a hit
	   event happens.
	7. HitListener - In charge of notice whenever a hit event happens.
	8. HitNotifier - In charge of updating the relevant objects when a hit event happens.
 
