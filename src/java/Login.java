import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame{
    private JFrame frame;
    private JPanel rootPanel;
    private JButton loginButton;
    private JTextField loginTextField;
    private JPasswordField passTextField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Login okno = new Login();
                    okno.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Connection connection = null;

    /* vytvoření aplikace */
    public Login() {
        initialize();
        connection = SQLConnection.dbConnector();
    }

    /* inicializace okna */
    private void initialize() {
        frame = new JFrame();
        frame.setLayout(null);

        frame.setBounds(50,50,500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField loginTextField = new JTextField();
        loginTextField.setBounds(100,70,180,20);
        frame.getContentPane().add(loginTextField);

        JPasswordField passTextField = new JPasswordField();
        passTextField.setBounds(100,100,180,20);
        frame.getContentPane().add(passTextField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                try {
                    String query = "select * from uzivatele where login = ? and pass = ?";
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
                        JOptionPane.showMessageDialog(null, "Correct");
                        frame.dispose();
                        Storage storage = new Storage();
                        storage.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong");
                    }
                    rs.close();
                    pst.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        });
        loginButton.setBounds(300,100,100,20);
        frame.getContentPane().add(loginButton);
    }
}
