package go;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import static go.Board.STONERADIUS;

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
	/** Widoczność kamienia */
	Visibility visibility = Visibility.INVISIBLE;
	Ellipse2D circle;

	/** Konstruktor kamienia */
	Stone(int row, int column, double x, double y) {
		//this.player = player;
		this.row = row;
		this.column = column;
		this.x = x;
		this.y = y;
		circle = new Ellipse2D.Double(x-STONERADIUS, y-STONERADIUS, STONERADIUS*2, STONERADIUS*2);
	}

	/** Status kamienia (widocznosc) */
	public enum Visibility {
		INVISIBLE, VISIBLE, HALFVISIBLE;
	}

	/** Przypisuje gracza kamieniowi */
	public void setPlayer(Player player) {
		this.player = player;
		visibility = Visibility.VISIBLE;
    }

    /** Ustawienie koloru kamieniowi */
	public Color stoneColor() {
		if (player == Player.BLACK) return Color.BLACK;
		if (player == Player.WHITE) return Color.WHITE;
		if (visibility == Visibility.HALFVISIBLE) return Color.DARK_GRAY;
		else return null;
	}

    /** Sprawdza czy wspolrzedne sa wewnatrz jakiegos kamienia
     * @param x wspolrzedna x myszy
     * @param y wspolrzedna y myszy
     */
	public boolean isInsideStone(double x, double y) {
		if (((x-this.x)*(x-this.x))+((y-this.y)*(y-this.y)) <= STONERADIUS* STONERADIUS) return true;
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
