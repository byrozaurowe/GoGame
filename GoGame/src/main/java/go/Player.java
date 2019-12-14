package go;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/** Klasa gracz */
public class Player implements Runnable {
	@Override
	public void run() {

	}
	/** Enum kolor kamienia */
	enum StoneColor {
		WHITE,
		BLACK;
	}

	private Socket socket;
	private Scanner dataIn;
	private PrintWriter dataOut;
	private int captives;
	Player opponent;

	private StoneColor playerStoneColor;

	Player (Socket socket, int numPlayer) throws IOException {
		captives = 0;
		if(numPlayer == 1) {
			playerStoneColor = StoneColor.WHITE;
		}
		else {
			playerStoneColor = StoneColor.BLACK;
		}
		this.socket = socket;
		setUp();
	}

	void setUp () throws IOException {
		dataIn = new Scanner(socket.getInputStream());
		dataOut = new PrintWriter(socket.getOutputStream(), true);
		if (playerStoneColor == StoneColor.WHITE) {
			Board.currentPlayer = this;
			dataOut.println("\nMESSAGE Waiting for opponent to connect");
		} else {
			opponent = Board.currentPlayer;
			opponent.opponent = this;
			opponent.dataOut.println("MESSAGE Your move");
		}

	}

	StoneColor getStoneColor () {
		return playerStoneColor;
	}
}
