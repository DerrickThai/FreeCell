import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Looks after most of the FreeCell Game.
 * 
 * @author Derrick Thai and Gord Ridout
 * @version November 2014
 */
public class CardPanel extends JPanel implements MouseListener,
		MouseMotionListener, ActionListener
{
	// To remove yellow warning
	private static final long serialVersionUID = 1L;

	// Constants for the table layout and the time font
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static final Color TABLE_COLOUR = new Color(0, 140, 0);
	private static final Font TIME_FONT = new Font("Calibri", Font.BOLD, 18);

	// Constants for layout of Card area
	private final int NO_OF_CASCADES = 8;
	private final int NO_OF_FREECELLS = 4;
	private final int NO_OF_FOUNDATIONS = 4;
	private final int CASCADE_X = 30;
	private final int CASCADE_Y = 150;
	private final int CASCADE_SPACING = 95;
	private final int FREECELL_X = 30;
	private final int FREECELL_Y = 30;
	private final int TOP_SPACING = 90;
	private final int FOUNDATION_X = 425;
	private final int FOUNDATION_Y = 30;

	// Variables for the FreeCell Game
	private FreeCellMain parentFrame;
	private GDeck myDeck;
	private ArrayList<GHand> allHands;
	private LinkedList<Move> moves;
	private Movable selectedItem, movingCard;
	private GHand sourceHand;
	private Point lastPoint;
	private boolean inGame;
	private Statistics stats;
	private Timer timer;
	private int gameSeconds;

	/**
	 * Constructs a CardPanel by setting up the Panel, the Deck and all of
	 * required Hands to keep track of the FreeCells, Foundations, and Cascades.
	 * Also sets up listeners for mouse events, a move list, loads the
	 * Statistics from the file, and initialize some instance variables.
	 * 
	 * @param parentFrame the main Frame that holds this Panel
	 */
	public CardPanel(FreeCellMain parentFrame)
	{
		// Set up the size and background colour
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(TABLE_COLOUR);
		this.parentFrame = parentFrame;

		// Add mouse listeners to the Card panel
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		// Set up the Deck, Cascades, Foundations and FreeCells
		myDeck = new GDeck(400 - GCard.WIDTH / 2, 470);
		allHands = new ArrayList<GHand>();

		// Create Cascades
		int xCascade = CASCADE_X;
		int yCascade = CASCADE_Y;
		for (int i = 0; i < NO_OF_CASCADES; i++)
		{
			allHands.add(new Cascade(xCascade, yCascade));
			xCascade += CASCADE_SPACING;
		}

		// Create FreeCells
		int xFreeCell = this.FREECELL_X;
		int yFreeCell = this.FREECELL_Y;
		for (int i = 0; i < this.NO_OF_FREECELLS; i++)
		{
			allHands.add(new FreeCell(xFreeCell, yFreeCell));
			xFreeCell += TOP_SPACING;
		}

		// Create Foundations
		int xFoundation = FOUNDATION_X;
		int yFoundation = FOUNDATION_Y;
		for (int i = 0; i < this.NO_OF_FOUNDATIONS; i++)
		{
			allHands.add(new Foundation(xFoundation, yFoundation));
			xFoundation += TOP_SPACING;
		}

		// Initialize instance variables
		moves = new LinkedList<Move>();
		movingCard = null;
		stats = Statistics.readFromFile(Statistics.STATS_FILE);
	}

	/**
	 * Starts up a new game by clearing all of the Hands, shuffling the Deck and
	 * dealing new Cards to the Cascades. Also resets the move list, resets the
	 * number of open Cascades and FreeCells, and resets the time.
	 */
	public void newGame()
	{
		// Clear out all of the Hands
		for (Hand next : allHands)
			next.clear();

		myDeck.shuffle();

		// Deal the Cards to the Cascades (first 8 Hands)
		int cascasdeIndex = 0;
		while (myDeck.cardsLeft() > 0)
		{
			GCard dealtCard = myDeck.dealCard();
			Point pos = new Point(dealtCard.getPosition());
			allHands.get(cascasdeIndex).addCard(dealtCard);
			Point finalPos = new Point(dealtCard.getPosition());
			if (parentFrame.isAnimateOn())
				moveACard(dealtCard, pos, finalPos);
			if (!dealtCard.isFaceUp())
				dealtCard.flip();
			cascasdeIndex++;
			if (cascasdeIndex == NO_OF_CASCADES)
				cascasdeIndex = 0;
		}
		paintImmediately(0, 0, WIDTH, HEIGHT);

		// Reset some variables for the new game
		moves.clear();
		parentFrame.setUndoOption(false);
		FreeCell.resetnoOfOpenFreeCells();
		Cascade.resetNoOfOpenCascades();
		inGame = true;
		timer = new Timer(1000, this);
		gameSeconds = 0;
		paintImmediately(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * Checks if there are any moves in the moves list so that we can see if it
	 * is okay to undo a move.
	 * @return true if we can undo, false if not
	 */
	public boolean canUndo()
	{
		return !moves.isEmpty();
	}

	/**
	 * Undoes the last move.
	 */
	public void undo()
	{
		if (canUndo())
		{
			Move lastMove = moves.removeLast();
			lastMove.undo();
			repaint();
		}
	}

	/**
	 * Finds and returns a List of all of the valid Moves between Cascades only.
	 * @return an ArrayList of all of the valid moves between Cascades
	 */
	public ArrayList<Move> allCascadeMoves()
	{
		// Create an ArrayList to store all of the possible Moves
		ArrayList<Move> allMoves = new ArrayList<Move>();

		// For each Cascade, get all of the Movables that can be taken from it
		// and if any can be placed on another Cascade, add that Move to the
		// List of possible Moves
		for (GHand Cascade : allHands.subList(0, NO_OF_CASCADES))
			for (Movable movable : ((Cascade) Cascade).getAllMovables())
				for (GHand next : allHands.subList(0, NO_OF_CASCADES))
					if (next != Cascade)
						if (movable.canPlaceOn(next))
							allMoves.add(new Move(Cascade, next, movable));
		return allMoves;
	}

	/**
	 * Checks to see if the player has won by completing all of the Foundations
	 * @return true if they have won, false if not
	 */
	private boolean checkForWinner()
	{
		for (GHand nextFoundation : allHands.subList(NO_OF_CASCADES
				+ NO_OF_FREECELLS, allHands.size()))
			if (nextFoundation.cardsLeft() < 13)
				return false;
		return true;
	}

	/**
	 * Auto completes any Cards up to the Foundations when possible.
	 */
	private void autoComplete()
	{
		// If we auto complete once, we must check everything again
		boolean autoCompletedOnce = true;
		while (autoCompletedOnce)
		{
			autoCompletedOnce = false;

			// Go through each Cascade and FreeCell and determine if it can be
			// put up to a Foundation
			for (GHand hand : allHands.subList(0, NO_OF_CASCADES
					+ NO_OF_FREECELLS))
				for (GHand Foundation : allHands.subList(NO_OF_CASCADES
						+ NO_OF_FREECELLS, allHands.size()))
					if (hand.cardsLeft() > 0)
					{
						// To avoid null checking, check the number of Cards in
						// the foundation, if it is not empty then call
						// canPlaceOnFoundation, if not check if the Card in
						// consideration to be auto completed is an Ace
						if ((Foundation.cardsLeft() > 0 && hand
								.getTopCard().canPlaceOnFoundation(
										Foundation.getTopCard()))
								|| (Foundation.cardsLeft() == 0 && hand
										.getTopCard().isAce()))
							// Determine if it would be beneficial to auto
							// complete the card in consideration and if so,
							// auto complete the Card
							if (shouldAutoComplete(hand))
							{
								complete(hand, Foundation);
								autoCompletedOnce = true;
							}
					}
		}
	}

	/**
	 * Helper method of autoComplete() that determines if the top Card of the
	 * given GHand should be auto completed.
	 * @param hand the GHand of the Card considering
	 * @return true if the Card should be auto completed or false if not
	 */
	private boolean shouldAutoComplete(GHand hand)
	{
		// Aces and Twos should always auto complete
		if (hand.getTopCard().getRank() <= 2)
			return true;

		// If there is a Card that can be placed on the Card in consideration
		// (by the canPlaceOnCascade rules) that is not in a Foundation yet, the
		// Card in consideration should not be auto completed
		GCard CardConsidering = hand.getTopCard();
		for (GHand nextHand : allHands.subList(0, NO_OF_CASCADES
				+ NO_OF_FREECELLS))
			for (Card nextCard : nextHand.hand)
				if (((GCard) nextCard).canPlaceOnCascade(CardConsidering))
					return false;
		return true;
	}

	/**
	 * Auto completes a Card from the given GHand to the other given GHand.
	 * @param from the GHand that has the Card to be auto completed
	 * @param to the GHand (Foundation) that the Card will move to
	 */
	private void complete(GHand from, GHand to)
	{
		// Remove the Card from its current GHand and add it to correct
		// Foundation, animating the Card if animate is on
		GCard CardToMove = from.removeTopCard();
		if (parentFrame.isAnimateOn())
			moveACard(CardToMove, CardToMove.getPosition(), to.getPosition());
		to.addCard(CardToMove);

		// Add the Move to the Moves List so that an auto complete can be undoed
		moves.addLast(new Move(from, to, CardToMove));
		parentFrame.setUndoOption(true);
	}

	/**
	 * Moves a Card during the animation.
	 * @param CardToMove Card that you want to move
	 * @param fromPos initial position of the Card
	 * @param toPos final position of the Card
	 */
	public void moveACard(final GCard CardToMove, Point fromPos, Point toPos)
	{
		// Get the animation frames from FreeCell Main can calculate the change
		// in x and y per frame
		int animationFrames = parentFrame.getAnimationFrames();
		int dx = (toPos.x - fromPos.x) / animationFrames;
		int dy = (toPos.y - fromPos.y) / animationFrames;

		// Make the movingCard the Card to be moved so that it will be painted
		// on top of the hands
		movingCard = CardToMove;

		// Animate the Card from to the destination Point
		for (int times = 1; times <= animationFrames; times++)
		{
			fromPos.x += dx;
			fromPos.y += dy;
			CardToMove.setPosition(fromPos);

			// Update the drawing area
			paintImmediately(0, 0, getWidth(), getHeight());
			delay(30);

		}
		// Snap the Card to the exact position and then nullify movingCard
		CardToMove.setPosition(toPos);
		movingCard = null;
	}

	/**
	 * Delays the given number of milliseconds.
	 * @param milliSec number of milliseconds to delay
	 */
	private void delay(int milliSec)
	{
		try
		{
			Thread.sleep(milliSec);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Draws the information in this CardPanel. Draws the Deck, all of the
	 * hands, the moving Card in an animation and the selected Card or GHand.
	 * @param g the Graphics context to do the drawing
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Draw the Deck if there are Cards left
		if (myDeck.cardsLeft() > 0)
			myDeck.draw(g);

		// Draw all of the Hands
		for (GHand next : allHands)
			next.draw(g);

		// For animation to draw the moving Card
		if (movingCard != null)
			movingCard.draw(g);

		// Draw selected GHand or Card on top
		if (selectedItem != null)
			selectedItem.draw(g);

		// Draw the time if a game is in progress
		g.setFont(TIME_FONT);
		if (inGame)
			g.drawString(
					String.format("%d:%02d", gameSeconds / 60, gameSeconds % 60),
					385, 20);
	}

	/**
	 * Updates the Statistics after a win with the given time.
	 * @param time the time in seconds in which the game lasted
	 */
	private void updateStatsAfterWin(int time)
	{
		// Update the appropriate Statistics variables
		stats.incrementGamesWon();
		stats.incrementGamesPlayed();
		stats.incrementCurrentStreak();
		stats.updateLongestStreak();
		stats.updateFastestTime(time);

		// Save to the file
		stats.writeToFile(Statistics.STATS_FILE);
	}

	/**
	 * Updates the Statistics after a loss.
	 */
	public void updateStatsAfterLose()
	{
		// Update the appropriate Statistics variables
		stats.incrementGamesPlayed();
		stats.resetCurrentStreak();

		// Save to the file
		stats.writeToFile(Statistics.STATS_FILE);
	}

	/**
	 * Checks if there is a game currently in progress.
	 * @return true if there is a game currently in progress or false if not
	 */
	public boolean isInGame()
	{
		return inGame;
	}

	/**
	 * Displays the Statistics and has the option to reset the Statistics.
	 */
	public void showStats()
	{
		stats.writeToFile(Statistics.STATS_FILE);

		String[] choices = { "OK", "Reset Statistics" };
		if (JOptionPane.showOptionDialog(this, stats.toString(),
				"Statistics", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, choices, null) == 1)
			if (JOptionPane.showConfirmDialog(this,
					"Are you sure you want to reset the Statistics?",
					"Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				stats = new Statistics();
				stats.writeToFile(Statistics.STATS_FILE);
			}
	}

	/**
	 * Handles the timer which fires every one second.
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == timer)
		{
			// Increment the time by one second
			gameSeconds++;
			repaint();
		}

	}

	/**
	 * Handles the mouse pressed events to pick a Card or a Tableau
	 * @param event event information for mouse pressed
	 */
	public void mousePressed(MouseEvent event)
	{
		if (selectedItem == null)
		{
			Point selectedPoint = event.getPoint();

			// Pick up one of Cards from a Hand (FreeCell or Cascade)
			// Could also pick up from a Foundation if you want
			for (GHand nextHand : allHands)
				if (nextHand.contains(selectedPoint)
						&& nextHand.canPickUp(selectedPoint))
				{
					// Split off a section of the Cascade or pick up a Card
					selectedItem = nextHand.pickUp(selectedPoint);

					// In case our move is not valid, we want to return the
					// Card(s) to where they initially came from
					sourceHand = nextHand;
					lastPoint = selectedPoint;
					repaint();
					return;
				}
		}
	}

	/**
	 * Handles the mouse released events to drop a Card or a Tableau
	 * @param event event information for mouse released
	 */
	public void mouseReleased(MouseEvent event)
	{
		if (selectedItem != null)
		{
			// Check to see if we can add this to another Cascade
			// Foundation or FreeCell
			for (GHand nextHand : allHands)
				if (selectedItem.intersects(nextHand)
						&& selectedItem.canPlaceOn(nextHand))
				{
					selectedItem.placeOn(nextHand);

					// Count this move if you didn't place it on the same spot
					if (nextHand != sourceHand)
					{
						moves.addLast(new Move(sourceHand, nextHand,
								selectedItem));
						parentFrame.setUndoOption(true);
						// Start the time if that was the first move
						if (!timer.isRunning())
							timer.start();
					}
					selectedItem = null;
					repaint();

					// Check these things after a Card is dropped
					if (parentFrame.isAutoCompleteOn())
						autoComplete();
					// Check if game has been won
					if (checkForWinner())
					{
						// End the game, prevent undos, stop the time, and
						// update the statistics
						inGame = false;
						parentFrame.setUndoOption(false);
						timer.stop();
						updateStatsAfterWin(gameSeconds);

						// Congratulatory message. Let minutes surpass 60 since
						// it is uncommon
						// for one to play an hour long game
						String time = String.format("%d:%02d.",
								gameSeconds / 60,
								gameSeconds % 60);
						JOptionPane.showMessageDialog(parentFrame,
								"You Win! Your time was "
										+ time, "Congratulations",
								JOptionPane.INFORMATION_MESSAGE);
						repaint();
					}
					return;
				}

			// Return to original spot if not a valid move
			selectedItem.placeOn(sourceHand);
			selectedItem = null;
			repaint();
		}
	}

	/**
	 * Handles the mouse dragged events to drag the moving Card(s)
	 * @param event event information for mouse dragged
	 */
	public void mouseDragged(MouseEvent event)
	{
		Point currentPoint = event.getPoint();

		if (selectedItem != null)
		{
			// We use the difference between the lastPoint and the
			// currentPoint to move the Cascade or Card so that the position of
			// the mouse on the Cascade/Card doesn't matter.
			// i.e. we can drag the Card from any point on the Card image
			selectedItem.move(lastPoint, currentPoint);
			lastPoint = currentPoint;
			repaint();
		}

	}

	/**
	 * Handles the mouse moved events to show which Cards can be picked up
	 * @param event event information for mouse moved
	 */
	public void mouseMoved(MouseEvent event)
	{
		// Set the cursor to a hand if we are on a Card or Tableau that we can
		// pick up
		Point currentPoint = event.getPoint();
		for (GHand nextHand : allHands)
			if (nextHand.contains(currentPoint)
					&& nextHand.canPickUp(currentPoint))
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				return;
			}

		// Otherwise use the default cursor
		setCursor(Cursor.getDefaultCursor());

	}

	// Extra methods needed since we implemented MouseListener that are not
	// implemented in this class
	public void mouseClicked(MouseEvent event)
	{
	}

	public void mouseEntered(MouseEvent event)
	{
	}

	public void mouseExited(MouseEvent event)
	{
	}
}
