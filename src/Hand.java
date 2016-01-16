import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Keeps track of a Hand's information including the Card objects in the Hand.
 * Can add Cards to the Hand, get the Blackjack value of the Hand, clear all
 * Cards from the Hand, check if the Hand is a Blackjack hand, sort the Hand by
 * rank or by suit, and can return a String representation of this Hand.
 * @author Derrick Thai
 * @version November 6, 2014
 */
public class Hand
{
	// ArrayList to keep track of the Cards in this Hand
	protected ArrayList<Card> hand;

	/**
	 * Constructs a new Hand object with the given String.
	 * @param handStr a String that contains the Cards in this Hand in the
	 *            format "RS rs RS RS rs", where R/r is the letter
	 *            representation of the rank of the card (A, 1...9, T, J, Q, K)
	 *            and S/s is the letter representation of the suit (C, D, H, S)
	 *            of the card (face up Cards are upper case, while face down
	 *            Cards are lower case)
	 */
	public Hand(String handStr)
	{
		// No Cards results in an empty Hand
		if (handStr.isEmpty())
			hand = new ArrayList<Card>();
		else
		{
			// Add each Card to the Hand's list of Cards
			StringTokenizer handTokens = new StringTokenizer(handStr);
			hand = new ArrayList<Card>((handStr.length() + 1) / 3);
			while (handTokens.hasMoreTokens())
				hand.add(new Card(handTokens.nextToken()));
		}
	}

	/**
	 * Constructs a new Hand object that has no Cards.
	 */
	public Hand()
	{
		this("");
	}

	/**
	 * Adds a Card to this Hand.
	 * @param card the Card to add to this Hand
	 */
	public void addCard(Card card)
	{
		hand.add(card);
	}

	/**
	 * Removes all of the Cards from this Hand.
	 */
	public void clear()
	{
		hand.clear();
	}

	/**
	 * Sorts the Cards in this Hand by rank first then suit.
	 */
	public void sortByRank()
	{
		Collections.sort(hand);
	}

	/**
	 * Sorts the Cards in this Hand by suit first then rank.
	 */
	public void sortBySuit()
	{
		Collections.sort(hand, Card.SUIT_ORDER);
	}

	/**
	 * Gets the number of Cards left in this Hand.
	 * @return the number of Cards left in this Hand.
	 */
	public int cardsLeft()
	{
		return hand.size();
	}

	/**
	 * Returns this Hand's information as a String.
	 * @return the String representation of each Card in this Hand in their
	 *         current order
	 */
	public String toString()
	{
		// Add each Card's String representation together
		StringBuilder handStrBuild = new StringBuilder(3 * hand.size());
		for (Card card : hand)
			handStrBuild.append(card).append(" ");

		return handStrBuild.toString();
	}
}
