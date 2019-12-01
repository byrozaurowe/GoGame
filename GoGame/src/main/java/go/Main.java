package go;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Głowna klasa gracza */
public class Main extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

	/** Pasek menu */
	JMenuBar menuBar;
	/** Opcje na pasku menu */
	JMenu optionsMenu;
	/** Info na pasku menu */
	JMenu infoMenu;
	/** Opcja nowa gra */
	JMenuItem newGameItem;
	/** Opcja wyjdz z gry */
	JMenuItem exitItem;
	/** Opcja spasuj kolejke */
	JMenuItem passItem;
	/** Informacja o zasadach */
	JMenuItem rulesItem;
	/** Informacja o autorach */
	JMenuItem authorsItem;
	/** Plansza */
	Board board;

	JButton botStartButton;

	JButton playerStartButton;

	JPanel menuPanel;

	JPanel titlePanel;

	JPanel descPanel;

	JPanel radioPanel;

	JPanel playerPanel;

	JPanel botPanel;

	JLabel titleLabel;

	JLabel descLabel;

	ButtonGroup buttonGroup;

	JRadioButton big;
	JRadioButton normal;
	JRadioButton small;
	/**
	 * Konstruktor maina
	 */
	Main() {
		super("Go Game");
		// ustawianie nazw buttonow i labeli
		Font labelFont = new Font("Segoe UI", Font.PLAIN, 20);
		botStartButton = new JButton("Start game against a computer");
		botStartButton.setFont(labelFont);
		playerStartButton = new JButton("Start game against player");
		playerStartButton.setFont(labelFont);
		titleLabel = new JLabel("Go Game");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		descLabel = new JLabel("To play, select the board size and mode");
		descLabel.setFont(labelFont);
		// ustawienie panelu glownego
		menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

		// ustawienie radio buttonow
		buttonGroup = new ButtonGroup();
		big = new JRadioButton("19x19");
		normal = new JRadioButton("13x13");
		small = new JRadioButton("9x9");
		Font radioFont = new Font("Segoe UI", Font.PLAIN, 16);
		radioPanel = new JPanel();
		buttonGroup.add(big);
		radioPanel.add(big);
		big.setFont(radioFont);
		buttonGroup.add(normal);
		radioPanel.add(normal);
		normal.setFont(radioFont);
		buttonGroup.add(small);
		radioPanel.add(small);
		small.setFont(radioFont);

		// dodawanie elementow do ich paneli
		titlePanel = new JPanel();
		titlePanel.add(titleLabel);
		descPanel = new JPanel();
		descPanel.add(descLabel);
		playerPanel = new JPanel();
		playerPanel.add(playerStartButton);
		botPanel = new JPanel();
		botPanel.add(botStartButton);
		// dodawanie paneli do panelu glownego
		menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		menuPanel.add(titlePanel);
		menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		menuPanel.add(descPanel);
		menuPanel.add(radioPanel);
		menuPanel.add(playerPanel);
		menuPanel.add(botPanel);
		add(menuPanel);
		pack();

	}

	public static void main(String[] args) {
		JFrame frame = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//frame.setResizable(false);
	}

	/** Metoda actionPerformed */
	public void actionPerformed(ActionEvent actionEvent) {
		Object event = actionEvent.getSource();
		if (event == exitItem) {
			System.exit(0);
		} else if (event == authorsItem) {
			JOptionPane.showMessageDialog(null, "Authors: Wiktoria Byra, Wojciech Pakulski", "Authors", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(event == botStartButton) {
			gameInitialize();
		}
	}

	void gameInitialize() {
		// tworzenie menu
		menuBar = new JMenuBar();
		optionsMenu = new JMenu("Options");
		passItem = new JMenuItem("Pass");
		newGameItem = new JMenuItem("New Game");
		exitItem = new JMenuItem("Exit");
		// dodawanie elementow do menu
		optionsMenu.add(passItem);
		optionsMenu.add(newGameItem);
		optionsMenu.add(exitItem);
		menuBar.add(optionsMenu);
		setJMenuBar(menuBar);
		// menu info z opcjami zasady i autorzy
		infoMenu = new JMenu("Info");
		rulesItem = new JMenuItem("Rules");
		authorsItem = new JMenuItem("Authors");
		infoMenu.add(rulesItem);
		infoMenu.add(authorsItem);
		menuBar.add(infoMenu);

		setLocationByPlatform(true);
		board = new Board();
		add(board, BorderLayout.CENTER);

		// action Listenery
		passItem.addActionListener(this);
		newGameItem.addActionListener(this);
		exitItem.addActionListener(this);
		rulesItem.addActionListener(this);
		authorsItem.addActionListener(this);

		//mouse listener
		board.addMouseListener(this);
		board.addMouseMotionListener(this);
		pack();
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		board.clickedOnStone(mouseEvent.getX(), mouseEvent.getY());
	}

	public void mousePressed(MouseEvent mouseEvent) {
		board.enteredStone(mouseEvent);

	}

	public void mouseReleased(MouseEvent mouseEvent) {
		board.releasedStone();
	}

	public void mouseEntered(MouseEvent mouseEvent) {

	}

	public void mouseExited(MouseEvent mouseEvent) {

	}

	public void mouseDragged(MouseEvent mouseEvent) {
		board.draggedStone(mouseEvent);
	}

	public void mouseMoved(MouseEvent mouseEvent) {

	}
}