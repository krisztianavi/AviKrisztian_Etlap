package com.example.etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtlapService {
    private static final String DB_DRIVER = "mysql";
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_DATABASE = "etlapdb";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private final Connection connection;

    public EtlapService() throws SQLException {
        String dbUrl = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DATABASE);
        connection = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD);
    }

    public List<Etlap> getAll() throws SQLException {
        List<Etlap> etelek = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM etlap";
        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String nev = result.getString("nev");
            int ar = result.getInt("ar");
            String leiras = result.getString("leiras");
            etelek.add(new Etlap(id, nev, ar, leiras));
        }
        return etelek;
    }

    public boolean create(Etlap etel) throws SQLException {
        String sql = "INSERT INTO etlap(nev, ar, leiras) VALUES(?,?,?);";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, etel.getNev());
        statement.setInt(2, etel.getAr());
        statement.setString(3, etel.getLeiras());
        return statement.executeUpdate() == 1;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        return statement.executeUpdate() == 1;
    }
}