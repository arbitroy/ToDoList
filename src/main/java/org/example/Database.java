package org.example;

import java.io.*;
import java.net.URL;
import java.sql.*;

import org.apache.ibatis.jdbc.ScriptRunner;
public class Database {

    Statement state = null;
    Connection connection = null;
    public Connection getConnection() throws SQLException, IOException {
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQLite JDBC driver.");
            e.printStackTrace();
        }


        // Connect to the in-memory database

        try {
            connection = DriverManager.getConnection("jdbc:sqlite::resource:to-do-list.db");
            System.out.println("done");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the in-memory SQLite database.");
            e.printStackTrace();

        }

        URL resource = getClass().getResource("/schema.sql");
        //Initialize the script runner
        ScriptRunner sr = new ScriptRunner(connection);

        try (InputStream in = getClass().getResourceAsStream("/schema.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            // Use resource
            sr.runScript(reader);
        }
        //Running the script


        try {
            state = connection.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT COUNT(*) FROM to_do_list");
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            if (count == 0) {
                state.executeUpdate("INSERT INTO to_do_list (task, done) VALUES ('Write code', 0)");
                state.executeUpdate("INSERT INTO to_do_list (task, done) VALUES ('Test code', 0)");
                state.executeUpdate("INSERT INTO to_do_list (task, done) VALUES ('Debug code', 0)");
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert data into to_do_list table.");
            e.printStackTrace();
        }
        return connection;

    }
    public void close() {
        try {
            if (state != null && !state.isClosed()) {
                state.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing statement: " + e.getMessage());
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
