package go;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Board extends JPanel{
	final int SIZE = 13;
	final static int STONERADIUS = 15;
	final int DIMX = 600, DIMY = 600;
	static Stone[][] boardTab;
	Stone lastMovedField;

	public Board() {
		setBackground(new Color(193,154,107));
		this.setPreferredSize(new Dimension(DIMX+2*STONERADIUS, DIMY+2*STONERADIUS));
		boardTab = new Stone [SIZE][SIZE];
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				boardTab[i][j] = new Stone(i,j,(i*DIMX/(SIZE-1))+STONERADIUS,(j*DIMY/(SIZE-1))+STONERADIUS);
			}
		}
		repaint();
	}

	public void clickedOnStone(double x, double y) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].isInsideStone(x,y)) {
					boardTab[i][j].setPlayer(Player.BLACK);
				}
			}
		}
		repaint();
	}

	//podświetlanie najechanych pól
	public void enteredStone(MouseEvent event) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].circle.contains(event.getPoint()) && boardTab[i][j].visibility == Stone.Visibility.INVISIBLE) {
					lastMovedField = boardTab[i][j];
					boardTab[i][j].visibility= Stone.Visibility.HALFVISIBLE;
				}
			}
		}
		repaint();
	}
	public void draggedStone(MouseEvent event) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].circle.contains(event.getPoint()) && boardTab[i][j].visibility == Stone.Visibility.INVISIBLE) {
				    if(lastMovedField != null) {
                        lastMovedField.visibility = Stone.Visibility.INVISIBLE;
                        lastMovedField = boardTab[i][j];
                        boardTab[i][j].visibility = Stone.Visibility.HALFVISIBLE;
                    }
				}

			}
		}
		repaint();
	}
	public void releasedStone(MouseEvent event) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
			    if(lastMovedField != null) {
                    lastMovedField.setPlayer(Player.BLACK);
                }
			}
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		//wydrukowanie tła
		Image bgImage = new ImageIcon("wood.jpg").getImage();
		g.drawImage(bgImage, 0, 0, null);

		for (int i=0; i<SIZE; i++) { // pionowe linie
				Point2D x = new Point2D.Double((DIMX / (SIZE-1)) * i + STONERADIUS, STONERADIUS);
				Point2D y = new Point2D.Double((DIMY / (SIZE-1)) * i + STONERADIUS, DIMY+STONERADIUS);
				Line2D line = new Line2D.Double(x,y);
				g2d.setPaint(Color.DARK_GRAY);
				g2d.setStroke(new BasicStroke(3));
				g2d.draw(line);
		}
		for (int i=0; i<SIZE; i++) { // poziome linie
			Point2D x = new Point2D.Double(STONERADIUS, (DIMY / (SIZE-1)) * i + STONERADIUS);
			Point2D y = new Point2D.Double(DIMX + STONERADIUS, (DIMX / (SIZE-1)) * i + STONERADIUS);
			Line2D line = new Line2D.Double(x,y);
			g2d.setPaint(Color.DARK_GRAY);
			g2d.setStroke(new BasicStroke(3));
			g2d.draw(line);
		}
		for (int i=0; i<3; i++) { //rysowanie punktów strategicznych
			for (int j=0; j<3; j++) {
				Ellipse2D circle = new Ellipse2D.Double(DIMX/4*(i+1)-5+STONERADIUS, DIMY/4*(j+1)-5+STONERADIUS, 10, 10);
				g2d.setPaint(Color.DARK_GRAY);
				g2d.draw(circle);
				g2d.fill(circle);
			}
		}
		for (int i=0; i<SIZE; i++) { //rysowanie kamieni
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].visibility == Stone.Visibility.VISIBLE) {
					g2d.setPaint(boardTab[i][j].stoneColor());
					g2d.draw(boardTab[i][j].circle);
					g2d.fill(boardTab[i][j].circle);
				}

				//wyświetlanie najchanych pól
				if (boardTab[i][j].visibility == Stone.Visibility.HALFVISIBLE) {
					Ellipse2D circle = new Ellipse2D.Double(boardTab[i][j].x-STONERADIUS, boardTab[i][j].y-STONERADIUS, STONERADIUS*2, STONERADIUS*2);
					g2d.setPaint(boardTab[i][j].stoneColor());
					/*float alpha = i * 0.5f; //półprzeźroczyste
					AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
					g2d.setComposite(alcom); */
					g2d.draw(circle);
					g2d.fill(circle);
				}
			}
		}
	}
}
