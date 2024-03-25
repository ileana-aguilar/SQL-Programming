package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;


public class Main {

    public static void main(String[] args) {
        System.out.println("Connect to SQL Server and demo executing queries on different databases.");

        String connectionUrlBase = "jdbc:sqlserver://localhost:13001;" +
                                    "encrypt=true;" +
                                    "trustServerCertificate=true;" +
                                    "user=sa;" +
                                    "password=PH@123456789;";




        String[][] queries = {
                //Ileana Aguilar Submission
                //top queries
                    {"Northwinds2022TSQLV7", "WITH\n" +
                            "  CustomerOrders\n" +
                            "  AS\n" +
                            "  (\n" +
                            "    SELECT\n" +
                            "      c.CustomerID,\n" +
                            "      c.CustomerCompanyName,\n" +
                            "      COUNT(o.OrderID) AS NumberOfOrders\n" +
                            "    FROM\n" +
                            "      Sales.Customer c\n" +
                            "      JOIN Sales.[Order] o ON c.CustomerID = o.CustomerID\n" +
                            "    GROUP BY\n" +
                            "    c.CustomerID, c.CustomerCompanyName\n" +
                            "  )\n" +
                            "SELECT\n" +
                            "  CustomerID,\n" +
                            "  CustomerCompanyName,\n" +
                            "  NumberOfOrders\n" +
                            "FROM\n" +
                            "  CustomerOrders\n" +
                            "WHERE\n" +
                            "  CustomerID IN (\n" +
                            "    SELECT\n" +
                            "  CustomerID\n" +
                            "FROM\n" +
                            "  CustomerOrders\n" +
                            "WHERE\n" +
                            "      NumberOfOrders > 5\n" +
                            "  )\n" +
                            "FOR JSON PATH, ROOT('CustomerOrdersDetails');"},
                            {
                                "PrestigeCars", 
                                "WITH\n" +
                                "    ModelCosts\n" +
                                "    AS\n" +
                                "    (\n" +
                                "        SELECT\n" +
                                "            ModelID,\n" +
                                "            AVG(Cost) AS AvgCost,\n" +
                                "            AVG(RepairsCost) AS AvgRepairsCost\n" +
                                "        FROM Data.Stock\n" +
                                "        GROUP BY ModelID\n" +
                                "    )\n" +
                                "\n" +
                                "SELECT\n" +
                                "    m.ModelName,\n" +
                                "    mc.AvgCost,\n" +
                                "    mc.AvgRepairsCost,\n" +
                                "    (SELECT COUNT(*)\n" +
                                "    FROM Data.Stock\n" +
                                "    WHERE ModelID = m.ModelID) AS TotalStockCount\n" +
                                "FROM Data.Model m\n" +
                                "    INNER JOIN ModelCosts mc ON m.ModelID = mc.ModelID\n" +
                                "ORDER BY TotalStockCount DESC\n" +
                                "FOR JSON PATH, ROOT('SalesCustomerCategory');"
                            },
                            {
                                "Northwinds2022TSQLV7", 
                                "CREATE OR ALTER FUNCTION dbo.CalculateAverageSale\n" +
                                "(\n" +
                                "    @TotalSalesAmount MONEY,\n" +
                                "    @NumberOfSales INT\n" +
                                ")\n" +
                                "RETURNS MONEY\n" +
                                "AS\n" +
                                "BEGIN\n" +
                                "    RETURN CASE WHEN @NumberOfSales > 0 THEN @TotalSalesAmount / @NumberOfSales ELSE 0 END;\n" +
                                "END;\n" +
                                "WITH\n" +
                                "    EmployeeCustomerSales\n" +
                                "    AS\n" +
                                "    (\n" +
                                "        SELECT\n" +
                                "            o.EmployeeId,\n" +
                                "            o.CustomerId,\n" +
                                "            SUM(o.Freight) AS TotalSalesValue,\n" +
                                "            COUNT(o.OrderId) AS NumberOfSales\n" +
                                "        FROM\n" +
                                "            Sales.[Order] o\n" +
                                "        WHERE\n" +
                                "        o.OrderDate BETWEEN '2014-01-01' AND '2014-12-31'\n" +
                                "        GROUP BY\n" +
                                "        o.EmployeeId,\n" +
                                "        o.CustomerId\n" +
                                "    )\n" +
                                "\n" +
                                "SELECT\n" +
                                "    e.EmployeeFirstName,\n" +
                                "    e.EmployeeLastName,\n" +
                                "    c.CustomerCompanyName,\n" +
                                "    ECS.TotalSalesValue,\n" +
                                "    dbo.CalculateAverageSale(ECS.TotalSalesValue, ECS.NumberOfSales) AS AverageSaleValue\n" +
                                "FROM\n" +
                                "    EmployeeCustomerSales ECS\n" +
                                "    INNER JOIN\n" +
                                "    HumanResources.Employee e ON ECS.EmployeeId = e.EmployeeId\n" +
                                "    INNER JOIN\n" +
                                "    Sales.Customer c ON ECS.CustomerId = c.CustomerId\n" +
                                "GROUP BY\n" +
                                "    e.EmployeeFirstName,\n" +
                                "    e.EmployeeLastName,\n" +
                                "    c.CustomerCompanyName,\n" +
                                "    ECS.TotalSalesValue,\n" +
                                "    ECS.NumberOfSales\n" +
                                "ORDER BY\n" +
                                "    e.EmployeeLastName,\n" +
                                "    c.CustomerCompanyName\n" +
                                "FOR JSON PATH, ROOT('AverageSales');"
                            },
                            //worst queries
                            {
                                "AdventureWorks2017", 
                                "WITH\n" +
                                "    EmailCount\n" +
                                "    AS\n" +
                                "    (\n" +
                                "        SELECT\n" +
                                "            BusinessEntityID,\n" +
                                "            COUNT(EmailAddressID) AS EmailTotal\n" +
                                "        FROM Person.EmailAddress\n" +
                                "        GROUP BY BusinessEntityID\n" +
                                "    )\n" +
                                "\n" +
                                "SELECT\n" +
                                "    BEC.BusinessEntityID,\n" +
                                "    EC.EmailTotal,\n" +
                                "    (SELECT COUNT(PhoneNumber)\n" +
                                "    FROM Person.PersonPhone PP\n" +
                                "    WHERE PP.BusinessEntityID = BEC.BusinessEntityID) AS PhoneTotal\n" +
                                "FROM\n" +
                                "    Person.BusinessEntityContact BEC\n" +
                                "    LEFT JOIN EmailCount EC ON BEC.BusinessEntityID = EC.BusinessEntityID\n" +
                                "GROUP BY BEC.BusinessEntityID, EC.EmailTotal\n" +
                                "ORDER BY BEC.BusinessEntityID\n" +
                                "FOR JSON PATH, ROOT('BusinessEmailCount');"
                            },
                            {
                                "Northwinds2022TSQLV7", 
                                "WITH\n" +
                                "    OrderSales\n" +
                                "    AS\n" +
                                "    (\n" +
                                "        SELECT\n" +
                                "            o.CustomerId,\n" +
                                "            o.OrderId,\n" +
                                "            SUM(CONVERT(int, oda.NewVal) * p.UnitPrice) AS TotalSales\n" +
                                "        FROM Sales.OrderDetailAudit oda\n" +
                                "            INNER JOIN Sales.[Order] o ON oda.OrderId = o.OrderId\n" +
                                "            INNER JOIN Production.Product p ON oda.ProductId = p.ProductId\n" +
                                "        GROUP BY o.CustomerId, o.OrderId\n" +
                                "    )\n" +
                                "SELECT\n" +
                                "    c.CustomerId,\n" +
                                "    c.CustomerCompanyName,\n" +
                                "    SUM(os.TotalSales) AS TotalSalesAmount\n" +
                                "FROM OrderSales os\n" +
                                "    INNER JOIN Sales.Customer c ON os.CustomerId = c.CustomerId\n" +
                                "GROUP BY c.CustomerId, c.CustomerCompanyName\n" +
                                "ORDER BY TotalSalesAmount DESC\n" +
                                "FOR JSON PATH, ROOT('TotalNRepairCosts');"
                            },
                            {
                                "AdventureWorksDW2017", 
                                "WITH\n" +
                                "    AverageSalesByCurrency\n" +
                                "    AS\n" +
                                "    (\n" +
                                "        SELECT\n" +
                                "            f.CurrencyKey,\n" +
                                "            AVG(f.SalesAmount) AS AvgSalesAmount\n" +
                                "        FROM\n" +
                                "            dbo.FactInternetSales f\n" +
                                "            INNER JOIN\n" +
                                "            dbo.DimCurrency c ON f.CurrencyKey = c.CurrencyKey\n" +
                                "        WHERE\n" +
                                "        f.OrderDateKey IN (SELECT DateKey\n" +
                                "        FROM dbo.DimDate\n" +
                                "        WHERE CalendarYear = 2014)\n" +
                                "        GROUP BY\n" +
                                "        f.CurrencyKey\n" +
                                "    )\n" +
                                "SELECT\n" +
                                "    c.CurrencyName,\n" +
                                "    c.CurrencyAlternateKey,\n" +
                                "    AVG(a.AvgSalesAmount) AS OverallAverageSalesAmount\n" +
                                "FROM\n" +
                                "    AverageSalesByCurrency a\n" +
                                "    INNER JOIN\n" +
                                "    dbo.DimCurrency c ON a.CurrencyKey = c.CurrencyKey\n" +
                                "GROUP BY\n" +
                                "    c.CurrencyName, c.CurrencyAlternateKey\n" +
                                "ORDER BY\n" +
                                "    OverallAverageSalesAmount DESC\n" +
                                "FOR JSON PATH, ROOT('AverageSalesAmount');"
                            },

                            //fixed queries
                            {
                                    "AdventureWorks2017",
                                    "WITH\n" +
                                            "    EmailCount\n" +
                                            "    AS\n" +
                                            "    (\n" +
                                            "        SELECT\n" +
                                            "            BusinessEntityID,\n" +
                                            "            COUNT(EmailAddressID) AS EmailTotal\n" +
                                            "        FROM Person.EmailAddress\n" +
                                            "        GROUP BY BusinessEntityID\n" +
                                            "    )\n" +
                                            "\n" +
                                            "SELECT\n" +
                                            "    BEC.BusinessEntityID,\n" +
                                            "    EC.EmailTotal,\n" +
                                            "    (SELECT COUNT(PhoneNumber)\n" +
                                            "    FROM Person.PersonPhone PersonPhoneNumber\n" +
                                            "    WHERE PersonPhoneNumber.BusinessEntityID = BEC.BusinessEntityID) AS PhoneTotal\n" +
                                            "FROM\n" +
                                            "    Person.BusinessEntityContact BEC\n" +
                                            "    LEFT JOIN EmailCount EC ON BEC.BusinessEntityID = EC.BusinessEntityID\n" +
                                            "GROUP BY BEC.BusinessEntityID, EC.EmailTotal\n" +
                                            "ORDER BY BEC.BusinessEntityID\n" +
                                            "FOR JSON PATH, ROOT('BusinessEmailCount');"
                            },
                            {
                                    "Northwinds2022TSQLV7",
                                    "WITH\n" +
                                            "    OrderSales\n" +
                                            "    AS\n" +
                                            "    (\n" +
                                            "        SELECT\n" +
                                            "            OrderD.CustomerId,\n" +
                                            "            OrderD.OrderId,\n" +
                                            "            SUM(CONVERT(int, OrderDA.NewVal) * p.UnitPrice) AS TotalSales\n" +
                                            "        FROM Sales.OrderDetailAudit OrderDA\n" +
                                            "            INNER JOIN Sales.[Order] OrderD ON OrderDA.OrderId = OrderD.OrderId\n" +
                                            "            INNER JOIN Production.Product p ON OrderDA.ProductId = p.ProductId\n" +
                                            "        GROUP BY OrderD.CustomerId, OrderD.OrderId\n" +
                                            "    )\n" +
                                            "SELECT\n" +
                                            "    c.CustomerId,\n" +
                                            "    c.CustomerCompanyName,\n" +
                                            "    SUM(os.TotalSales) AS TotalSalesAmount\n" +
                                            "FROM OrderSales os\n" +
                                            "    INNER JOIN Sales.Customer c ON os.CustomerId = c.CustomerId\n" +
                                            "GROUP BY c.CustomerId, c.CustomerCompanyName\n" +
                                            "ORDER BY TotalSalesAmount DESC\n" +
                                            "FOR JSON PATH, ROOT('TotalNRepairCosts');"
                            },
                            {
                                    "AdventureWorksDW2017",
                                    "WITH\n" +
                                            "    AverageSalesByCurrency\n" +
                                            "    AS\n" +
                                            "    (\n" +
                                            "        SELECT\n" +
                                            "            f.CurrencyKey,\n" +
                                            "            AVG(f.SalesAmount) AS AvgSalesAmount\n" +
                                            "        FROM\n" +
                                            "            dbo.FactInternetSales f\n" +
                                            "            INNER JOIN\n" +
                                            "            dbo.DimCurrency c ON f.CurrencyKey = c.CurrencyKey\n" +
                                            "        WHERE\n" +
                                            "        f.OrderDateKey IN (SELECT DateKey\n" +
                                            "        FROM dbo.DimDate\n" +
                                            "        WHERE CalendarYear = 2014)\n" +
                                            "        GROUP BY\n" +
                                            "        f.CurrencyKey\n" +
                                            "    )\n" +
                                            "SELECT\n" +
                                            "    c.CurrencyName,\n" +
                                            "    c.CurrencyAlternateKey,\n" +
                                            "    AVG(a.AvgSalesAmount) AS OverallAverageSalesAmount\n" +
                                            "FROM\n" +
                                            "    AverageSalesByCurrency a\n" +
                                            "    INNER JOIN\n" +
                                            "    dbo.DimCurrency c ON a.CurrencyKey = c.CurrencyKey\n" +
                                            "GROUP BY\n" +
                                            "    c.CurrencyName, c.CurrencyAlternateKey\n" +
                                            "ORDER BY\n" +
                                            "    OverallAverageSalesAmount DESC\n" +
                                            "FOR JSON PATH, ROOT('AverageSalesAmount');"
                            }

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
