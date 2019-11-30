package go;

import java.awt.*;
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
	Player player;
	/** współrzędne kamienia */
	final double x;
	final double y;
	/** widoczność kamienia */
	boolean visibility = false;

	final int STONERADIUS = 15;

	/** Konstruktor kamienia */
	Stone(int row, int column, double x, double y) {
		//this.player = player;
		this.row = row;
		this.column = column;
		this.x = x;
		this.y = y;
	}

	public void setPlayer(Player player) {
		this.player = player;
		visibility = true;
		return;
	}

	public Color stoneColor() {
		if (player == Player.BLACK) return Color.BLACK;
		if (player == Player.WHITE) return Color.WHITE;
		else return null;
	}

	public boolean isInsideStone(double x, double y) {
		if (((x-this.x)*(x-this.x))+((y-this.y)*(y-this.y)) <= STONERADIUS*STONERADIUS) return true;
		else return false;
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
