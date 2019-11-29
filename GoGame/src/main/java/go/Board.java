package go;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Board extends JPanel{
	final int SIZE = 13;
	final int DIMX = 600, DIMY = 600;

	public Board() {
		this.setSize(new Dimension(DIMX, DIMY));
		repaint();
	}

	public void paintBackground() {

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for (int i=0; i<SIZE; i++) { // pionowe linie
				Point2D x = new Point2D.Double((DIMX / SIZE) * i, 0);
				Point2D y = new Point2D.Double((DIMX / SIZE) * (i + 1), DIMY);
				Line2D line = new Line2D.Double(x,y);
				g2d.draw(line);
		}
		for (int i=0; i<SIZE; i++) { // poziome linie
			Point2D x = new Point2D.Double(0, (DIMY / SIZE) * i);
			Point2D y = new Point2D.Double(DIMX, (DIMX / SIZE) * (i + 1));
			Line2D line = new Line2D.Double(x,y);
			g2d.draw(line);
		}
	}
}
