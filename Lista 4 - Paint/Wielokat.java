import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.awt.Graphics;
import static java.lang.Math.*;
import java.awt.*;

/** Klasa wielokąt dziedzicząca po figurze */
public class Wielokat extends Figura {
	
	ArrayList<Double> boki = new ArrayList<Double>();
	boolean closed;
	double x, y, dl;
	int[] iksy;
	int[] igreki;
	int liczbapunktow;
	
	/** Konstruktor wielokąta
	* @param wspx lista zawierająca współrzędne iksowe kątów wielokąta
	* @param wspy lista zawierająca współrzędne igrekowe kątów wielokąta
	* @param liczbapunktow liczba kątów wielokąta
	* @param wybranyKolor wielokąta
	*/
	public Wielokat(ArrayList<Integer> wspx, ArrayList<Integer> wspy, int liczbapunktow, Color wybranyKolor) {
		this.wybranyKolor = wybranyKolor;
		this.liczbapunktow = liczbapunktow;
		iksy = new int[liczbapunktow];
		igreki = new int[liczbapunktow];
		
		for(int i = 0; i < liczbapunktow; i++){
			iksy[i] = wspx.get(i);
			igreki[i] = wspy.get(i);
		}
		calculate();
	}

	@Override
	/** Metoda sprawdzajaca, czy mysz znajduje się wewnątrz wielokąta 
	* @param dx współrzędna x położenia myszy
	* @param dy współrzędna y położenia myszy
	* @return czy mysz znajduje się wewnątrz
	*/
	public boolean isInside(double dx, double dy) {
		return sqrt(Math.pow(x - dx, 2) + Math.pow(y - dy, 2)) <= dl;
	}
	
	/** Metoda przeliczająca środek wielokata */
	void calculate(){
		int sumax = 0, sumay = 0;
		for(int i = 0; i < liczbapunktow; i++){
			sumax += iksy[i];
			sumay += igreki[i];
		}
		boki.clear();
		double bok;
		for(int i = 0; i < liczbapunktow; i++){
				bok = sqrt(Math.pow(iksy[i] - iksy[(i+1)%liczbapunktow], 2) + Math.pow(igreki[i] - igreki[(i+1)%liczbapunktow], 2));
				boki.add(bok);
		}
		x  = sumax / liczbapunktow;
		y = sumay / liczbapunktow;
		dl = sqrt(Math.pow(x - iksy[0], 2) + Math.pow(y - igreki[0], 2));
	}
	
	@Override
	/** Metoda wypisująca część danych o wielokącie w postaci stringa 
	* @return informacje o figurze
	*/
	String getName() {
		if(boki.get(0) != 0){
			return String.format(" Wielokat (x: %.0f, y: %.0f, ", x, y);
		}
		return " ";
	}

	@Override
	/** Metoda zwracająca pole wielokąta */
	double pole() {
		double pole = 0;
		for(int i=1; i <= liczbapunktow; i++){
			pole += iksy[i%liczbapunktow] * (igreki[(i+1)%liczbapunktow] - igreki[(i-1)%liczbapunktow]);
		}
		pole = abs(pole) * 0.5;
		return pole;
	}

	@Override
	/** Metoda zwracająca obwód wielokąta 
	* @return obwod wielokąta
	*/
	double obwod() {
		double obwod = 0;
		for(int i = 0; i < boki.size(); i++){
			obwod += boki.get(i);
		}
		return obwod;
	}
	
	@Override
	/** Metoda odpowiedzialna za przesuwanie wielokąta */
	void move(double dx, double dy) {
		for(int i = 0; i < iksy.length; i++){
			iksy[i] += dx;
			igreki[i] += dy;
		}
		calculate();
	}

	@Override
	/** Metoda odpowiedzialna za skalowanie wielokąta 
	* @param s wartość pokazująca, czy scrolluje w górę czy w doł
	*/
	void scale(int s) {
		double k;
		for(int i = 0; i < iksy.length; i++){
			if(s == -1){
				k = 1.1;
				iksy[i] = (int) ((1 - k) * x + k * iksy[i]);
				igreki[i] = (int) ((1 - k) * y + k * igreki[i]);
			}
			else{
				k = 0.9;
				iksy[i] = (int) ((1 - k) * x + k * iksy[i]);
				igreki[i] = (int) ((1 - k) * y + k * igreki[i]);
			}
		}
		calculate();
	}

	@Override
	/** Metoda odpowiedzialna za rysowanie wielokąta */
	void draw(Graphics g2d) {
        g2d.setColor(wybranyKolor);
        g2d.fillPolygon(iksy, igreki, liczbapunktow);
	}
}