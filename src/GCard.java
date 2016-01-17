import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * Keeps track of a Graphical Card (GCard). Inherits data and methods from Card.
 * Keeps track of a position and an Image for each GCard. Also keeps track of
 * some static variables for the background image and the width and height of
 * each Card. Includes methods to construct a new Card, look at and change a
 * Card's position and draw this Card.
 * 
 * @author Derrick Thai and Gord Ridout
 * @version November 2014
 * 
 */
public class GCard extends Card implements Movable
{
	// Constants for the back image
	public final static Image BACK_IMAGE = new ImageIcon("images\\blueback.png")
			.getImage();
	public final static int WIDTH = BACK_IMAGE.getWidth(null);
	public final static int HEIGHT = BACK_IMAGE.getHeight(null);

	// Variables to keep track of each Card's position and image
	private Point position;
	private Image image;

	/**
	 * Constructs a new graphical Card.
	 * @param rank the rank of the Card
	 * @param suit the suit of the Card
	 * @param position the initial position of the Card
	 */
	public GCard(int rank, int suit, Point position)
	{
		super(rank, suit);
		this.position = position;

		// Load up the appropriate image file for this card
		String imageFileName = "" + " cdhs".charAt(suit) + rank + ".png";
		imageFileName = "images\\" + imageFileName;
		image = new ImageIcon(imageFileName).getImage();

	}

	/**
	 * Sets the current position of this GCard.
	 * @param position the Card's current position
	 */
	public void setPosition(Point position)
	{
		this.position = position;
	}

	/**
	 * Gets the current position of this GCard.
	 * @return the Card's current position
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * Draws a card in a Graphics context.
	 * @param g Graphics to draw the card in
	 */
	public void draw(Graphics g)
	{
		if (isFaceUp())
			g.drawImage(image, position.x, position.y, null);
		else
			g.drawImage(BACK_IMAGE, position.x, position.y, null);
	}

	/**
	 * Moves a Card by the amount between the initial and final position.
	 * @param initialPos the initial position to start dragging this Card
	 * @param finalPos the final position to keep dragging this Card
	 */
	public void move(Point initialPos, Point finalPos)
	{
		position.x += finalPos.x - initialPos.x;
		position.y += finalPos.y - initialPos.y;
	}

	/**
	 * Determines if this GCard contains the given Point.
	 * @return true if this GCard contains the given Point or false if not
	 */
	public boolean contains(Point point)
	{
		return new Rectangle(position.x, position.y, WIDTH, HEIGHT)
				.contains(point);
	}

	/**
	 * Determines if this GCard intersects the given GHand.
	 * @return true if this GCard intersects the given GHand or false if not
	 */
	public boolean intersects(GHand otherHand)
	{
		return otherHand.getRectangle().intersects(
				new Rectangle(position.x, position.y, WIDTH, HEIGHT));
	}

	/**
	 * Determines if this GCard can be placed on the given GHand.
	 * @param otherHand the GHand this GCard is trying to be placed on
	 * @return true if this GCard can be placed on the given GHand or false if
	 *         not
	 */
	public boolean canPlaceOn(GHand otherHand)
	{
		// Cascade
		if (otherHand instanceof Cascade)
		{
			// Added this check to avoid checking null cards
			if (otherHand.cardsLeft() == 0)
				return true;
			return canPlaceOnCascade(otherHand.getTopCard());
		}
		// Foundation
		if (otherHand instanceof Foundation)
		{
			// Added this check to avoid checking null cards
			if (otherHand.cardsLeft() == 0)
				return this.isAce();
			return canPlaceOnFoundation(otherHand.getTopCard());
		}
		// FreeCell
		return otherHand.cardsLeft() == 0;
	}

	/**
	 * Places this GCard on the given GHand.
	 * @param otherHand the GHand to place this GCard on
	 */
	public void placeOn(GHand otherHand)
	{
		otherHand.addCard(this);
	}
}
