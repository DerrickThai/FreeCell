import java.awt.Point;
import java.util.ArrayList;

/**
 * Keeps track of a Cascade and the number of open Cascades.
 * 
 * @author Derrick Thai and Gord Ridout
 * @version November 2014
 */
public class Cascade extends GHand
{
	// Keep track of the number of open Cascades 
	private static int noOfOpenCascades = 0;

	/**
	 * Constructs a new Cascade object with the given x and y coordinates.
	 * @param x the x coordinate of the Cascade
	 * @param y the y coordinate of the Cascade
	 */
	public Cascade(int x, int y)
	{
		// Vertical spacing is 20 pixels
		super(x, y, 20);
	}

	/**
	 * Determines if a Movable can be picked up from the given Point.
	 * @param point the Point clicked by the mouse 
	 * Precondition: the given Point is contained in this Cascade
	 * @return true if a Movable can be picked up from the given Point or false
	 *         if not
	 */
	public boolean canPickUp(Point point)
	{
		// You cannot pick up from an empty hand
		if (hand.isEmpty())
			return false;
		// You can always pick up a single Card
		if (getTopCard().contains(point))
			return true;

		// For each Card of the Cascade starting from the card underneath the
		// top Card, if the Point is contained in the Card then we are clicking
		// on that Card
		for (int card = hand.size() - 2; card >= 0; card--)
		{
			GCard gCard = (GCard) hand.get(card);

			if (gCard.contains(point))
			{
				// The Tableau is only valid if each Card can be placed on the
				// card below it
				for (int next = hand.size() - 1; next > card; next--)
					if (!hand.get(next).canPlaceOnCascade(hand.get(next - 1)))
						return false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Picks up a Movable from the given Point, removing the Cards picked up
	 * from this Cascade.
	 * @param the Point clicked by the mouse 
	 * Precondition: the Point is contained in this Cascade and a valid 
	 * Tableau/GCard can be picked up from the Point
	 * @return the Movable picked up from the given Point
	 */
	public Movable pickUp(Point point)
	{
		// Pick up the top Card if the point is on the top Card
		if (((GCard) hand.get(hand.size() - 1)).contains(point))
			return removeTopCard();

		// For each Card of the Cascade starting from the card underneath the
		// top Card, if the Point is contained in the Card then we are clicking
		// on that Card
		for (int card = hand.size() - 2; card >= 0; card--)
		{
			GCard gCard = (GCard) hand.get(card);

			if (gCard.contains(point))
			{
				// Create a new Tableau with the same coordinates as the Card 
				// being clicked and add all of the Cards from the Card being
				// clicked down to the top Card to the Tableau, removing them
				// from this Cascade after 
				Tableau tableau = new Tableau(gCard.getPosition().x,
						gCard.getPosition().y, this);

				for (Card next : hand.subList(card, hand.size()))
					tableau.addCard((GCard) next);
				for (int next = card; next < hand.size();)
					removeTopCard();

				return tableau;
			}
		}
		return null;
	}

	/***
	 * Finds all of the possible Movable objects that can be taken from this
	 * Cascade.
	 * @return an ArrayList of all possible Movable objects that can be taken
	 *         from this Cascade Postcondition: this Cascade is left as is
	 */
	public ArrayList<Movable> getAllMovables()
	{
		// Create an ArrayList to store all of the Movables
		ArrayList<Movable> movables = new ArrayList<Movable>(cardsLeft());

		// There are no Movables if this Cascade is empty
		if (cardsLeft() == 0)
			return movables;

		// The top Card is always a valid Movable
		movables.add(getTopCard());

		// For each Card starting from the Card underneath the top Card, if 
		// the Card on top of that Card can be placed on that Card (by
		// canPlaceOnCascade rules), create a Tableau containing all of the
		// Cards from and including this Card to the top Card.
		for (int card = cardsLeft() - 2; card >= 0; card--)
		{
			if (hand.get(card + 1).canPlaceOnCascade(hand.get(card)))
			{
				Point point = ((GCard) hand.get(card)).getPosition();
				Tableau tableau = new Tableau(point.x, point.y, this);
				for (Card nextCard : hand.subList(card, cardsLeft()))
					tableau.addCard((GCard) nextCard);

				// Add the Tableau to the Movables List
				movables.add(tableau);
			}
			else
				// Once a case is found where the Card on top of the current
				// Card cannot be placed on the current Card, there are no more
				// possible Movables
				return movables;
		}
		return movables;
	}

	/**
	 * Adds a GCard to this Cascade, updating the number of open Cascades if
	 * necessary.
	 * @overrides the addCard(GCard card) method in GHand
	 */
	public void addCard(GCard card)
	{
		if (cardsLeft() == 0 && !(this instanceof Tableau))
			noOfOpenCascades--;
		super.addCard(card);
	}

	/**
	 * Removes the top GCard from this Cascade, updating the number of open 
	 * Cascades if necessary.
	 * @overrides the removeTopCard() method in GHand
	 */
	public GCard removeTopCard()
	{
		if (cardsLeft() == 1)
			noOfOpenCascades++;
		return super.removeTopCard();

	}

	/**
	 * Resets the number of free Cascades to zero.
	 */
	public static void resetNoOfOpenCascades()
	{
		noOfOpenCascades = 0;
	}

	/**
	 * Gets the number of free Cascades.
	 * @return the number of free Cascades
	 */
	public static int getNoOfOpenCascades()
	{
		return noOfOpenCascades;
	}
}
