package org.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final ToDoDao todoDao;

    static {
        try {
            todoDao = new ToDoDao(new Database().getConnection());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws SQLException {

            while (true) {
                System.out.println("1. List all ToDo items");
                System.out.println("2. Find ToDo item");
                System.out.println("3. Add a ToDo item");
                System.out.println("4. Update a ToDo item");
                System.out.println("5. Delete a ToDo item");
                System.out.println("6. Quit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> listToDos();
                    case 2 -> findToDo();
                    case 3 -> addToDo();
                    case 4 -> updateToDo();
                    case 5 -> deleteToDo();
                    case 6 -> {
                        todoDao.close();
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        }
    private static void listToDos() throws SQLException {
        System.out.println("ToDo items:");

        for (ToDo todo : todoDao.list()) {
            int id = todo.getId();
            String description = todo.getTask();
            boolean status = todo.isDone();

            System.out.println("Task ID: " + id);
            System.out.println("Task Description: " + description);
            System.out.println("Task Status: " + status);
        }
    }

    private static void findToDo() throws SQLException {
        System.out.print("Enter ToDo item ID: ");
        int id = scanner.nextInt();

        ToDo todo = todoDao.findById(id);

        if (todo == null) {
            System.out.println("ToDo item not found.");
        } else {
            System.out.println(todo);
        }
    }

    private static void addToDo() throws SQLException {
        System.out.print("Enter ToDo task: ");
        String task = scanner.next();

        ToDo todo = new ToDo(task, false);
        todoDao.create(todo);

        System.out.println("ToDo item added.");
    }

    private static void updateToDo() throws SQLException {
        System.out.print("Enter ToDo item ID: ");
        int id = scanner.nextInt();

        ToDo todo = todoDao.findById(id);

        if (todo == null) {
            System.out.println("ToDo item not found.");
        } else {
            System.out.print("Enter ToDo task: ");
            String task = scanner.next();

            todo.setTask(task);
            todoDao.update(todo);

            System.out.println("ToDo item updated.");
        }
    }

    private static void deleteToDo() throws SQLException {
        System.out.print("Enter ToDo item ID: ");
        int id = scanner.nextInt();

        ToDo todo = todoDao.findById(id);

        if (todo == null) {
            System.out.println("ToDo item not found.");
        } else {
            todoDao.delete(id);

        }
    }

}