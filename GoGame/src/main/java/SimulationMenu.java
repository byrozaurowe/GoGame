import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class SimulationMenu extends JFrame implements ActionListener {
    /**Combo box z zapisanymi grami*/
    private JComboBox savedGamesComboBox;

    public SimulationMenu() {
        super("Simulation Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        //dodanie historii gier do combo boxa
       Vector<Date> dateList = new Vector<>();
        savedGamesComboBox = new JComboBox(dateList);
        savedGamesComboBox.addActionListener(this);
        add(savedGamesComboBox);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Date date = (Date) savedGamesComboBox.getSelectedItem();
    }
}
