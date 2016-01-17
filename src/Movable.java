import java.awt.Graphics;
import java.awt.Point;

/**
 * Behaviour for a Movable object (GCard or Tableau).
 * @author Derrick Thai and Gord Ridout
 * @version November 2014
 */
public interface Movable
{
	/**
	 * Moves this object by the amount between the initial and final position.
	 * @param initialPos the initial position to start dragging this object
	 * @param initialPos the final position to keep dragging this object
	 */
	public void move(Point initialPos, Point finalPos);

	/**
	 * Displays this object in its current position.
	 * @param g Graphics context to display this object
	 */
	public void draw(Graphics g);

	/**
	 * Checks to see if the given point is contained within this object.
	 * @param point the point to check
	 * @return true if the point is in this object, false if not
	 */
	public boolean contains(Point point);

	/**
	 * Checks to see if this object intersects the given GHand.
	 * @param otherHand the GHand to check for intersection
	 * @return true if this object intersects the given GHand, false if not
	 */
	public boolean intersects(GHand otherHand);

	/**
	 * Checks to see if you can place this object on the given GHand. Behaviour
	 * will depend on what kind of GHand we are placing this object on.
	 * @param otherHand the GHand we want to place this object on
	 * @return true if this object can be placed on the given GHand, false if
	 *         not
	 */
	public boolean canPlaceOn(GHand otherHand);

	/**
	 * Places this object on the given GHand adding all of the Cards in this
	 * object to the GHand.
	 * @param otherHand the GHand to place this object on
	 */
	public void placeOn(GHand otherHand);
}
