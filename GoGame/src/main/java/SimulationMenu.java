import org.hibernate.Query;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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

        //dodanie historii gier do combo boxa
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 20);

        savedGamesComboBox = new JComboBox(result.toArray());
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
            Object date =  savedGamesComboBox.getSelectedItem();
            GameClient.gameClient.sendGame(date);
        }
    }
}
