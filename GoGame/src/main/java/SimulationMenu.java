import org.hibernate.Query;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class SimulationMenu extends JFrame implements ActionListener {
    /**Combo box z zapisanymi grami*/
    private JComboBox savedGamesComboBox;
    private JButton startSimulation;

    public SimulationMenu() {
        super("Simulation Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("SELECT date FROM SavedGames");
        List result = query.list();
        String[] wynik = new String[result.size()];
        for(int j = 0; j < result.size(); j++) {
            for (int i = 0; i <= 18; i++) {
                if(i==0)
                    wynik[j] = String.valueOf(result.get(j).toString().charAt(i));
                else wynik[j] += String.valueOf(result.get(j).toString().charAt(i));
            }
        }
        //dodanie historii gier do combo boxa
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 20);

        savedGamesComboBox = new JComboBox(wynik);
        savedGamesComboBox.addActionListener(this);
        savedGamesComboBox.setFont(labelFont);
        ((JLabel)savedGamesComboBox.getRenderer()).setHorizontalAlignment(DefaultListCellRenderer. CENTER);

        startSimulation = new JButton("Start simulation");
        startSimulation.addActionListener(this);
        startSimulation.setFont(labelFont);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.add(Box.createRigidArea(new Dimension(400, 10)));
        menuPanel.add(startSimulation);
        startSimulation.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createRigidArea(new Dimension(200, 10)));
        menuPanel.add(savedGamesComboBox);
        menuPanel.add(Box.createRigidArea(new Dimension(200, 10)));
        add(menuPanel);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startSimulation)  {
            Object date = savedGamesComboBox.getSelectedItem();
            GameClient.gameClient.sendGame(date);
            this.dispose();
        }
    }
}
