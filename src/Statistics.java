import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Keeps track of the Statistics for a FreeCell game include the games played
 * and won, current and longest win streak, win percentage, and fastest time.
 * @author Derrick Thai
 * @version November 27, 2014
 */
public class Statistics implements Serializable
{
	// To remove the yellow warning
	private static final long serialVersionUID = 1L;
	
	// Name of file to save Statistics to
	public static final String STATS_FILE = "stats.dat";

	// Variables to keep track of statistics for a FreeCell game
	private int gamesPlayed;
	private int gamesWon;
	private int currentStreak;
	private int longestStreak;
	private double winPercentage;
	private int fastestTime;

	/**
	 * Constructs a new Statistics object with all of the statistics reset to
	 * their starting values.
	 */
	public Statistics()
	{
		gamesPlayed = 0;
		gamesWon = 0;
		winPercentage = 0;
		currentStreak = 0;
		longestStreak = 0;
		winPercentage = 0;
		fastestTime = Integer.MAX_VALUE;
	}

	/**
	 * Writes this Statistics object to a file with the given name.
	 * @param fileName the name of the file to write to
	 */
	public void writeToFile(String fileName)
	{
		try
		{
			// Write the entire object
			ObjectOutputStream fileOut = new ObjectOutputStream(
					new FileOutputStream(fileName));
			fileOut.writeObject(this);
			fileOut.close();
		}
		catch (IOException exp)
		{
			System.out.println("Error writing to the file");
		}
	}

	/**
	 * Reads a Statistics object from a file with the given name.
	 * @param fileName the name of the file to read from
	 * @return the Statistics object read from the file
	 */
	public static Statistics readFromFile(String fileName)
	{
		try
		{
			// Read the entire object
			ObjectInputStream fileIn = new ObjectInputStream(
					new FileInputStream(fileName));
			Statistics stats = (Statistics) fileIn.readObject();
			fileIn.close();
			return stats;
		}
		catch (Exception exp)
		{
			// If the file does not exist, return a new Statistics object
			return new Statistics();
		}
	}

	/**
	 * Increments the number of games played by one and updates the win
	 * percentage as well. Precondition: if the game played was a win, this
	 * method is called before calling incrementGamesWon()
	 */
	public void incrementGamesPlayed()
	{
		gamesPlayed++;
		winPercentage = (double) gamesWon / gamesPlayed * 100;
	}

	/**
	 * Increments the number of games won by one.
	 */
	public void incrementGamesWon()
	{
		gamesWon++;
	}

	/**
	 * Increments the current streak by one.
	 */
	public void incrementCurrentStreak()
	{
		currentStreak++;
	}

	/**
	 * Resets the current streak to zero.
	 */
	public void resetCurrentStreak()
	{
		currentStreak = 0;
	}

	/**
	 * Updates the longest steak if necessary.
	 */
	public void updateLongestStreak()
	{
		if (currentStreak > longestStreak)
			longestStreak = currentStreak;
	}

	/**
	 * Updates the fastest time if the given time is faster than the current
	 * fastest time.
	 * @param time the time for the game that just finished
	 */
	public void updateFastestTime(int time)
	{
		if (time < fastestTime)
			fastestTime = time;
	}

	/**
	 * Returns a String representation of all of the statistics.
	 */
	public String toString()
	{
		// Let minutes surpass 60 since it is uncommon for one to play an hour
		// long game
		String time;
		if (fastestTime != Integer.MAX_VALUE)
			time = String.format("%d:%02d", fastestTime / 60, fastestTime % 60);
		else
			time = "N/A";

		// Represent each statistic on a separate line
		return String
				.format("Games Played: %46d%nGames Won: %50d%nWin Percentage: %36.2f%%%n"
						+ "Current Streak: %45d%nLongest Streak: %44d%nFastest Time: %46s",
						gamesPlayed, gamesWon, winPercentage, currentStreak,
						longestStreak, time);
	}
}
