package org.example;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
public class DatabaseManager {
    public static void executeQuery(String dbName, String sqlQuery) {
        String connectionUrl = DatabaseConfig.CONNECTION_URL + "databaseName=" + dbName + ";";
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            System.out.println("Connected to database: " + dbName);
            System.out.println("Executing query:\n" + sqlQuery);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print column names
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnLabel(i));
                if (i < columnCount) System.out.print(" | ");
            }
            System.out.println();

            // Display the result set
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getString(i));
                    if (i < columnCount) System.out.print(" | ");
                }
                System.out.println();
            }
            System.out.println("Query completed on database: " + dbName);
        } catch (Exception e) {
            System.out.println("Error occurred in database: " + dbName);
            e.printStackTrace();
        }
    }
}
