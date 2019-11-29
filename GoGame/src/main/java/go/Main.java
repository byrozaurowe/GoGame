package go;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {


	Main() {
		super("Go Game");
		Board board = new Board();
	}

	public static void main(String[] args) {
		Main okno = new Main();
		okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	public void actionPerformed(ActionEvent actionEvent) {
		ActionEvent event = actionEvent;
		//if(event ==)
	}
}
