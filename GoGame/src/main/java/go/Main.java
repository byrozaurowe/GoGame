package go;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.lang.Integer.SIZE;

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

	/**
	 * Konstruktor maina
	 */
	Main() {
		super("Go Game");
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

	public static void main(String[] args) {
		JFrame frame = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
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