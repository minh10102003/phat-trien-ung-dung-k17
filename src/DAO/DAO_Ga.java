package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DAO_Ga {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    public static List<String> getAllGa() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT tenGa FROM Ga";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("tenGa"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    public static Map<String, String> getGaMap() {
        Map<String, String> map = new java.util.LinkedHashMap<>();
        String sql = "SELECT maGa, tenGa FROM Ga";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tenGa = rs.getString("tenGa").trim();
                String maGa = rs.getString("maGa").trim();
                map.put(tenGa, maGa);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }


	public static Object getGaList() {
		// TODO Auto-generated method stub
		return null;
	}
}

