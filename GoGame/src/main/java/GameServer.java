import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
    /** Obiekt zajmujacy sie logika */
    GameHandler gameHandler;
    /** Czyja tura- warosci 1/2 */
    private int whoseTurn = 1;
    /** Tablica ze stanem planszy */
    private int[][] stoneLogicTable;
    /** Rozmiar planszy */
    private int boardSize;
    /** Wspolrzedna x i y ruchu */
    private int moveX, moveY;
    /** Czy gra jest skończona */
    private boolean gameIsFinished = true;
    /** Czy jakis gracz opuscil gre? */
    private boolean playerLeft = false;
    /** Liczba jencow gracza nr 1 i 2 */
    int[] captives = new int[]{0, 0};
    /** Licznik spasowan */
    private int passCounter = 0;
    /** Czy z botem? */
    private boolean bot = false;

    /** Konstruktor serwera */
    private GameServer() {
        System.out.println("----Game Server----");
        numPlayers = 0;
        try {
            serverSocket = new ServerSocket(4444);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Wyslij stan planszy do obu graczy */
    private void sendToPlayers () {
        System.out.println("czyja tura po ruchu " + whoseTurn);
        String msg = Integer.toString(whoseTurn);
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                msg = msg + stoneLogicTable[i][j];
            }
        }
        msg += " " + captives[0] + " " + captives[1];
        System.out.println(msg);
        dataOutPlayer1.println(msg);
        dataOutPlayer2.println(msg);
    }

    /** Sluchanie socketa gracza
     * @param dataInPlayer obiekt do nasluchiwania odpowiedniego gracza
     * */
    private void listenPlayer (BufferedReader dataInPlayer) {
        String line;
        try {
            line = dataInPlayer.readLine();
            if (line.equals("pass")) {
                passTurn();
            }
            else {
                makeMoveInformationLine(line);
            }
        } catch (IOException e) {
            System.out.println("Player left the game");
            gameIsFinished = true;
            opponentLeft();
        }
    }

    /** Metoda odpowiadajaca za ruch graczy
     * @param line koordynaty ruchu gracza
     * */
    private void makeMoveInformationLine(String line) {
        String[] coordinates = line.split(" ");
        moveX = Integer.parseInt(coordinates[0]);
        moveY = Integer.parseInt(coordinates[1]);
        System.out.println(moveX +" " + moveY);
        whoseTurn = gameHandler.move(moveX, moveY, whoseTurn, stoneLogicTable);
        System.out.println(whoseTurn);
    }

    /** Metoda wykonujaca sie jak klient spasuje */
    private void passTurn() {
        if (whoseTurn == 1) whoseTurn = 2;
        else whoseTurn = 1;
        passCounter+=2;
    }

    /** Ustawia roziar planszy i inicjalizuje tablice ze stanem planszy */
    private void setBoardSize() throws IOException {
        String line;
        line = dataInPlayer1.readLine(); //informacje o rozmiarze planszy
        try {
            boardSize = Integer.parseInt(String.valueOf(line));
        }
        catch(NumberFormatException e) {
            System.out.println("Podano zly rozmiar planszy - ustawiam domyslny");
            boardSize = 13;
        }
        stoneLogicTable = new int[boardSize][boardSize];
    }

    /** Ustaw tablice ze stanem plaszy
     * @param table stan planszy po stronie serwera
     */
    void setTable(int[][] table) {
        stoneLogicTable = table;
    }

    /** Metoda podlaczająca klientow */
    private void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            connectClient();
            if(bot) {
                new Bot();
            }
            connectClient();
        }
        catch (IOException ex) {
            System.out.println("Blad przy polaczeniu");
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
            if(dataInPlayer1.readLine().equals("BOT")) bot = true;
            setBoardSize();

        }
        else {
            dataInPlayer2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOutPlayer2 = new PrintWriter(socket.getOutputStream(), true);
            dataOutPlayer2.println(numPlayers);
            System.out.println("Player " + numPlayers + " has connected");
            dataOutPlayer2.println(boardSize);
        }
        if (numPlayers == 2) {
            System.out.println("Now we have two players");
            dataOutPlayer1.println("Let's begin");
            dataOutPlayer2.println("Let's begin");
        }
    }

    /** Metoda trwajaca cala gre, ustawia kiedy nasluchiwac, a kiedy wysylac*/
    private void play() {
        gameHandler = new GameHandler(boardSize);
        gameIsFinished = false;
        while (!gameIsFinished) {
            if(whoseTurn == 1)
                listenPlayer(dataInPlayer1);
            else if(whoseTurn == 2) {
                listenPlayer(dataInPlayer2);
            }
            if(passCounter > 0) passCounter--;
            if(passCounter == 2) gameIsFinished = true;
            if(gameIsFinished && playerLeft) break;
            if(gameIsFinished) finishGame();
            sendToPlayers();
        }
    }

    /** Wywolywane gdy gra zakonczy sie poprawnie */
    private void finishGame() {
        String input1;
        String input2;
        passCounter = 0;
        dataOutPlayer1.println("#");
        dataOutPlayer2.println("#");
        try {
            input1 = dataInPlayer1.readLine();
            if(input1.equals("Y")) {
                whoseTurn = 2;
                gameIsFinished = false;
            }
            dataOutPlayer2.println("Do you want to resume?");
            input2 = dataInPlayer2.readLine();
            if(input2.equals("Y")) {
                whoseTurn = 1;
                gameIsFinished = false;
            }
            if(input2.equals("Y") && input1.equals("Y")) {
                whoseTurn = (int) Math.floor(Math.random()+1);
                gameIsFinished = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!gameIsFinished) {
            dataOutPlayer1.println("Game is continuing");
            dataOutPlayer2.println("Game is continuing");
        }
        else {
            dataOutPlayer1.println(":(");
            dataOutPlayer2.println(":(");
        }
    }

    /** Wywolywane gdy gracz opusci gre */
    private void opponentLeft() {
        if(whoseTurn == 1) {
            dataOutPlayer2.println("Opponent left");
            playerLeft = true;
        }
        else {
            dataOutPlayer1.println("Opponent left");
            playerLeft = true;
        }
    }
    /** Main
     * @param args pusto
     * */
    public static void main(String[] args) {
        gameServer = new GameServer();
        gameServer.acceptConnections();
        gameServer.play();
    }

}
