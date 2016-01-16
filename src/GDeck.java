import java.awt.Graphics;
import java.awt.Point;

/**
 * Keeps track of a Graphical Deck (GDeck). Inherits data and methods from Deck.
 * Keeps track of this Deck's position. Includes methods to construct this Deck,
 * shuffle the Deck, deal GCards from this Deck and to draw this Deck.
 * @author Ridout
 * @version November 2014
 */
public class GDeck extends Deck
{
	private Point position;

	/**
	 * Constructs a new Graphical Deck (GDeck).
	 * @param x the x coordinate of the upper left corner of the GDeck
	 * @param y the y coordinate of the upper left corner of the GDeck
	 */
	public GDeck(int x, int y)
	{
		this.position = new Point(x, y);
		topCard = 0;
		deck = new GCard[52];

		for (int suit = 1; suit <= 4; suit++)
			for (int rank = 1; rank <= 13; rank++)
				deck[topCard++] = new GCard(rank, suit, position);

	}

	/**
	 * Shuffles the GDeck by shuffling all of the Cards. Also sets the position
	 * of all of the Cards in the GDeck back to the GDecks's positions.
	 */
	public void shuffle()
	{
		super.shuffle();

		// Put the cards back to the position of the Deck
		for (int index = 0; index < topCard; index++)
		{
			GCard nextCard = (GCard) deck[index];
			nextCard.setPosition(position);
		}
	}

	/**
	 * Deals a GCard from this GDeck.
	 * @return the dealt GCard
	 */
	public GCard dealCard()
	{
		return (GCard) super.dealCard();
	}

	/**
	 * Displays the un-dealt Cards in this Hand.
	 * @param g Graphics context to display the deck
	 */
	public void draw(Graphics g)
	{
		for (int index = 0; index < topCard; index++)
		{
			GCard nextCard = (GCard) deck[index];
			nextCard.draw(g);
		}
	}
}
