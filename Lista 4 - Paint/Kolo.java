import java.awt.*;
import static java.lang.Math.*;

/** Klasa koło dziedzicząca po figurze */
public class Kolo extends Figura {
    double r;
	double x, y, x1, x2, y1, y2;
	
	/** Konstruktor koła 
	* @param x1 początkowa współrzędna iksowa
	* @param y1 początkowa współrzędna igrekowa
	* @param x2 końcowa współrzędna iksowa
	* @param y2 końcowa współrzędna igrekowa
	* @param wybranyKolor zmienna określająca kolor RGB
	*/
    Kolo(double x1, double y1, double x2, double y2, Color wybranyKolor) {
        this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.wybranyKolor = wybranyKolor;
		double var1 = (x2 - x1) * (x2 - x1);
		double var2 = (y2 - y1) * (y2 - y1) ;
		r = Math.sqrt(var1 + var2) / 2;
		var1 = (x1 + x2) / 2;
		var2 = (y1 + y2) / 2;
		x = var1;
		y = var2;
    }

	/** Konstruktor koła zaznaczającego początek wielokąta
	* @param x współrzędna iksowa środka koła
	* @param y współrzędna igrekowa środka koła
	* @param r promień koła
	* @param wybranyKolor zmienna określająca kolor RGB
	*/
	Kolo(double x, double y, double r, Color wybranyKolor){
		this.x = x;
		this.y = y;
		this.r = r;
		this.wybranyKolor = wybranyKolor;
	}
	
	/** Metoda odpowiedzialna za sprawdzanie, czy mysz jest wewnątrz koła 
	* @param px aktualne współrzędne iksowe myszy
	* @param py aktualne współrzędne igrekowe myszy
	* @return czy myszka jest w środku figury, czy nie
	*/
   public boolean isInside(double px, double py) {
       return (Math.sqrt((x - px) * (x - px) + (y - py) * (y - py)) <= r);
   }
   
   
    @Override
	/** Metoda wypisująca część danych o kole w postaci stringa 
	* @return informacje o figurze
	*/
    String getName() {
		if(r != 0){
			return String.format(" Kolo (x: %.0f, y: %.0f, r: %.0f, ", x, y, r);
		}
		return " ";
   }

    @Override
	/** Metoda zwracająca pole koła 
	* @return pole koła
	*/
    double pole() {
        return (double) Math.PI * r * r;
    }

	/** Metoda zwracająca obwód koła 
	* @return obwód koła
	*/
    @Override
    double obwod() {
        return (double) Math.PI * r * 2;
    }

    @Override
	/** Metoda skalująca koło
	* @param s wartość pokazująca, czy scrolluje w górę czy w doł
	*/
    void scale(int s) {
		if(s == 1){
			r *= 0.9;
		}
		else{
			r *= 1.1;
		}
    }
	
	@Override
	/** Metoda odpowiedzialna za przesuwanie koła */
	void move(double dx, double dy) {
        x += dx;
        y += dy;
    }
	
    @Override
	/** Metoda odpowiedzialna za rysowanie koła */
    void draw(Graphics g2d) {
        g2d.setColor(wybranyKolor);
        g2d.fillOval((int)(x - r),(int) (y - r), (int)(2 * r), (int)(2 * r));
    }
}
