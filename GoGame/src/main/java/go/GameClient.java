package go;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/** Klient gry */
public class GameClient implements Runnable{

    Menu menu;
    /** Id gracza */
    private int playerID;
    /** Socket klienta */
    private Socket socket;
    /** Czytanie danych z serwera */
    private BufferedReader dataIn;
    /** Wysylanie danych do serwera */
    private PrintWriter dataOut;
    static GameClient gameClient;
    private boolean isYourTurn;
    Board board;
    static GUI gui;
    String moveMsg;


    /** Konstruktor klienta */
    GameClient () {
        menu = new Menu();
    }

    /** Gra */
    public void run() {
        while (true) {
            String line;
            if (isYourTurn) {
                moveMsg = null;
                while (moveMsg == null);
                dataOut.println(moveMsg);
                try {
                    line = dataIn.readLine();
                    readServerMsg(line);
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            else {
                try {
                    line = dataIn.readLine();
                    readServerMsg(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void readServerMsg (String line) {
        if (line.charAt(0) != playerID)
            isYourTurn = true;
        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 0)
                    board.getBoardTab()[i][j].setVisibility(Stone.Visibility.INVISIBLE);
                else if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 1) {
                    board.getBoardTab()[i][j].setVisibility(Stone.Visibility.VISIBLE);
                    board.getBoardTab()[i][j].setPlayer("WHITE");
                } else if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 2) {
                    board.getBoardTab()[i][j].setVisibility(Stone.Visibility.VISIBLE);
                    board.getBoardTab()[i][j].setPlayer("BLACK");
                }
            }
        }
    }

    public void connectClient() {
        System.out.println("-----Client----");
        try {
            socket = new Socket("localhost", 4444);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new PrintWriter(socket.getOutputStream(), true);
            playerID = Integer.parseInt(dataIn.readLine());
            System.out.println("Connected as player " + playerID);

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(gameClient);
        thread.start();
        board = gui.getBoard();
        if (playerID == 1) {
            isYourTurn = true;
            dataOut.println(board.getSIZE());
        }
        else isYourTurn = false;

    }

    void setMoveMsg (String moveMsg) {
        this.moveMsg = moveMsg;
    }

    void sendCoordinates (String line) {
        String[] coordinates = line.split(".");

    }

    /** Wyslij informacje na serwer
     * @param msg informacja do serwera
     * @return informacja zwrotna od serwera
     */

    void sendPass() {
        if(isYourTurn) {
            dataOut.println("pass");
            System.out.println("Gracz #" + playerID + " spasowal");
        }
        else System.out.println("Nie twoja tura");
    }

    /** Main
     * @param args
     */
    public static void main(String[] args) {
        gameClient = new GameClient();
    }



    /** Zamknij socket */
    private void close () {
        try {
            dataIn.close();
            dataOut.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
