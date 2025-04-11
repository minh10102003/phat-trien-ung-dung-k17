package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    public static Connection getConnection() {
        Connection conn = null;

        String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true;";
        String user = "sa"; // tên user SQL Server
        String password = "sapassword"; // mật khẩu

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Kết nối thất bại!");
        }

        return conn;
    }

    public static void main(String[] args) {
        getConnection();
    }
}