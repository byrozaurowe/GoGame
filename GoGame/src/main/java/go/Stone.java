package go;

import java.util.ArrayList;

/** Klasa kamien */
public class Stone {

	/** Lista sasiadow kamienia */
	ArrayList<Stone> neighbour;
	/** Numer wiersza */
	final int row;
	/** Numer kolumny */
	final int column;
	/** Kolor kamienia */
	final Player player;

	/** Konstruktor kamienia */
	Stone(Player player, int row, int column) {
		this.player = player;
		this.row = row;
		this.column = column;
	}

	/*private void setNeighbours() {
		neighbour = new ArrayList<Stone>();
		if(row > 0) {
			neighbour.add(board[row - 1][column]);
		}
		if(row < Board.SIZE - 1) {
			neighbour.add(board[Board.SIZE - 1][column]);
		}
		if(column > 0) {
			neighbour.add(board[row][column - 1]);
		}
		if(column < )
	}*/
}
