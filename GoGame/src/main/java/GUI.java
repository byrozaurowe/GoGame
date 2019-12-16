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

    /** Konstruktor maina
     * @param boardSize rozmiar planszy
     * */
    GUI(int boardSize) {
        gameInitialize(boardSize);
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

    void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /** Metoda actionPerformed */
    public void actionPerformed(ActionEvent actionEvent) {
        Object event = actionEvent.getSource();
        if (event == exitItem) {
            //doYouWantToEnd();
            System.exit(0);
        } else if (event == authorsItem) {
            JOptionPane.showMessageDialog(null, "Authors: Wiktoria Byra, Wojciech Pakulski", "Authors", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(event == passItem) {
            msg = "pass";
            //GameClient.gameClient.setMoveMsg("pass");
        }
    }

    /** Tworzy okno gry */
    private void gameInitialize(int boardSize) {
        // tworzenie menu
        menuBar = new JMenuBar();
        optionsMenu = new JMenu("Options");
        passItem = new JMenuItem("Pass");
        newGameItem = new JMenuItem("Menu");
        exitItem = new JMenuItem("Exit");
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
        board = new Board(boardSize);
        add(board, BorderLayout.CENTER);

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

    int doYouWantToEnd() {
        int input = JOptionPane.showConfirmDialog(this,
                "You have " + GameClient.gameClient.getCaptives() + " captives \n"
                        + "Do you want to resume the game?", "Summary",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (input == -1) {
            doYouWantToEnd();
            return -1;
        }
        return input;
    }

    void waitForOpponent() {
        JOptionPane.showMessageDialog(this, "You have " + GameClient.gameClient.getCaptives() + " captives \n" +
                "Waiting for opponent", "Results - waiting...", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Zwroc obiekt plansza
     * @return plansza
     * */
    Board getBoard () {
        return board;
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
}
