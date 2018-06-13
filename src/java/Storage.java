import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Storage extends JFrame{
    private JFrame frame;
    private JPanel rootPanel;
    private JTable table;
    private JButton loadStorage;
    private JButton pridatPolozkuButton;
    private JButton odstranitPoložkuButton;
    private JButton button3;
    private JTextArea nazevTextArea;
    private JTextArea popisTextArea;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Storage okno = new Storage();
                    okno.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Connection connection = null;

    public Storage() {
        connection = SQLConnection.dbConnector();

        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50,50,500,500);

        rootPanel.setBorder(new EmptyBorder(5,5,5,5));

        loadStorage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "SELECT * FROM subdodavky";
                    PreparedStatement pst = connection.prepareStatement(query);
                    ResultSet rs = pst.executeQuery();
                    table.setModel(DbUtils.resultSetToTableModel(rs));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });

        pridatPolozkuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "INSERT INTO subdodavky (sub_id, nazev, popis) VALUES((SELECT (MAX(sub_id) + 1) FROM subdodavky), ?, ?)";
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.setString(1, nazevTextArea.getText());
                    pst.setString(2, popisTextArea.getText());

                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Data vložena");

                    pst.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
