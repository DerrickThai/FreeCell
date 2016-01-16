import java.util.Comparator;

/**
 * Keeps track of a Card's information including its rank, suit, and whether
 * it's face up or not. Can get the rank of the Card, flip the Card, check if
 * the Card is face up or not, check if the Card is an ace, check if the Card
 * can be placed on a Cascade or Foundation given its respective top Card,
 * compare Cards, sort Cards by rank first then suit or suit first then rank,
 * and can return a String representation of the Card.
 * 
 * @author Derrick Thai
 * @version November 4, 2014
 */
public class Card implements Comparable<Card>
{
	// String representations of the ranks and suits
	// --------------------------------------------1
	// Integer values of the ranks ------ 1234567890123
	private static final String RANKS = " A23456789TJQK";
	// Integer values of the suits ------ 1234
	private static final String SUITS = " CDHS";

	// Comparator to sort Cards by their suit first then rank
	public static final Comparator<Card> SUIT_ORDER = new SuitOrder();

	// Instance variables to keep track of this Card's rank, suit, and whether
	// this Card is face up or not
	private int rank, suit;
	private boolean faceUp;

	/**
	 * Constructs a new Card object with the given rank (A to K -> 1 to 13),
	 * suit (C, D, H, S -> 1, 2, 3, 4), and whether the card is face up or not.
	 * @param rank the integer value of the rank of the card
	 * @param suit the integer value of the suit of the card
	 * @param faceUp whether the card is face up or not
	 */
	public Card(int rank, int suit, boolean faceUp)
	{
		this.rank = rank;
		this.suit = suit;
		this.faceUp = faceUp;
	}

	/**
	 * Constructs a new Card object with the given rank (A to K -> 1 to 13),
	 * suit (C, D, H, S -> 1, 2, 3, 4), assumes the card is not face up.
	 * @param rank the integer value of the rank of the card
	 * @param suit the integer value of the suit of the card
	 */
	public Card(int rank, int suit)
	{
		this(rank, suit, false);
	}

	/**
	 * Constructs a new Card object with the given String.
	 * @param card a String that contains information about this Card's rank,
	 *            suit, and whether it's face up or not (in the format
	 *            "letter / number of rank and letter of suit" not separated by
	 *            a space, face up cards having these letters in upper case and
	 *            face down cards having these letters in lower case)
	 */
	public Card(String card)
	{
		// Check if face up by looking at the case of the suit letters
		if (!(faceUp = Character.isUpperCase(card.charAt(1))))
			card = card.toUpperCase();
		rank = RANKS.indexOf(card.charAt(0));
		suit = SUITS.indexOf(card.charAt(1));
	}

	/**
	 * Gets the numeric rank of this Card (A to K = 1 to 13).
	 * @return the numeric rank of this Card
	 */
	public int getRank()
	{
		return rank;
	}

	/**
	 * Flips the card (face up to down, face down to up).
	 */
	public void flip()
	{
		faceUp = !faceUp;
	}

	/**
	 * Checks if this Card is face up or not.
	 * @return true if this Card is face up and false if this Card is face down
	 */
	public boolean isFaceUp()
	{
		return faceUp;
	}

	/**
	 * Checks if this Card is an Ace or not.
	 * @return true if this Card is an Ace and false if it is not an Ace
	 */
	public boolean isAce()
	{
		return rank == 1;
	}

	/**
	 * Checks if this Card can be placed on a Cascade whose top Card is the
	 * given Card.
	 * @param cascadeCard the top Card of the Cascade to be placed on
	 * Precondition: this Card and cascadeCard cannot be null
	 * @return true if this Card can be placed on the top Card of the Cascade or
	 *         false if not
	 */
	public boolean canPlaceOnCascade(Card cascadeCard)
	{
		// Removed to avoid null checking
		// if(cascadeCard == null)
		// return true;

		// Can place on if suits are of opposite colour and the rank of this
		// Card is one less than the rank of the top Card of the Cascade
		return !(suit == cascadeCard.suit || suit + cascadeCard.suit == 5)
				&& rank == cascadeCard.rank - 1;
	}

	/**
	 * Checks if this Card can be placed on a Foundation whose top Card is the
	 * given Card.
	 * @param foundationCard the top Card of the Foundation to be placed on
	 * Precondition: this Card and foundationCard cannot be null
	 * @return true if this Card can be placed on the top Card of the Foundation
	 *         or false if not
	 */
	public boolean canPlaceOnFoundation(Card foundationCard)
	{
		// Removed to avoid null checking
		// if(foundationCard == null)
		// return isAce();

		// Can place on if ranks are the same and this Card's rank is one more
		// than the top Card of the Foundation
		return suit == foundationCard.suit && rank == foundationCard.rank + 1;
	}

	/**
	 * Compares this Card to another Card by comparing their ranks first and
	 * then suits.
	 * @param other the Card to compare to this Card
	 * @return a value < 0 if this Card's rank is lower than the other Card's
	 *         rank (or if they have the same rank but this Card's suit has a
	 *         lower value than the other Card's suit), a value > 0 if this
	 *         Card's rank is greater than the other Card's rank (or if they
	 *         have the same rank but this Card's suit has a greater value than
	 *         the other Card's suit), or 0 if the Cards have the same values
	 *         for their ranks and suits
	 */
	public int compareTo(Card other)
	{
		if (this.rank != other.rank)
			return this.rank - other.rank;
		return this.suit - other.suit;
	}

	/**
	 * Returns this Card's information as a String.
	 * @return this Card's rank as a letter / number followed by this Card's
	 *         suit as a letter, the entire String being upper case if the card
	 *         is face up or lower case if the card is face down
	 */
	public String toString()
	{
		if (faceUp)
			return String
					.format("%c%c", RANKS.charAt(rank), SUITS.charAt(suit));
		return String.format("%c%c", RANKS.charAt(rank), SUITS.charAt(suit))
				.toLowerCase();
	}

	/**
	 * An inner Comparator class that compares two Cards by their suits
	 */
	private static class SuitOrder implements Comparator<Card>
	{
		/**
		 * Compares two Cards by their suits first then rank.
		 * @param first the first card to compare
		 * @param second the second card to compare
		 * @return a value < 0 if the first Card's suit has a lower value than
		 *         the second Card's suit (or if they have the same suit but the
		 *         first Card's rank is lower than the second Card's rank), a
		 *         value > 0 if the first Card's suit has a greater value than
		 *         the second Card's suit (or if they have the same suit but the
		 *         first Card's rank is greater than the other Card's rank), or
		 *         0 if the Cards have the same values for their ranks and suits
		 */
		public int compare(Card first, Card second)
		{
			if (first.suit != second.suit)
				return first.suit - second.suit;
			return first.rank - second.rank;
		}
	}
}
