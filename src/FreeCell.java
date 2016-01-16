import java.awt.Point;

/**
 * Keeps track of a FreeCell. FreeCells are only one Card, but code is
 * simplified if we assume a FreeCell is a GHand.
 * 
 * @author Ridout and Derrick Thai
 * @version November 2014
 */
public class FreeCell extends GHand
{
	private static int noOfOpenFreeCells = 4;

	/**
	 * Constructs a new FreeCell object with the given x and y coordinates.
	 * @param x the x coordinate of the FreeCell
	 * @param y the y coordinate of the FreeCell
	 */
	public FreeCell(int x, int y)
	{
		// There is only one Card in a FreeCell therefore spacing is zero
		super(x, y, 0);
	}

	/**
	 * Determines if a Card can be picked up from this FreeCell.
	 * @param point the Point where the mouse clicked
	 * Precondition: the given Point is contained in this FreeCell
	 * @return true if the hand is not empty or false if not
	 */
	public boolean canPickUp(Point point)
	{
		return !hand.isEmpty();
	}

	/**
	 * Picks up the only Card from this FreeCell.
	 * @param point the Point where the mouse clicked
	 * Precondition: the Point is contained in this FreeCell and a valid 
	 * GCard can be picked up from the Point
	 * @return the top Card of this FreeCell
	 */
	public Movable pickUp(Point point)
	{
		return removeTopCard();
	}

	/**
	 * Adds a GCard to this FreeCell, updating the number of open FreeCells if
	 * necessary.
	 * @overrides the addCard(GCard card) method in GHand
	 */
	public void addCard(GCard card)
	{
		super.addCard(card);
		noOfOpenFreeCells--;
	}

	/**
	 * Removes the GCard from this FreeCell, updating the number of open
	 * FreeCells if necessary.
	 * @overrides the removeTopCard() method in GHand
	 */
	public GCard removeTopCard()
	{
		noOfOpenFreeCells++;
		return super.removeTopCard();
	}

	/**
	 * Resets the number of FreeCells to four.
	 */
	public static void resetnoOfOpenFreeCells()
	{
		noOfOpenFreeCells = 4;
	}

	/**
	 * Gets the number of FreeCells.
	 * @return the number of FreeCells
	 */
	public static int getnoOfOpenFreeCells()
	{
		return noOfOpenFreeCells;
	}

}
