/**
 * Keeps track of a Deck's information including a list of the Card objects in
 * the deck and the current top card of the deck. Can deal Cards from the deck,
 * shuffle the deck, get how many cards are left in the deck, and can return a
 * String representation of the Deck.
 * @author Derrick Thai
 * @version November 4, 2014
 */
public class Deck
{
	// Instance variables to keep track of the Cards in this Deck and the
	// current top card (as Cards are dealt, the top card decreases, however
	// Cards are never removed from the list of Cards)
	protected Card[] deck;
	protected int topCard;

	/**
	 * Constructs a new Deck object given the number of standard 52 card decks
	 * this Deck consists of.
	 * @param noOfDecks the number of 52 card decks in this Deck
	 */
	public Deck(int noOfDecks)
	{
		deck = new Card[noOfDecks * 52];
		topCard = 0;

		// Fill this deck with face down Cards in the following order:
		// ac ad ah as 2c 2d 2h 2s ... kc kd kh ks
		// The top card will start as the last card added to this Deck
		for (int deckNo = 1; deckNo <= noOfDecks; deckNo++)
			for (int rank = 1; rank <= 13; rank++)
				for (int suit = 1; suit <= 4; suit++)
					deck[topCard++] = new Card(rank, suit);
	}

	/**
	 * Constructs a new Deck object that consists of one 52 card decks.s
	 */
	public Deck()
	{
		this(1);
	}

	/**
	 * Deals the top card from this Deck.
	 * @return the top Card of this Deck
	 */
	public Card dealCard()
	{
		if (topCard == 0)
			return null;
		return deck[--topCard];
	}

	/**
	 * Shuffles this Deck (collect all Card, flip them all face down, and
	 * scramble their order using the Fisher-Yates shuffle
	 */
	public void shuffle()
	{
		// Reset the top Card
		topCard = deck.length;

		// Shuffle using the Fisher–Yates shuffle method
		for (int card = 0; card < deck.length; card++)
		{
			// Swap each card with a random card that comes after it
			int randomCard = (int) (Math.random() * (deck.length - card))
					+ card;

			Card temp = deck[randomCard];
			deck[randomCard] = deck[card];
			deck[card] = temp;

			// Flip all Cards face down
			if (deck[card].isFaceUp())
				deck[card].flip();
		}
	}

	/**
	 * Gets the number of cards that are left in this Deck.
	 * @return the number of cards that remain in this Deck
	 */
	public int cardsLeft()
	{
		return topCard;
	}

	/**
	 * Returns this Deck's information as a String.
	 * @return the String representations of the Cards left in this deck
	 */
	public String toString()
	{
		// Add each remaining Card's String representation together
		StringBuilder deckStrBuild = new StringBuilder(3 * deck.length);
		for (int card = 0; card < topCard; card++)
			deckStrBuild.append(deck[card]).append(" ");

		return deckStrBuild.toString();
	}
}
