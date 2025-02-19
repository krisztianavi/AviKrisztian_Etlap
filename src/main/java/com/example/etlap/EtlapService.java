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
        String sql = "SELECT * FROM etlap ORDER BY nev ASC";
        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                int id = result.getInt("id");
                String nev = result.getString("nev");
                String kategoria = result.getString("kategoria");
                int ar = result.getInt("ar");
                String leiras = result.getString("leiras");
                etelek.add(new Etlap(id, nev, kategoria, ar, leiras));
            }
        }
        return etelek;
    }

    public boolean create(Etlap etel) throws SQLException {
        String sql = "INSERT INTO etlap (nev, ar, leiras) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, etel.getNev());
            statement.setInt(2, etel.getAr());
            statement.setString(3, etel.getLeiras());
            return statement.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean updatePrice(int id, int amount, boolean isPercentage) throws SQLException {
        String sql = isPercentage
                ? "UPDATE etlap SET ar = ar + (ar * ? / 100) WHERE id = ?"
                : "UPDATE etlap SET ar = ar + ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, amount);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;
        }
    }

    public List<String> getCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT kategoria FROM etlap";
        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                categories.add(result.getString("kategoria"));
            }
        }
        return categories;
    }
}
