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
    private JTabbedPane tabbedPane1;
    private JButton aktualizaceSkladuButton;
    private JTable tableSkladDily;
    private JTable tableDily;
    private JButton aktualizaceSeznamuDily;
    private JButton pridatPolozkuButton;
    private JTextArea kodTextArea;
    private JTextArea vyrobceTextArea;
    private JTextArea dodavatelTextArea;
    private JPanel Dily;
    private JComboBox comboBox1;
    private JButton button1;
    private JTextArea textArea1;

    private String id_dilu;

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

    public void naplnCombobox() {
        try {
            String query = "SELECT id_dilu FROM dily";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                comboBox1.addItem(rs.getString("id_dilu"));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Storage() {
        connection = SQLConnection.dbConnector();

        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50,50,1000,450);

        rootPanel.setBorder(new EmptyBorder(5,5,5,5));

        aktualizaceSeznamuDily.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "SELECT id_dilu, kod AS Kód, dodavatel AS Dodavatel, vyrobce AS Výrobce FROM dily";
                    PreparedStatement pst = connection.prepareStatement(query);
                    ResultSet rs = pst.executeQuery();
                    tableDily.setModel(DbUtils.resultSetToTableModel(rs));

                    pst.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });

        aktualizaceSkladuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query =  "SELECT id_dilu_ve_skladu, id_dilu, id_zakazky, id_skladu, sklady.popis, dily.kod FROM dily_ve_skladu " +
                            "LEFT JOIN sklady USING(id_skladu)" +
                            "LEFT JOIN dily USING(id_dilu) " +
                            "WHERE id_zakazky IS NULL";
                    PreparedStatement pst = connection.prepareStatement(query);
                    ResultSet rs = pst.executeQuery();
                    tableSkladDily.setModel(DbUtils.resultSetToTableModel(rs));

                    pst.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });

        pridatPolozkuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "INSERT INTO dily (id_dilu, kod, dodavatel, vyrobce) VALUES((SELECT (MAX(id_dilu) + 1) FROM dily), ?, ?, ?)";
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.setString(1, kodTextArea.getText());
                    pst.setString(2, dodavatelTextArea.getText());
                    pst.setString(3, vyrobceTextArea.getText());

                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Data vložena");

                    pst.close();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });


        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "SELECT id_dilu FROM dily WHERE id_dilu = ?";
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.setString(1, (String)comboBox1.getSelectedItem());
                    ResultSet rs = pst.executeQuery();

                    textArea1.setText(rs.getString("id_dilu"));
                    id_dilu = rs.getString("id_dilu");

                    pst.close();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query =  "INSERT INTO dily_ve_skladu (id_dilu_ve_skladu, id_dilu, id_skladu) " +
                            "VALUES((SELECT (MAX(id_dilu_ve_skladu) + 1) FROM dily_ve_skladu), ?, ?)";
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.setString(1, id_dilu);
                    String idSkladu = JOptionPane.showInputDialog(frame, "Zadej id_skladu");
                    pst.setString(2, idSkladu);

                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Data vložena");

                    pst.close();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });


        naplnCombobox();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
