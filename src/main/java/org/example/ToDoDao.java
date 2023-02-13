package org.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ToDoDao {



    private final Connection connection;

    public ToDoDao(Connection connection) {
        this.connection = connection;
    }

    public List<ToDo> list() throws SQLException {
        String sql = "SELECT id, task, done FROM to_do_list";
        List<ToDo> todos = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String task = resultSet.getString("task");
                boolean done = resultSet.getBoolean("done");

                ToDo todo = new ToDo(id, task, done);
                todos.add(todo);
            }
        }

        return todos;
    }

    public ToDo findById(int id) throws SQLException {
        String sql = "SELECT task, done FROM to_do_list WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String task = resultSet.getString("task");
                    boolean done = resultSet.getBoolean("done");

                    return new ToDo(id, task, done);
                } else {
                    return null;
                }
            }
        }
    }

    public void create(ToDo todo) throws SQLException {
        String sql = "INSERT INTO to_do_list (task, done) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, todo.getTask());
            statement.setBoolean(2, todo.isDone());
            statement.executeUpdate();
            // Commit the changes to the database
            connection.commit();


        }
    }

    public void update(ToDo todo) throws SQLException {
        String sql = "UPDATE to_do_list SET task = ?, done = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, todo.getTask());
            statement.setBoolean(2, todo.isDone());
            statement.setInt(3, todo.getId());
            statement.executeUpdate();
            // Commit the changes to the database
            connection.commit();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM to_do_list WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            // Commit the changes to the database
            connection.commit();
        }
    }
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
