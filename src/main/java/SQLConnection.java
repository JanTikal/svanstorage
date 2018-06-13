import javax.swing.*;
import java.sql.*;

public class SQLConnection {

    Connection conn = null;

    public static Connection dbConnector() {

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:svan_db.sqlite");
            JOptionPane.showMessageDialog(null, "Spojení s databází bylo úspěšné");
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }

    /*
    public static void main(String[] args) {
        Connection connection = dbConnector();
    }
    */

}