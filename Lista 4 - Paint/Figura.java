import java.awt.*;
import static java.lang.Math.*;
import java.io.*;
/** Abstrakcyjna klasa Figura */
public abstract class Figura implements Serializable {

    private boolean zaznaczone = false;
	Color wybranyKolor;
	
	/** Funkcja sprawdzająca czy figura jest zaznaczona 
	* @return zmienna, która informuje czy obiekt jest zaznaczony
	*/
    public boolean czyZaznaczone() {
        return zaznaczone;
    }
	
	/** Funkcja zaznaczająca figurę */
    public void zaznacz() {
        zaznaczone = true;
    }
	
	/** Funkcja odznaczająca figurę */
    public void odznacz() {
        zaznaczone = false;
    }
	
	/** Metoda abstrakcyjna sprawdzająca czy mysz jest wewnątrz jakiejś figury 
	* @param px pozycja myszy na osi x
	* @param py pozycja myszy na osi y
	* @return czy mysz jest w jakiejś figurze
	*/
    public abstract boolean isInside(double px, double py);

	/** Funkcja wypisująca dane figury 
	*@return informacje o figurze
	*/
	protected String info() {
		if(pole() != 0){
			String s = String.format(getName() + "pole: %.0f  obwod: %.0f) | ", pole(), obwod());
			return s;
		}
		return " ";
    }
    
	/** Metoda abstrakcyjna odpowiedzialna za pobieranie części informacji o figurze 
	* @return współrzędne figury i rozmiar
	*/
    abstract String getName();

	/** Abstrakcyjna metoda licząca pole figury 
	* @return pole pole figury
	*/
    abstract double pole();

	/** Abstrakcyjna metoda licząca obwód figury 
	* @return obwod obwód figury
	*/
	abstract double obwod();

	/** Abstrakcyjna metoda odpowiedzialna za przesuwanie figury
	* @param dx liczba pikseli o ile przesunięto figurę po osi x
	* @param dy liczba pikseli o ile przesunięto figurę po osi y
	*/
	abstract void move(double dx, double dy);

	/** Abstrakcyjna metoda odpowiedzialna za skalowanie figury
	* @param s wartość o jaką figura będzie zeskalowana
	*/
	abstract void scale(int s);

	/** Abstrakcyjna metoda odpowiedzialna za rysowanie figury 
	* @param g2d obiekt/figura
	*/
    abstract void draw(Graphics g2d);
}
