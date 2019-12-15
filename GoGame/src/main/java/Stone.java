import java.awt.*;
import java.awt.geom.Ellipse2D;

/** Klasa kamien */
public class Stone {
	
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
		circle = new Ellipse2D.Double(x-Board.STONERADIUS, y-Board.STONERADIUS, Board.STONERADIUS*2, Board.STONERADIUS*2);
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

    public void setPlayer(String player) {
		if(player == "WHITE")
			setPlayer(Player.WHITE);
		else if(player == "BLACK")
			setPlayer(Player.BLACK);
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
		if (((x-this.x)*(x-this.x))+((y-this.y)*(y-this.y)) <= Board.STONERADIUS* Board.STONERADIUS) return true;
		else return false;
	}
	void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
}
