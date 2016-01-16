import java.awt.Point;

/**
 * Keeps track of a moving Cascade know as a Tableau.
 * 
 * @author Ridout and Derrick Thai
 * @version November 2014
 */
public class Tableau extends Cascade implements Movable
{
	private Cascade fromHand;

	/**
	 * Constructs a new Tableau object with the give x and y coordinates and the
	 * Cascade where the Tableau was picked up from.
	 * @param x the x coordinate of the Tableau
	 * @param y the y coordinate of the Tableau
	 * @param from the Cascade where the Tableau was picked up from
	 */
	public Tableau(int x, int y, Cascade from)
	{
		super(x, y);
		fromHand = from;
	}

	/**
	 * Moves this Tableau by the amount between the given initial and final
	 * positions. Also moves the Cards of the Tableau.
	 * @param initialPos the initial position to start dragging this Tableau
	 * @param finallPos the final position to keep dragging this Tableau
	 */
	public void move(Point initialPos, Point finalPos)
	{
		getPosition().x += finalPos.x - initialPos.x;
		getPosition().y += finalPos.y - initialPos.y;

		// Move the Cards of the Tableau
		for (Card card : hand)
			((GCard) card).move(initialPos, finalPos);
	}

	/**
	 * Checks if this Tableau intersects the given GHand.
	 * @param otherHand the GHand to check for intersection
	 * @return true if this Tableau intersects the given GHand
	 */
	public boolean intersects(GHand otherHand)
	{
		return getRectangle().intersects(otherHand.getRectangle());
	}

	/**
	 * Checks if this Tableau can be placed on the given GHand.
	 * @param otherHand the GHand this Tableau is trying to be placed on
	 * @return true if this Tableau can be placed on the given GHand or false if
	 *         it cannot
	 */
	public boolean canPlaceOn(GHand otherHand)
	{
		// Returning to where we came from is always valid
		if (otherHand.equals(fromHand))
			return true;

		// Check if we can move that many cards
		if (cardsLeft() > maxCardsMovable(otherHand))
			return false;

		// Cascade
		if (otherHand instanceof Cascade)
		{
			if (otherHand.cardsLeft() == 0)
				return true;
			return hand.get(0).canPlaceOnCascade(otherHand.getTopCard());
		}
		// Foundation and FreeCell
		return false;
	}

	/**
	 * Calculates the maximum number of Cards that can be moved.
	 * @param toHand the Hand this Tableau is trying to move to
	 * @return the maximum number of Cards that can be moved
	 */
	private int maxCardsMovable(GHand toHand)
	{
		// 1 + number of open FreeCells
		int cardsMovable = 1 + FreeCell.getnoOfOpenFreeCells();

		// Adjust the number of open Cascades if the one this Tableau is moving
		// from or to is empty since they cannot count towards the number of
		// open Cascades
		int openCascades = Cascade.getNoOfOpenCascades();
		if (fromHand.cardsLeft() == 0)
			openCascades--;
		if (toHand.cardsLeft() == 0)
			openCascades--;

		// 2 ^ number of open Cascades
		for (int i = 0; i < openCascades; i++)
			cardsMovable *= 2;

		return cardsMovable;
	}

	/**
	 * Places this Tableau on the given GHand.
	 * @param otherHand the GHand to place this Tableau on
	 */
	public void placeOn(GHand otherHand)
	{
		for (Card card : hand)
			otherHand.addCard((GCard) card);
	}
}
