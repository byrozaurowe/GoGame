package go;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/** Serwer gry */
public class GameServer {
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
    private void sendToPlayers (String msg) {
        dataOutPlayer1.println(msg);
        dataOutPlayer2.println(msg);
    }

    /** Sluchanie socketa gracza 1 */
    private void listenPlayer1 () {
        String line = "";
        while (line != null) {
            try {
                line = dataInPlayer1.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        }
        else {
            dataInPlayer2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOutPlayer2 = new PrintWriter(socket.getOutputStream(), true);
            dataOutPlayer2.println(numPlayers);
            System.out.println("Player " + numPlayers + " has connected");
        }
        if (numPlayers == 2) System.out.println("Now we have two players");
    }
    /** Main
     * @param args
     * */
    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.acceptConnections();
        gameServer.listenPlayer1();

    }
}
