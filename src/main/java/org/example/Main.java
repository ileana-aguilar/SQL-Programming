package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;


public class Main {

    public static void main(String[] args) {
        System.out.println("Connect to SQL Server and demo executing queries on different databases.");

        // Base connection URL without specifying the database
        String connectionUrlBase = "jdbc:sqlserver://[];" +
                "encrypt=true;" +
                "trustServerCertificate=true;" +
                "user=[];" +
                "password=[];";

        // Queries to execute on different databases
        String[][] queries = {
                {"TSQLv4", "SELECT orderid, SUM(qty * unitprice) AS totalvalue " + // Added space before FROM
                        "FROM Sales.OrderDetails " + // Added space before GROUP BY and corrected table name
                        "GROUP BY orderid " + // Added space before HAVING
                        "HAVING SUM(qty * unitprice) > 10000 " + // Added space before ORDER BY
                        "ORDER BY totalvalue DESC;"},

                {"Northwinds2022TSQLV7", "WITH JanuaryOrders AS (\n" +
                        "    SELECT CustomerId, EmployeeId\n" +
                        "    FROM Sales.[Order]\n" +
                        "    WHERE OrderDate >= '2016-01-01' AND OrderDate < '2016-02-01'\n" +
                        "),\n" +
                        "FebruaryOrders AS (\n" +
                        "    SELECT CustomerId, EmployeeId\n" +
                        "    FROM Sales.[Order]\n" +
                        "    WHERE OrderDate >= '2016-02-01' AND OrderDate < '2016-03-01'\n" +
                        ")\n" +
                        "SELECT j.CustomerId, j.EmployeeId\n" +
                        "FROM JanuaryOrders j\n" +
                        "LEFT JOIN FebruaryOrders f ON j.CustomerId = f.CustomerId AND j.EmployeeId = f.EmployeeId\n" +
                        "WHERE f.CustomerId IS NULL AND f.EmployeeId IS NULL\n" +
                        "ORDER BY CustomerId;"},
                // Add more databases and queries as needed
        };


        for (String[] queryInfo : queries) {
            String dbName = queryInfo[0];
            String sqlQuery = queryInfo[1];

            // Construct connection URL for the specific database
            String connectionUrl = connectionUrlBase + "databaseName=" + dbName + ";";
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlQuery)) {

                System.out.println("Connected to database: " + dbName);
                System.out.println("Executing query: " +'\n'+ sqlQuery);

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
}
