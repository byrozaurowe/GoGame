import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/** Klasa obslugujaca menu glowne */
class Menu extends JFrame implements ActionListener {

    GUI frame;
    /** Guzik rozpoczynajacy gre z botem */
    private JButton botStartButton;
    /** Guzik rozpoczynajacy gre z graczem */
    private JButton playerStartButton;

    private JButton joinGameButton;
    /** Panele do menu */
    private JPanel menuPanel, titlePanel, descPanel, radioPanel, playerPanel, botPanel;
    /** Etykieta z tytulem gry */
    private JLabel titleLabel;
    /** Etykieta z opisem */
    private JLabel descLabel;
    /** Grupa radio buttonow */
    private ButtonGroup buttonGroup;
    /** Radio button plansza 19x19 */
    private JRadioButton big;
    /** Radio button plansza 13x13 */
    private JRadioButton normal;
    /** Radio button plansza 9x9 */
    private JRadioButton small;

    /** Konstruktor menu */
    Menu(){
        super("Go Menu");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        // ustawianie nazw buttonow i labeli
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 20);
        botStartButton = new JButton("Start game against a computer");
        botStartButton.setFont(labelFont);
        playerStartButton = new JButton("Start game against player");
        playerStartButton.setFont(labelFont);
        titleLabel = new JLabel("Go Game");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        descLabel = new JLabel("To play, select the board size and mode");
        descLabel.setFont(labelFont);
        // ustawienie panelu glownego
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // ustawienie radio buttonow
        buttonGroup = new ButtonGroup();
        big = new JRadioButton("19x19");
        normal = new JRadioButton("13x13");
        small = new JRadioButton("9x9");
        Font radioFont = new Font("Segoe UI", Font.PLAIN, 16);
        radioPanel = new JPanel();
        buttonGroup.add(big);
        radioPanel.add(big);
        big.setFont(radioFont);
        buttonGroup.add(normal);
        radioPanel.add(normal);
        normal.setFont(radioFont);
        buttonGroup.add(small);
        radioPanel.add(small);
        small.setFont(radioFont);
        small.setSelected(true);

        // dodawanie elementow do ich paneli
        titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        descPanel = new JPanel();
        descPanel.add(descLabel);
        playerPanel = new JPanel();
        playerPanel.add(playerStartButton);
        botPanel = new JPanel();
        botPanel.add(botStartButton);
        // dodawanie paneli do panelu glownego
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(titlePanel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(descPanel);
        menuPanel.add(radioPanel);
        menuPanel.add(playerPanel);
        menuPanel.add(botPanel);
        add(menuPanel);
        botStartButton.addActionListener(this);
        playerStartButton.addActionListener(this);
        pack();
    }
    /** Metoda actionPerformed */
    public void actionPerformed(ActionEvent actionEvent) {
        Object event = actionEvent.getSource();
        if(event == playerStartButton) {
            if (big.isSelected()) {
                frame = new GUI(19);
                GameClient.gui = frame;
                GameClient.gameClient.connectClient();
                this.dispose();
            }
            else if (normal.isSelected()) {
                frame = new GUI(13);
                GameClient.gui = frame;
                GameClient.gameClient.connectClient();
                this.dispose();
            }
            else if (small.isSelected()){
                frame = new GUI(9);
                GameClient.gui = frame;
                GameClient.gameClient.connectClient();
                this.dispose();
            }
        }
    }
}
