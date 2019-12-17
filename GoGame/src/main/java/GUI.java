import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Głowna klasa gracza */
public class GUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
    /** Pasek menu */
    private JMenuBar menuBar;
    /** Opcje na pasku menu */
    private JMenu optionsMenu;
    /** Info na pasku menu */
    private JMenu infoMenu;
    /** Opcja nowa gra */
    private JMenuItem newGameItem;
    /** Opcja wyjdz z gry */
    private JMenuItem exitItem;
    /** Opcja spasuj kolejke */
    private JMenuItem passItem;
    /** Informacja o zasadach */
    private JMenuItem rulesItem;
    /** Informacja o autorach */
    private JMenuItem authorsItem;
    /** Plansza */
    private static Board board;
    /** Wiadomosc do serwera */
    private String msg = null;
    /** Id gracza */
    private int playerID;
    /** Etykieta wyswietlajaca stan gry */
    private JLabel gameStatusLabel;
    /** Twoi jency */
    private int captives;

    /** Konstruktor maina
     * @param boardSize rozmiar planszy
     * */
    GUI(int boardSize) {
        gameInitialize(boardSize);
    }

    /** Metoda actionPerformed */
    public void actionPerformed(ActionEvent actionEvent) {
        Object event = actionEvent.getSource();
        if (event == exitItem) {
            System.exit(0);
        } else if (event == authorsItem) {
            JOptionPane.showMessageDialog(null, "Authors: Wiktoria Byra, Wojciech Pakulski", "Authors", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(event == passItem) {
            msg = "pass";
        }
    }

    /** Tworzy okno gry */
    private void gameInitialize(int boardSize) {
        // tworzenie menu
        menuBar = new JMenuBar();
        optionsMenu = new JMenu("Options");
        passItem = new JMenuItem("Pass");
        newGameItem = new JMenuItem("Menu");
        exitItem = new JMenuItem("Surrender/Exit");
        // dodawanie elementow do menu
        optionsMenu.add(passItem);
        optionsMenu.add(newGameItem);
        optionsMenu.add(exitItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
        // menu info z opcjami zasady i autorzy
        infoMenu = new JMenu("Info");
        rulesItem = new JMenuItem("Rules");
        authorsItem = new JMenuItem("Authors");
        infoMenu.add(rulesItem);
        infoMenu.add(authorsItem);
        menuBar.add(infoMenu);
        board = new Board(this, boardSize);
        add(board, BorderLayout.CENTER);


        gameStatusLabel = new JLabel("Test");
        gameStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        board.add(gameStatusLabel, BorderLayout.SOUTH);


        // action Listenery
        passItem.addActionListener(this);
        newGameItem.addActionListener(this);
        exitItem.addActionListener(this);
        rulesItem.addActionListener(this);
        authorsItem.addActionListener(this);

        //mouse listener
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        pack();
    }

    /** Pokaz podsumowanie jencow na koncu gry */
    void showSummary() {
        JOptionPane.showMessageDialog(this, "You have " + captives + " captives \n",
                "Results", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Zapytanie czy gracz chce wznowic gre
     * @return tak lub nie
     * */
    int doYouWantToEnd() {
        return JOptionPane.showConfirmDialog(this,
                "You have " + captives + " captives \n"
                        + "Do you want to resume the game?", "Is this the end?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    /** Ustawia wyglad planszy analizujac wiadomosc od serwera
     * @param line wiadomosc o stanie gry od serwera
     * */
    void setBoard(String line) {
        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 0)
                    board.getBoardTab()[i][j].setVisibility(Visibility.INVISIBLE);
                else if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 1) {
                    board.getBoardTab()[i][j].setVisibility(Visibility.VISIBLE);
                    board.getBoardTab()[i][j].setPlayer("BLACK");
                } else if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 2) {
                    board.getBoardTab()[i][j].setVisibility(Visibility.VISIBLE);
                    board.getBoardTab()[i][j].setPlayer("WHITE");
                }
            }
        }
        String[] dane = line.split(" ");
        if(playerID == 1)
            captives = Integer.parseInt(dane[1]);
        else captives = Integer.parseInt(dane[2]);
        repaint();
    }

    /** Okno mowiace ze przeciwnik podejmuje decyzje */
    void waitForOpponent() {
        JOptionPane.showMessageDialog(this, "You have " + captives + " captives \n" +
                "Waiting for opponent", "Results - waiting...", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Okno mowiace ze przeciwnik podejmuje decyzje */
    void opponentLeft() {
        JOptionPane.showMessageDialog(this, "You win! Opponent left or surrendered", "You win!", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Ustaw tabele statusu gry
     * @param isYourTurn czy jest twoja tura
     */
    void setGameStatusLabel(boolean isYourTurn) {
        if(isYourTurn) {
            gameStatusLabel.setText("It's your turn");
        }
        else gameStatusLabel.setText("It's your opponent's turn");
    }

    /** Ustaw Stringa statusu gry
     * @param text tekst do labela
     */
    void setGameStatusLabel(String text) {
        gameStatusLabel.setText(text);
    }

    /** Sprawdza czy jest twoja tura
     * @return czy jest twoja tura
     */
    boolean checkIfIsYourTurn() {
        return GameClient.gameClient.isYourTurn;
    }

    /** Sprawdza czy gra juz sie skonczyla
     * @return czy gra sie skonczyla
     * */
    boolean checkIfGameIsFinished() {
        return GameClient.gameClient.gameIsFinished;
    }

    /** Zwroc msg
     * @return wiadomosc do serwera
     */
    String getMsg() {
        return msg;
    }

    /** Ustaw msg na null */
    void nullMsg() {
        msg = null;
    }

    /** Ustaw id gracza */
    void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //board.clickedOnStone(mouseEvent.getX(), mouseEvent.getY());
    }
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
       board.enteredStone(mouseEvent);
    }
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        msg = board.releasedStone();
    }
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        board.draggedStone(mouseEvent);
    }
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {}

    /** Ustaw msg (do testow) */
    void setMsg(String msg) { this.msg = msg;}

    /** Zwroc id gracza (do testow)
     * @return id gracza
     * */
    int getPlayerID() { return playerID; }

    /** Zwroc co wypisuje etykieta statusu (do testow)
     * @return tekst na etykiecie statusu
     * */
    String getLabelText() {
        return gameStatusLabel.getText();
    }
}
