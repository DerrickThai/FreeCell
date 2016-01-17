/**
 * Keeps track of a Move and can undo a Move.
 * @author Derrick Thai
 * @version November 2014
 */
public class Move
{
	// Variables to keep track of what was moved from where to where for each
	// Move instance
	private GHand from;
	private GHand to;
	private Movable moved;

	/**
	 * Constructs a new Move object with the given from and to GHands and the
	 * give Movable that was moved.
	 * @param from the GHand where the Movable moved from
	 * @param to the GHand where the Movable moved to
	 * @param moved the Movable that was moved for this Move
	 */
	public Move(GHand from, GHand to, Movable moved)
	{
		this.from = from;
		this.to = to;
		this.moved = moved;
	}

	/**
	 * Undos this move.
	 */
	public void undo()
	{
		// Remove the moved Cards from the to GHand
		if (moved instanceof Tableau)
		{
			int cards = ((Tableau) moved).cardsLeft();
			for (int card = 1; card <= cards; card++)
				to.removeTopCard();
		}
		else
			// GCard
			to.removeTopCard();

		// Place the Movable back to its original GHand
		moved.placeOn(from);
	}
}
