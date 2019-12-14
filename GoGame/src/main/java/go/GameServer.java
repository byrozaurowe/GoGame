package go;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/** Serwer gry */
public class GameServer {
    static GameServer gameServer;
    /** Socket serwera */
    private ServerSocket serverSocket;
    /** Licznik podlaczonych graczy */
    private int numPlayers;
    /** Przyjmowanie info od gracza 1 */
    private BufferedReader dataInPlayer1;
    /** Wysylanie info do gracza 1 */
    private PrintWriter dataOutPlayer1;
    /** Przyjmowanie info od gracza 2 */
    private BufferedReader dataInPlayer2;
    /** Wysylanie info do gracza 2 */
    private PrintWriter dataOutPlayer2;

    GameHandler gameHandler;

    int whoseTurn;
    int[][] stoneLogicTable;
    int boardSize;
    int moveX, moveY;
    boolean gameIsFinished = true;

    /** Konstruktor serwera */
    public GameServer() {
        System.out.println("----Game Server----");
        numPlayers = 0;
        try {
            serverSocket = new ServerSocket(4444);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Wyslij wiadomosc do obu graczy
     * @param msg sygnal do klienta
     * */
    private void sendToPlayers () {
        String msg = Integer.toString(whoseTurn);
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                msg = msg + stoneLogicTable[i][j];
            }
        }
        dataOutPlayer1.println(msg);
        dataOutPlayer2.println(msg);
    }

    /** Sluchanie socketa gracza 1 */
    private void listenPlayer (BufferedReader dataInPlayer) {
        String line = "";
        while (line != null) {
            try {
                line = dataInPlayer.readLine();
                makeTurnInformationLine(line);
                line = dataInPlayer.readLine();
                if (line == "pass") {
                    passTurn();
                }
                else {
                    makeMoveInformationLine(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeTurnInformationLine(String line) {
        whoseTurn = Character.getNumericValue(line.charAt(0));
        for (int i =0; i <boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                stoneLogicTable[i][j] = Character.getNumericValue(line.charAt((i*boardSize)+j+1));
            }
        }
    }
    private void makeMoveInformationLine(String line) {
        String[] coordinates = line.split(".");
        moveX = Integer.parseInt(String.valueOf(coordinates[0]));
        moveY = Integer.parseInt(String.valueOf(coordinates[1]));
        whoseTurn = gameHandler.move(moveX, moveY, whoseTurn, stoneLogicTable);
    }

    private void passTurn() {
        if (whoseTurn == 1) whoseTurn = 2;
        else whoseTurn = 1;
    }

    private void setBoardSize() throws IOException {
        String line;
        line = dataInPlayer1.readLine(); //informacje o rozmiarze planszy
        boardSize = Integer.parseInt(String.valueOf(line));
        stoneLogicTable = new int[boardSize][boardSize];
    }

    public void setTable(int[][] table) {
        stoneLogicTable = table;
    }

    /** Metoda podlaczająca klientow */
    private void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            connectClient();
            connectClient();
            //sendToPlayers("mam info");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /** Ustaw sockety klientow */
    private void connectClient() throws IOException {
        Socket socket = serverSocket.accept();
        numPlayers++;
        if(numPlayers == 1) {
            dataInPlayer1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOutPlayer1 = new PrintWriter(socket.getOutputStream(), true);
            dataOutPlayer1.println(numPlayers);
            System.out.println("Player " + numPlayers + " has connected");

            setBoardSize();
        }
        else {
            dataInPlayer2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOutPlayer2 = new PrintWriter(socket.getOutputStream(), true);
            dataOutPlayer2.println(numPlayers);
            System.out.println("Player " + numPlayers + " has connected");
        }
        if (numPlayers == 2) System.out.println("Now we have two players");
    }

    private void play() {
        gameHandler = new GameHandler(boardSize);
        gameIsFinished = false;
        while (gameIsFinished == false) {
            listenPlayer(dataInPlayer1);
            sendToPlayers();
            listenPlayer(dataInPlayer2);
            sendToPlayers();
        }
    }
    /** Main
     * @param args
     * */
    public static void main(String[] args) {
        gameServer = new GameServer();
        gameServer.acceptConnections();
        gameServer.play();
    }
}
