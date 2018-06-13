import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame{
    private JFrame frame;
    private JPanel rootPanel;
    private JPasswordField passTextField;
    private JTextField loginTextField;
    private JButton loginButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Login login = new Login();
                    login.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Connection connection = null;

    public Login() {
        connection = SQLConnection.dbConnector();
        setContentPane(rootPanel);
        pack();
        setBounds(50,50,1000,450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                try {
                    String query = "SELECT * FROM UZIVATELE WHERE LOGIN = ? and PASS = ?";
                    PreparedStatement pst = connection.prepareStatement(query);

                    pst.setString(1, loginTextField.getText()); // (index parametru, první question mark) --- máme dva question mark, tak nejdřív ten první (index 1)
                    pst.setString(2, passTextField.getText()); // druhý s indexem 1

                    /* porovnání 1. parametru a 2. parametru */

                    ResultSet rs = pst.executeQuery();
                    int count = 0;
                    while (rs.next()){
                        count = count + 1;
                    }
                    if(count == 1) {
                        JOptionPane.showMessageDialog(null, "Správné");
                        dispose();
                        Storage storage = new Storage();
                        storage.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Špatné");
                    }
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
