import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/** Klient gry */
public class GameClient implements Runnable {

    private Menu menu;
    /** Id gracza */
    private int playerID;
    /** Socket klienta */
    private Socket socket;
    /** Czytanie danych z serwera */
    private BufferedReader dataIn;
    /** Wysylanie danych do serwera */
    private PrintWriter dataOut;
    /** Klient gracza */
    static GameClient gameClient;
    /** Czy jest twoja tura */
    boolean isYourTurn = false;
    /** Gui gracza */
    static GUI gui;
    /** Wiadomosc o ruchu */
    private String moveMsg;
    /** Czy gra jest skonczona */
    boolean gameIsFinished = true;
    /** Rozmiar planszy */
    private int boardSize;
    /** Czy z botem? */
    private boolean bot = false;

    /** Konstruktor klienta */
    GameClient() {
        menu = new Menu();
    }

    /** Gra */
    public void run() {
        gui.setGameStatusLabel("Waiting for opponent to join");
        try {
            if(dataIn.readLine().equals("Let's begin")) {
                gameIsFinished = false;
            }
        } catch (IOException e) {
            gui.setGameStatusLabel("Oops! Server has been disconnected");
            System.out.println("Oops! Server has been disconnected");
            gameIsFinished = true;
            return;
        }
        while (!gameIsFinished) {
            String line;
            gui.setGameStatusLabel(isYourTurn);
            if (isYourTurn) {
                moveMsg = null;
                while (moveMsg == null) {
                    if(isYourTurn)
                        moveMsg = gui.getMsg(); // Oczekiwanie, aż gracz zrobi jakiś ruch
                }
                System.out.println(moveMsg);
                dataOut.println(moveMsg);
                if(gameIsFinished) break;
                try {
                    line = dataIn.readLine();
                    readServerMsg(line);
                } catch(IOException e){
                    System.out.println("Oops! Server has been disconnected");
                    gui.setGameStatusLabel("Oops! Server has been disconnected");
                    gameIsFinished = true;
                    gui.showSummary();
                    return;
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
        gui.setGameStatusLabel("Game has ended");
        gui.showSummary();
    }

    /** Wyslij odpowiedz do serwera
     * @param response Y / N
     */
    private void sendSummaryResponse(String response) {
        dataOut.println(response);
    }
    /** Odczytuje informajce z serwera
     * @param line informacja z serwera
     * */
    private void readServerMsg (String line) {
        if((Character.toString(line.charAt(0)).equals("#"))) {
            if(playerID == 1)
                doWeEnd();
            if(playerID == 2)
                waitForOpponent();
            if(!gameIsFinished) {
                try {
                    line = dataIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else return;
        }
        if(line.equals("Opponent left")) {
            gui.opponentLeft();
            gameIsFinished = true;
            return;
        }
        if (line.charAt(0)- 48 == playerID ) {
            isYourTurn = true;
            gui.nullMsg();
        }
        else isYourTurn = false;
        gui.setBoard(line);
    }

    /** Czekaj na przeciwnika az podejmie decyzje czy chce wznowic gre */
    private void waitForOpponent() {
        gui.waitForOpponent();
        try {
            if(dataIn.readLine().equals("Do you want to resume?")) doWeEnd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Sprawdzamy czy gracz chce skonczyc gre */
    private void doWeEnd() {
        gameIsFinished = true;
        switch (gui.doYouWantToEnd()) {
            case 0:
                sendSummaryResponse("Y");
                break;
            case 1:
                sendSummaryResponse("N");
                break;
            case -1:
                doWeEnd();
                return;
        }
        try {
            if(dataIn.readLine().equals("Game is continuing")) gameIsFinished = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int getBoardSize() {
        return boardSize;
    }

    /** Podlacza klienta do serwera */
    int connectClient() {
        System.out.println("-----Client----");
        try {
            socket = new Socket("localhost", 4444);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new PrintWriter(socket.getOutputStream(), true);
            playerID = Integer.parseInt(dataIn.readLine());
            System.out.println("Connected as player " + playerID);
            if(playerID == 1) {
                if (bot) dataOut.println("BOT");
                else dataOut.println("NIEBOT");
            }
            if(playerID == 2) {
                boardSize = Integer.parseInt(dataIn.readLine());
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            return 0;
        } catch (IOException e) {
            System.out.println("Server you are trying to connect is disconnected");
            return 0;
        }
        return playerID;
    }

    /** Ustawienia poczatkowe */
    void setSettings(int boardSize) {
        gui.setTitle("Gracz #" + playerID);
        gui.setPlayerID(playerID);
        if (playerID == 1) {
            isYourTurn = true;
            dataOut.println(boardSize);
        }
        else {
            isYourTurn = false;
        }
        Thread thread = new Thread(gameClient);
        thread.start();
    }

    void startGameWithBot() {
        bot = true;
    }

    /** Main
     * @param args puste
     */
    public static void main(String[] args) {
        gameClient = new GameClient();
    }

}
