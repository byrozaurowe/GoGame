import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import static java.lang.Math.*;
import java.awt.geom.*;
import java.awt.Graphics;
import java.io.*;
/** Klasa główna Main */
public class Main extends JFrame implements ActionListener{
	JMenuBar menuBar;
	JMenu menuPlik, menuNarzedzia, menuPomoc, menuKolory;
	JMenuItem mNowy, mOtworz, mZapisz, mZamknij, mKolo, mProstokat, mWielokat, mInfo, mAutor, mZmienKolor, mresetPol;
	JPopupMenu popup;
	JTextArea textArea;
	static Main okno;
	Graphics g;
	int zaznaczone = 0, liczbapunktow = 0;
	boolean czyKolo = false, czyProstokat = false, czyWielokat = false;
	Color wybranyKolor;
	
	private static final long serialVersionUID = 3727471814914970170L;
	
	// Lista zapisujaca narysowane kol
	ArrayList<Figura> figury = new ArrayList<Figura>();
	
	ArrayList<Figura> tempfigury = new ArrayList<Figura>();
	
	ArrayList<Integer> wspx = new ArrayList<Integer>();
	ArrayList<Integer> wspy = new ArrayList<Integer>();
	
	/** Konstruktor okna */
	public Main(){
		
		Rysownik rysownik = new Rysownik();
		
		//Sprawdzanie wymiarów ekranu
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		
		// Ustawianie szerokości i wysokości okna 
		setSize(screenWidth, screenHeight);
		setLocationByPlatform(true);
		setTitle("PAINT");
		
		// Ustaw pasek menu 
		menuBar = new JMenuBar();
		
		// Utworz zakladke plik
		menuPlik = new JMenu("Plik");
		mNowy = new JMenuItem("Nowy");
		mOtworz = new JMenuItem("Wczytaj");
		mZapisz = new JMenuItem("Zapisz");
		mZamknij = new JMenuItem("Zamknij");
		menuPlik.add(mNowy);
		menuPlik.add(mOtworz);
		menuPlik.add(mZapisz);
		menuPlik.addSeparator();
		menuPlik.add(mZamknij);
		
		// Utworz zakladke narzedzia
		menuNarzedzia = new JMenu("Narzedzia");
		mKolo = new JMenuItem("Kolo");
		mProstokat = new JMenuItem("Prostokat");
		mWielokat = new JMenuItem("Wielokat");
		menuNarzedzia.add(mKolo);
		menuNarzedzia.add(mProstokat);
		menuNarzedzia.add(mWielokat);
		
		// Utworz zakladke pomoc
		menuPomoc = new JMenu("Pomoc");
		mInfo = new JMenuItem("Informacje");
		mAutor = new JMenuItem("O autorze");
		menuPomoc.add(mInfo);
		menuPomoc.add(mAutor);
		
		// Dodawanie zakladek do menu
		setJMenuBar(menuBar);
		menuBar.add(menuPlik);
		menuBar.add(menuNarzedzia);
		menuBar.add(menuPomoc);
		
		// Dodawanie menu kontekstowego
		popup = new JPopupMenu();
		mZmienKolor = new JMenuItem("Zmien Kolor");
		mresetPol = new JMenuItem("Rysuj wielokat od poczatku");
		popup.add(mZmienKolor);
		popup.add(mresetPol);
		rysownik.setComponentPopupMenu(popup);
		
		// Dodawanie akcji w menu
		mNowy.addActionListener(this);
		mOtworz.addActionListener(this);
		mZapisz.addActionListener(this);
		mZamknij.addActionListener(this);
		mKolo.addActionListener(this);
		mProstokat.addActionListener(this);
		mWielokat.addActionListener(this);
		mInfo.addActionListener(this);
		mAutor.addActionListener(this);
		mZmienKolor.addActionListener(this);
		mresetPol.addActionListener(this);
		JPanel panel = new JPanel();
		textArea = new JTextArea();
		add(textArea, BorderLayout.PAGE_START);
		add(rysownik, BorderLayout.CENTER);
		textArea.setEditable(false);
	}
	/** Main 
	* @param args puste
	*/
	public static void main(String[] args){
		 okno = new Main();
		 okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 okno.setVisible(true);
		 okno.setLayout(new BorderLayout());
	}
	
	/** Funkcja reagujaca na wydarzenia */
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();
		
		if(source == mNowy){
			figury.clear();
			wspx.clear();
			wspy.clear();
			tempfigury.clear();
			liczbapunktow = 0;
			textArea.setText("");
			czyKolo = false;
			czyProstokat = false;
			czyWielokat = false;
			mKolo.setEnabled(true);
			mProstokat.setEnabled(true);
			mWielokat.setEnabled(true);
			mresetPol.setEnabled(false);
			repaint();
		}
		
		else if(source == mOtworz){
			load();
		}
		
		else if(source == mZapisz){
			save();
		}
		
		else if(source == mZamknij){
			dispose();
		}
		
		else if(source == mKolo){
			czyKolo = true;
			czyProstokat = false;
			czyWielokat = false;
			mKolo.setEnabled(false);
			mProstokat.setEnabled(true);
			mWielokat.setEnabled(true);
			mresetPol.setEnabled(false);
		}
		
		else if(source == mProstokat){
			czyKolo = false;
			czyProstokat = true;
			czyWielokat = false;
			mKolo.setEnabled(true);
			mProstokat.setEnabled(false);
			mWielokat.setEnabled(true);
			mresetPol.setEnabled(false);
		}
		
		else if(source == mWielokat){
			czyKolo = false;
			czyProstokat = false;
			czyWielokat = true;
			mKolo.setEnabled(true);
			mProstokat.setEnabled(true);
			mWielokat.setEnabled(false);
			mresetPol.setEnabled(true);
		}
		
		else if(source == mresetPol){
			wspx.clear();
			wspy.clear();
			liczbapunktow = 0;
			tempfigury.clear();
		}
		
		else if(source == mInfo){
			JOptionPane.showMessageDialog(null, "Jest to program pozwalajacy uzytkownikowi na rysowanie dowolnego kola, prostokata lub wielokata w roznych kolorach. Za pomoca scrolla mozna powiekszac i pomniejszac narysowane wczesniej figury. Narysowany obrazek mozna zapisac do pliku, a takze go wczytac.", "Informacje", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if(source == mAutor){
			JOptionPane.showMessageDialog(null, "Autor: Wojciech Pakulski", "O autorze", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if(source == mZmienKolor){
			wybranyKolor = JColorChooser.showDialog(null, "Zmiana koloru", Color.BLACK);
			for (Figura f : figury) {
				if (f.czyZaznaczone()){
					f.wybranyKolor = wybranyKolor;
				}
			}
			repaint();
		}
	}
	
	/** Funkcja wczytująca obraz z pliku */
	public void load(){
		String name = JOptionPane.showInputDialog(null, "Nazwa pliku do wczytania:");
		if(name != null){
			try {
				figury.clear();
				FileInputStream fileIn = new FileInputStream(name + ".txt");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				
				int size = in.readInt();
				for(int i = 0; i < size; i++){
					figury.add((Figura) in.readObject());
				}
				
				in.close();
				fileIn.close();
			} 
			catch (FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "Nie znaleziono pliku", "Blad", JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Blad podczas odczytywania strumienia", "Blad", JOptionPane.ERROR_MESSAGE);
			}
			catch (ClassNotFoundException e){
				JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);
			}
			repaint();
		}
	}
	
	/** Funkcja zapisująca obraz do pliku */
	public void save(){
		String name = JOptionPane.showInputDialog(null, "Nazwa pliku:");
		if(name != null){
			try{
				FileOutputStream fileOut = new FileOutputStream(name + ".txt");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				
				out.writeInt(figury.size());
				for(int i = 0; i < figury.size(); i++){
					out.writeObject(figury.get(i));
				}
				
				out.close();
				fileOut.close();
				JOptionPane.showMessageDialog(null, "Zapisano obraz w pliku " + name + ".txt", "Zapisano do pliku", JOptionPane.INFORMATION_MESSAGE);
			} 
			catch (IOException i) {
				JOptionPane.showMessageDialog(null, "Blad podczas odczytywania strumienia", "Blad", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/** Klasa w ktorej odbywa sie rysowanie */
	class Rysownik extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{
		double x1, y1, x2, y2;
		double x, y;
		boolean wybrane = false;
		Kolo kolo;
		Prostokat prostokat;
		Wielokat wielokat;
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		private static final long serialVersionUID = 3727471814914970170L;
		// Ustawianie szerokości i wysokości okna 
		int WIDTH = screenWidth;
		int HEIGHT = screenHeight;
		
		/** Konstruktor rysownika */
		public Rysownik() {
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
			setBackground(Color.WHITE);
		}
		
		@Override
		/** Funkcja odpowiadająca za przenoszenie rzecz myszką */
		public void mouseDragged(MouseEvent e1) {
			okno.textArea.setText("");

			/** Pętla sprawdza czy jakaś figura jest zaznaczona */
			for (Figura f : figury) {
				if (f.czyZaznaczone()){
					wybrane = true;
					break;
				}
				else wybrane = false;
			}
			if(wybrane){
				double var1 = e1.getX() - x1;
				double var2 = e1.getY() - y1;
				for (Figura f : figury) {	
					if (f.czyZaznaczone()){
						f.move(var1, var2);
					}
				}
				repaint();
				x1 = e1.getX();
				y1 = e1.getY();
			}
			else{
				x2 = e1.getX();
				y2 = e1.getY();
				if(czyKolo){
					tempfigury.clear();
					kolo = new Kolo(x1, y1, x2, y2, wybranyKolor);
					tempfigury.add(kolo);
				}
				else if(czyProstokat){
					tempfigury.clear();
					prostokat = new Prostokat(x1, y1, x2, y2, wybranyKolor);
					tempfigury.add(prostokat);
				}
			}
			for (Figura t : tempfigury) {
				okno.textArea.append(t.info());
			}
			for (Figura f : figury) {
				if (f.czyZaznaczone()){
					okno.textArea.append(f.info());
				}
			}
			repaint();
		}
		
		public void mouseMoved(MouseEvent e1) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		
		/** Funkcja nadpisująca co się dzieje, gdy kliknie się przycisk myszy */
		public void mouseClicked(MouseEvent e) {
			okno.textArea.setText("");
			double px = e.getX();
			double py = e.getY();
			for (Figura f : figury) {
				if (f.isInside(px, py)){
					f.zaznacz();
					zaznaczone++;
					okno.textArea.append(f.info());
				}
				else{
					f.odznacz();
					zaznaczone--;
				}
			}
			if(!wybrane){
				okno.textArea.setText("");
			}
			repaint();
			if(czyWielokat) {
				wspx.add(e.getX());
				wspy.add(e.getY());
				liczbapunktow++;
				if(liczbapunktow == 1){
					kolo = new Kolo(e.getX(), e.getY(), 20, wybranyKolor);
					tempfigury.add(kolo);
				}
				if((wspx.get(liczbapunktow - 1) >= (wspx.get(0) - 20) && wspx.get(liczbapunktow - 1) <= (wspx.get(0) + 20))  && (wspy.get(liczbapunktow - 1) >= (wspy.get(0) - 20) && wspy.get(liczbapunktow - 1) <= (wspy.get(0) + 20)) && liczbapunktow >= 3){
					wspx.remove(liczbapunktow - 1);
					wspy.remove(liczbapunktow - 1);
					wielokat = new Wielokat(wspx, wspy, liczbapunktow - 1, wybranyKolor);
					figury.add(wielokat);
					wspx.clear();
					wspy.clear();
					tempfigury.clear();
					liczbapunktow = 0;
					repaint();
				}
			}
		}
		/** Funkcja nadpisująca wydarzenie po wciśnięciu myszy w ekran */
		public void mousePressed(MouseEvent event1){
			x1 = event1.getX();
			y1 = event1.getY();
			for (Figura f : figury) {
				if (f.czyZaznaczone()){
					wybrane = true;
					break;
				}
				else wybrane = false;
			}
			if(wybrane && czyWielokat){
				tempfigury.clear();
				wspx.clear();
				wspy.clear();
				tempfigury.clear();
				liczbapunktow = 0;
				repaint();
			}
		}
		
		/** Funkcja dzialajaca po puszczeniu przycisku myszy */
		public void mouseReleased(MouseEvent event2){
			if(!wybrane){
				if(!czyWielokat) tempfigury.clear();
				double y2temp = event2.getY();
				if(y2temp == y2){
					if(czyKolo){
						kolo = new Kolo(x1, y1, x2, y2, wybranyKolor);
						figury.add(kolo);
					}
					else if(czyProstokat){
						prostokat = new Prostokat(x1, y1, x2, y2, wybranyKolor);
						figury.add(prostokat);
					}
				}
			}
		}
		
		@Override
	    public void mouseWheelMoved(MouseWheelEvent e) {
			okno.textArea.setText("");
			for(Figura f : figury){	
				if(f.czyZaznaczone()){
					f.scale(e.getWheelRotation());
				}
			}
			for (Figura f : figury) {
				if (f.czyZaznaczone()){
					okno.textArea.append(f.info());
				}
			}
			repaint();
	    }
		
		/** Funkcja paintComponent rysująca obiekty */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setBackground(Color.WHITE);
			for (Figura f : figury){
				f.draw(g);
			}
			for(Figura a : tempfigury){
				a.draw(g);
			}
		}
	}
}


