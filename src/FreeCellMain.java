import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * Main Frame for a Simple FreeCell game. Sets up the menus and places a
 * CardPanel in the Frame.
 * 
 * @author Ridout and Derrick Thai
 * @version November 2014
 */
public class FreeCellMain extends JFrame implements ActionListener,
		ChangeListener
{
	// To remove yellow warning
	private static final long serialVersionUID = 1L;

	// Constants for the JSlider
	private static final int MIN_FRAMES = 1;
	private static final int MAX_FRAMES = 6;
	private static final int STARTING_FRAMES = 3;

	// Declare instance variables
	private CardPanel cardArea;
	private JMenuItem newMenuItem, undoOption, statisticsOption, quitMenuItem,
			aboutMenuItem, howToPlayItem;

	private JCheckBoxMenuItem autoCompleteOption, animateOption;
	private JSlider animationSlider;

	private boolean autoComplete, animate;
	private int animationFrames;

	/**
	 * Creates a FreeCellMain from object
	 */
	public FreeCellMain()
	{
		// Set the title of the window plus make it not resizable
		super("FreeCell");
		setResizable(false);

		// Add a window listener to confirm window closing
		addWindowListener(new CloseWindow());

		// Add in an Icon - Ace of Spades
		setIconImage(new ImageIcon("images\\ace.png").getImage());

		// Add the Game Menu to the menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G'); // Alt + G

		// Set up the Menu Items
		newMenuItem = new JMenuItem("New Game");
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		newMenuItem.addActionListener(this);

		undoOption = new JMenuItem("Undo Move");
		undoOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		undoOption.addActionListener(this);
		undoOption.setEnabled(false);

		statisticsOption = new JMenuItem("Statistics");
		statisticsOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		statisticsOption.addActionListener(this);

		quitMenuItem = new JMenuItem("Exit");
		quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_MASK));
		quitMenuItem.addActionListener(this);

		// Set up the auto complete and animation check boxes
		autoCompleteOption = new JCheckBoxMenuItem("Auto Complete");
		autoCompleteOption.addActionListener(this);
		autoCompleteOption.setSelected(true);
		autoComplete = true;

		animateOption = new JCheckBoxMenuItem("Animation");
		animateOption.addActionListener(this);
		animateOption.setSelected(true);
		animate = true;

		// Set up the animation frames JSlider
		animationFrames = STARTING_FRAMES;
		animationSlider = new JSlider(JSlider.HORIZONTAL, MIN_FRAMES,
				MAX_FRAMES, STARTING_FRAMES);
		animationSlider.addChangeListener(this);
		animationSlider.setMajorTickSpacing(1);
		animationSlider.setPaintTicks(true);
		animationSlider.setPaintLabels(true);

		Dimension originalSize = animationSlider.getPreferredSize();
		animationSlider.setPreferredSize(new Dimension(
				originalSize.width - 100,
				originalSize.height + 10));

		// Add the game menu items in preferred order
		gameMenu.add(newMenuItem);
		gameMenu.add(undoOption);
		gameMenu.add(statisticsOption);

		gameMenu.addSeparator();
		gameMenu.add(autoCompleteOption);
		gameMenu.add(animateOption);

		gameMenu.addSeparator();
		gameMenu.add(new JLabel("         Animation Frames"));
		gameMenu.add(animationSlider);

		gameMenu.addSeparator();
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);

		// Add the Help Menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H'); // Alt + H
		howToPlayItem = new JMenuItem("How to Play");
		howToPlayItem.addActionListener(this);
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(howToPlayItem);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);

		// Add the menu bar to the frame, set up the layout, and add in a
		// CardPanel for the card area
		setJMenuBar(menuBar);
		setLayout(new BorderLayout());
		cardArea = new CardPanel(this);
		add(cardArea, BorderLayout.CENTER);

		// Centre the frame in (almost) the middle of the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setVisible(true);
		setLocation((screen.width - CardPanel.WIDTH) / 2 - this.getWidth(),
				(screen.height - CardPanel.HEIGHT) / 2 - this.getHeight());
	}

	/**
	 * Method that deals with the menu options.
	 * 
	 * @param event the event that triggered this method
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == newMenuItem)
		{
			// Confirm that the user accepts defeat if they start a new game
			// during a game and count that as a loss if they click yes
			if (!cardArea.isInGame())
				cardArea.newGame();
			else if (JOptionPane
					.showConfirmDialog(
							cardArea,
							"If you create a new game in the middle"
									+ " of a game, it will count as a loss."
									+ " Are you sure you want to play a new game?",
							"Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				cardArea.updateStatsAfterLose();
				cardArea.newGame();
			}

		}
		else if (event.getSource() == undoOption)
		{
			cardArea.undo();
			if (!cardArea.canUndo())
				setUndoOption(false);
		}
		else if (event.getSource() == statisticsOption)
		{
			cardArea.showStats();

		}
		else if (event.getSource() == autoCompleteOption)
		{
			autoComplete = !autoComplete;
		}
		else if (event.getSource() == animateOption)
		{
			// Enable slider only when animation is on
			animate = !animate;
			animationSlider.setEnabled(animate);
		}
		else if (event.getSource() == quitMenuItem)
		{
			// Confirm that the user accepts defeat if they exit during a game
			// and count that as a loss if they click yes
			if (cardArea.isInGame()
					&& JOptionPane
							.showConfirmDialog(
									cardArea,
									"If you create a new game in the"
											+ " middle of a game, it will"
											+ " count as a loss. Are you sure"
											+ " you want to play a new game?",
									"Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				cardArea.updateStatsAfterLose();
				System.exit(0);
			}
		}
		else if (event.getSource() == aboutMenuItem)
		{
			JOptionPane.showMessageDialog(cardArea,
					"FreeCell by Ridout\nand Derrick Thai\n\u00a9 2014",
					"About FreeCell", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == howToPlayItem)
		{
			// Try to open the Wikipedia page on FreeCell
			try
			{
				Desktop.getDesktop().browse(
						new URL("http://en.wikipedia.org/wiki/FreeCell")
								.toURI());
			}
			catch (Exception exp)
			{
				exp.printStackTrace();
			}
		}
	}

	/**
	 * Handles the JSlider events.
	 * @param event the event that triggered this method
	 */
	public void stateChanged(ChangeEvent event)
	{
		// Change the animation frames to the slider's value
		if (event.getSource() == animationSlider)
			if (!animationSlider.getValueIsAdjusting())
				animationFrames = animationSlider.getValue();
	}

	/**
	 * Sets the Undo option in the Menu.
	 * @param canUndo if you can undo or not
	 */
	public void setUndoOption(boolean canUndo)
	{
		this.undoOption.setEnabled(canUndo);
	}

	/**
	 * Checks if auto complete is on.
	 * @return true if auto complete is on or false if not
	 */
	public boolean isAutoCompleteOn()
	{
		return autoComplete;
	}

	/**
	 * Checks if animation is on.
	 * @return true if animation is on or false if not
	 */
	public boolean isAnimateOn()
	{
		return animate;
	}

	/**
	 * Gets the current animation frames.
	 * @return the current animation frames
	 */
	public int getAnimationFrames()
	{
		return animationFrames;
	}

	public static void main(String[] args)
	{
		FreeCellMain frame = new FreeCellMain();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Private inner class that prompts the user when exiting in the middle of a
	 * game instead of just closing the window.
	 */
	private class CloseWindow extends WindowAdapter
	{
		/**
		 * Asks the user to confirm that they understand that if they exit
		 * before finishing a game it will count as a loss.
		 * @param event the event that triggered this method
		 */
		public void windowClosing(WindowEvent event)
		{
			if (!cardArea.isInGame())
				System.exit(0);
			else if (JOptionPane
					.showConfirmDialog(
							cardArea,
							"If you exit in the middle of a game, "
									+ "it will count as a loss. Are you sure you want to exit?",
							"Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				cardArea.updateStatsAfterLose();
				System.exit(0);
			}
		}
	}

}
