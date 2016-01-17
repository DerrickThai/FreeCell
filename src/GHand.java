import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * An abstract class for a Graphical Hand (GHand). Inherits data and methods
 * from Hand. A Graphical Hand is a Hand of Graphical Cards (GCard). Includes
 * variables to keep track of the Hand's position, current width and height
 * based on the Cards in the Hand and the horizontal spacing of the Hand's
 * Cards. Includes methods to construct a new GHand, get the current position of
 * the Hand, add and remove GCards from this Hand, check if a point is contained
 * within this Hand, get the underlining Rectangle of this Hand and to draw this
 * Hand. Also includes two abstract methods to see if you can pick up a Movable
 * object from this Hand given a selection point and to pick up a Movable object
 * from this Hand, given a selection point.
 * 
 * @author Derrick Thai and Gord Ridout
 * @version November 2014
 */

public abstract class GHand extends Hand
{
	// Transparent colour of the bases of the hands
	Color BASE_COLOUR = new Color(210, 210, 210, 100);

	// Variables to keep track of each GHand's position, dimensions and vertical
	// spacing between the Cards in the hand
	private Point position;
	private int width, height;
	private int spacing;

	/**
	 * Constructs a new GHand with the given x and y position and horizontal
	 * spacing.
	 * @param x the x position of upper left corner of the Hand
	 * @param y the y position of upper left corner of the Hand
	 * @param spacing vertical spacing between Cards
	 */
	public GHand(int x, int y, int spacing)
	{
		super();
		position = new Point(x, y);
		width = GCard.WIDTH;
		height = GCard.HEIGHT;
		this.spacing = spacing;
	}

	/**
	 * Returns the position of the top left corner of this Hand.
	 * @return the position of the top left corner of this Hand
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * Adds a GCard to this Hand updating the position of the new Card and
	 * adjusting the spacing of the Cards accordingly.
	 * @param card the Card to add to this GHand
	 */
	public void addCard(GCard card)
	{
		// if (hand.size() > 0)
		card.setPosition(new Point(position.x, position.y + hand.size()
				* spacing));
		// else
		// card.setPosition(new Point(position));
		hand.add(card);
		updateHeight();
	}

	/**
	 * Removes a GCard from this Hand at the given index.
	 * @param index the index of the GCard to remove
	 * @return the GCard removed from the Hand
	 */
	private GCard removeCard(int index)
	{
		GCard cardToRemove = (GCard) hand.remove(index);
		updateHeight();
		return cardToRemove;
	}

	/**
	 * Looks at the top Card in this GHand.
	 * @return the top GCard is this Hand
	 */
	public GCard getTopCard()
	{
		// Removed to avoid the use of null checks
		// if (hand.size() == 0)
		// return null;
		return (GCard) hand.get(hand.size() - 1);
	}

	/**
	 * Removes the top Card from this GHand 
	 * Precondition: this GHand has at least one Card in it
	 * @return the top GCard removed from the Hand
	 */
	public GCard removeTopCard()
	{
		return removeCard(hand.size() - 1);
	}

	/**
	 * Adjusts the height of this Hand after adding or removing a Card.
	 */
	private void updateHeight()
	{
		if (hand.size() > 1)
			height = GCard.HEIGHT + (hand.size() - 1) * spacing;
		else
			height = GCard.HEIGHT;
	}

	/**
	 * Checks to see if the given point is contained within this Hand.
	 * @param point the point to check
	 * @return true if the point is in this Hand, false if not
	 */
	public boolean contains(Point point)
	{
		return (new Rectangle(position.x, position.y, width, height))
				.contains(point);
	}

	/**
	 * Returns the outlining Rectangle of this GHand.
	 * @return the outlining Rectangle of this GHand
	 */
	public Rectangle getRectangle()
	{
		return new Rectangle(position.x, position.y, width, height);
	}

	/**
	 * Displays the Cards in this Hand in this Hand's position.
	 * @param g Graphics context to display this GHand
	 */
	public void draw(Graphics g)
	{
		// Draw the bases of the GHands and their cards
		if (!(this instanceof Tableau))
		{
			g.setColor(BASE_COLOUR);
			g.fillRect(position.x, position.y, GCard.WIDTH, GCard.HEIGHT);
			g.setColor(Color.BLACK);
			g.drawRect(position.x, position.y, GCard.WIDTH, GCard.HEIGHT);
		}
		for (Card next : hand)
			((GCard) next).draw(g);
	}

	/**
	 * Checks if you can pick up a single Card or a tableau of Cards from this
	 * Hand based on the given point that is being selected.
	 * @param point the point of the Hand that is selected
	 * @return true if you can pick up a single Card or a tableau of Cards based
	 *         on the given point, false if not
	 */
	public abstract boolean canPickUp(Point point);

	/**
	 * Based on the point of selection returns the Movable Card or Tableau that
	 * you can pick up.
	 * @param point the point of the Hand that is selected
	 * @return the Movable Card or Tableau based on the point of selection
	 */
	public abstract Movable pickUp(Point point);

}
