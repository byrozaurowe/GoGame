package go;

import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

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

    /** Konstruktor klienta */
    GameClient () {
        menu = new Menu();
    }

    /** Gra */
    public void run () {
        while (true) {
            try {
                System.out.println(dataIn.readLine());
            } catch (IOException e) {
                System.out.println("Blad");
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
            if (playerID == 1) {
                isYourTurn = true;
            }
            else isYourTurn = false;

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(gameClient);
        thread.start();
    }


    /** Wyslij informacje na serwer
     * @param msg informacja do serwera
     * @return informacja zwrotna od serwera
     */
    private String sendMessage(String msg) {
        dataOut.println(msg);
        String resp = null;
        try {
            resp = dataIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
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
