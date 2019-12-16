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
    static GameClient gameClient;
    public boolean isYourTurn;
    private Board board;
    static GUI gui;
    private String moveMsg;
    boolean gameIsFinished = false;

    /** Konstruktor klienta */
    private GameClient() {
        menu = new Menu();
    }

    /** Gra */
    public void run() {
        while (!gameIsFinished) {
            String line;
            if (isYourTurn) {
                moveMsg = null;
                while (moveMsg == null) {
                    if(isYourTurn)
                        moveMsg = gui.getMsg(); // Oczekiwanie, aż gracz zrobi jakiś ruch

                }
                System.out.println(moveMsg);
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
        gui.showSummary();
    }

    /**
     * @return liczba jencow gracza
     */
    int getCaptives() {
        return 2; // do zmiany
    }

    void sendSummaryResponse(String response) {
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
        if (line.charAt(0)- 48 == playerID ) {
            isYourTurn = true;
            gui.nullMsg();
        }
        else isYourTurn = false;
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
        gui.repaint();
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

    /** Podlacza klienta do serwera */
    void connectClient() {
        System.out.println("-----Client----");
        try {
            socket = new Socket("localhost", 4444);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new PrintWriter(socket.getOutputStream(), true);
            playerID = Integer.parseInt(dataIn.readLine());
            System.out.println("Connected as player " + playerID);
            gui.setTitle("Gracz #" + playerID);
            gui.setPlayerID(playerID);

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        board = gui.getBoard();
        if (playerID == 1) {
            isYourTurn = true;
            dataOut.println(board.getSIZE());
        }
        else {
            isYourTurn = false;
        }
        Thread thread = new Thread(gameClient);
        thread.start();
    }

    /** Main
     * @param args puste
     */
    public static void main(String[] args) {
        gameClient = new GameClient();
    }

    /** Zamyka socket */
     void close () {
        try {
            dataIn.close();
            dataOut.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
