# Space-Invaders
An implementation for the classic Space Invaders game.

The game was implemented based on my Arkanoid's infrastructure, since both have a lot in common. 
With the addition of some significant features and with the change of some small things, a fundamentally different game was created.

You can compile the program and generates a portable jar using the included makefile.

### Description of the modifications from 'Arkanoid' game.
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
	- I implemented a method in 'GameLevel' class called 'buildShields', inside this method I built three shields, each of them constructed by 90 tiny blocks (3 rows of 30 block each).
3. shots by aliens -
	- I implemented a method in 'GameLevel' class called 'shootMe' that gets a block (an alien), and creates a red ball from the alien's position with an angle of 180 degrees. This method is invoked from 'BlockFormation' class every 0.5 seconds by a random column of aliens.
	That happens inside 'timePassed' method that in every invocation:
		- Makes a list of the lowest aliens from all the columns.
		- Using 'startTime' member and the 'System.currentTimeMillis()' method, sends a random node from the lowest-aliens-list to 'shootMe' method every 0.5 seconds.
4. shots by player -
	- I implemented a method in 'GameLevel' class called 'shoot' that invoked whenever the player pressed the space-key. Inside this method a white ball is created from the paddle's position with an angle of 0 degrees.
