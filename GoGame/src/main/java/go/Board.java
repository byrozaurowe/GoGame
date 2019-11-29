package go;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Board extends JPanel{
	final int SIZE = 13;
	int dimX = 600, dimY = 600;

	public Board() {
		this.setPrefferedSize(new Dimension(dimX, dimY));
		repaint();
	}

	public void paintBackground() {

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for (int i=0; i<SIZE; i++) { //poziome linie
				Point2D x = new Point2D.Double((dimX / SIZE) * i, 0);
				Point2D y = new Point2D.Double((dimX / SIZE) * (i + 1), dimY);
				Line2D line = new Line2D.Double(x,y);
				g2d.draw(line);
		}
		for (int i=0; i<SIZE; i++) { //poziome linie
			Point2D x = new Point2D.Double(0, (dimY / SIZE) * i);
			Point2D y = new Point2D.Double(dimX, (dimX / SIZE) * (i + 1));
			Line2D line = new Line2D.Double(x,y);
			g2d.draw(line);
		}
	}
}
