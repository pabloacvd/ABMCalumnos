package ar.com.xeven.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**
 * @author Pablo Acevedo - pablo@xeven.com.ar
 */
public class XEVEN {
    private static Connection con = null;
    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                Runtime.getRuntime().addShutdownHook(new MyShDwnHook());
                ResourceBundle rb = ResourceBundle.getBundle("resources.db");
                String driver = rb.getString("driver");
                String url = rb.getString("url");
                String pwd = rb.getString("pwd");
                String usr = rb.getString("usr");
                Class.forName(driver);
                con = DriverManager.getConnection(url, usr, pwd);
            }
            return con;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("La conexión no pudo ser establecida con el driver.", e);
        } catch (SQLException e) {
            System.out.println("La conexión no pudo ser establecida con la base de datos. Reintentando.");
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost/eorders", "root", "");
            } catch (SQLException ex) {
                throw new RuntimeException("La conexión no pudo ser establecida con el driver.", ex);
            }
            return con;
        }
    }
    static class MyShDwnHook extends Thread {
        public void run() {
            try {
                Connection con = XEVEN.getConnection();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error al cerrar la conexión", e);
            }
        }
    }
}