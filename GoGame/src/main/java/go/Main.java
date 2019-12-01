package go;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Głowna klasa gracza */
public class Main extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

	static Menu menu;
	/** Pasek menu */
	private JMenuBar menuBar;
	/** Opcje na pasku menu */
	private JMenu optionsMenu;
	/** Info na pasku menu */
	private JMenu infoMenu;
	/** Opcja nowa gra */
	private JMenuItem newGameItem;
	/** Opcja wyjdz z gry */
	private JMenuItem exitItem;
	/** Opcja spasuj kolejke */
	private JMenuItem passItem;
	/** Informacja o zasadach */
	private JMenuItem rulesItem;
	/** Informacja o autorach */
	private JMenuItem authorsItem;
	/** Plansza */
	private Board board;

	/** Konstruktor maina
	 * @param gameSize rozmiar planszy
	 * */
	Main(int gameSize) {
		gameInitialize(gameSize);
	}

	/** Funkcja main
	 * @param args puste
	 */
	public static void main(String[] args) {
		menu = new Menu();
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setVisible(true);
		menu.setResizable(false);
	}

	/** Metoda actionPerformed */
	public void actionPerformed(ActionEvent actionEvent) {
		Object event = actionEvent.getSource();
		if (event == exitItem) {
			System.exit(0);
		} else if (event == authorsItem) {
			JOptionPane.showMessageDialog(null, "Authors: Wiktoria Byra, Wojciech Pakulski", "Authors", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/** Tworzy okno gry */
	void gameInitialize(int boardSize) {
		// tworzenie menu
		menuBar = new JMenuBar();
		optionsMenu = new JMenu("Options");
		passItem = new JMenuItem("Pass");
		newGameItem = new JMenuItem("Menu");
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
		board = new Board(boardSize);
		add(board, BorderLayout.CENTER);
		setLocationByPlatform(true);

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