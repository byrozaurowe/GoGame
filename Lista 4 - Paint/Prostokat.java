import java.awt.Graphics;
import static java.lang.Math.*;
import java.awt.*;

/** Klasa prostokąt dziedzicząca po figurze */
public class Prostokat extends Figura {

	double a, b, x1, x2, y1, y2, x, y;
	
	/** Konstruktor klasy prostokąt 
	* @param x1 początkowa współrzędna iksowa
	* @param y1 początkowa współrzędna igrekowa
	* @param x2 końcowa współrzędna iksowa
	* @param y2 końcowa współrzędna igrekowa
	* @param wybranyKolor zmienna określająca kolor RGB
	*/
	public Prostokat(double x1, double y1, double x2, double y2, Color wybranyKolor) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.wybranyKolor = wybranyKolor;
		a = abs(x1 - x2);
		b = abs(y1 - y2);
		x = (x1 + x2) / 2;
		y = (y1 + y2) / 2;
	}
	
	
	@Override
	/** Metoda odpowiedzialna za sprawdzanie, czy mysz jest wewnątrz prostokąta 
	* @param px aktualne współrzędne iksowe myszy
	* @param py aktualne współrzędne igrekowe myszy
	* @return czy myszka jest w środku figury, czy nie
	*/
	public boolean isInside(double px, double py) {
		if (px > x-0.5*a && px < x+0.5*a && py> y-0.5*b &&py<y+0.5*b) {
			return true;
		}
		return false;
	}

	@Override
	/** Metoda wypisująca część danych o prostokącie w postaci stringa 
	* @return informacje o figurze
	*/
	String getName() {
		if(a != 0){
			return String.format(" Prostokat (x: %.0f, y: %.0f, boki: %.0f, %.0f, ", x, y, a, b);
		}
		return " ";
	}

	@Override
	/** Metoda zwracająca pole prostokąta */
	double pole() {
		return a*b;
	}

	@Override
	/** Metoda zwracająca obwód prostokąta 
	* @return pole prostokąta
	*/
	double obwod() {
		return 2*a + 2*b;
	}
	
	@Override
	/** Metoda odpowiedzialna za przesuwanie prostokąta */
	void move(double dx, double dy) {
		x += dx;
		y += dy;
	}

	@Override
	/** Metoda odpowiedzialna za skalowanie prostokąta 
	* @param s wartość pokazująca, czy scrolluje w górę czy w doł
	*/
	void scale(int s) {
		if(s == 1){
			a*=0.9;
			b*=0.9;
		}
		else{
			a*=1.1;
			b*=1.1;
		}
	}

	@Override
	/** Metoda odpowiedzialna za rysowanie prostokąta */
	void draw(Graphics g2d) {
        g2d.setColor(wybranyKolor);
        g2d.fillRect((int)(x - a / 2), (int)(y - b / 2), (int)a, (int)b);
	}
}
