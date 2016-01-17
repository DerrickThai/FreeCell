import java.awt.Point;

/**
 * Keeps track of a Foundation.
 * 
 * @author Derrick Thai and Gord Ridout
 * @version November 2014
 */
public class Foundation extends GHand
{
	/**
	 * Constructs a new Foundation with the given x and y coordinates.
	 * @param x the x coordinate of the Foundation
	 * @param y the y coordinate of the Foundation
	 */
	public Foundation(int x, int y)
	{
		super(x, y, 0);
	}

	/**
	 * Determines if you can pick up a Card from the given Point.
	 * @param point the Point where the mouse was clicked
	 * @return false because you cannot pick up from a Foundation
	 */
	public boolean canPickUp(Point point)
	{
		return false;
	}

	/**
	 * Picks up a Movable from the given Point.
	 * @param point the Point where the mouse was clicked
	 * @return null because you cannot pick up from a Foundation
	 */
	public Movable pickUp(Point point)
	{
		return null;
	}
}
