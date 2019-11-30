package go;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Board extends JPanel{
	final int SIZE = 13;
	final int STONERADIUS = 15;
	final int DIMX = 600, DIMY = 600;
	static Stone[][] boardTab;

	public Board() {
		setBackground(new Color(193,154,107));
		this.setPreferredSize(new Dimension(DIMX, DIMY));
		boardTab = new Stone [SIZE][SIZE];
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				boardTab[i][j] = new Stone(i,j,(i*DIMX/(SIZE-1)),(j*DIMY/(SIZE-1)));
			}
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for (int i=0; i<SIZE; i++) { // pionowe linie
				Point2D x = new Point2D.Double((DIMX / (SIZE-1)) * i, 0);
				Point2D y = new Point2D.Double((DIMX / (SIZE-1)) * i, DIMY);
				Line2D line = new Line2D.Double(x,y);
				g2d.draw(line);
		}
		for (int i=0; i<SIZE; i++) { // poziome linie
			Point2D x = new Point2D.Double(0, (DIMY / (SIZE-1)) * i);
			Point2D y = new Point2D.Double(DIMX, (DIMX / (SIZE-1)) * i);
			Line2D line = new Line2D.Double(x,y);
			g2d.draw(line);
		}
		for (int i=0; i<SIZE; i++) { //rysowanie kamieni
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].visibility == true) {
					Ellipse2D circle = new Ellipse2D.Double(boardTab[i][j].x-STONERADIUS, boardTab[i][j].y-STONERADIUS, STONERADIUS*2, STONERADIUS*2);
					g2d.setPaint(boardTab[i][j].stoneColor());
					g2d.draw(circle);
					g2d.fill(circle);
				}
			}
		}
	}
}
