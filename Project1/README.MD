# SQL and Java JDBC Integration Project

## Project Description

This project demonstrates a robust integration of SQL and Java JDBC, showing advanced database operations, data serialization, and the use of open-source libraries to facilitate connectivity between Microsoft SQL Server and Java applications. The focus is on efficient data manipulation and interfacing different technologies to create a seamless data handling environment.

## Features
- **Advanced SQL Operations**: Develops and optimizes SQL queries in Jupyter Notebooks, performing complex joins, utilizing scalar functions, and leveraging SQL's built-in functions like `GROUP BY`, subqueries, and CTEs for efficient data analysis and performance enhancements.

- **Data Serialization**: Converts SQL result sets into JSON objects, promoting a seamless data interchange that integrates effortlessly with other systems.

- **Database Connectivity**: Uses JDBC with an open-source Java library to connect to Microsoft SQL Server, enabling the execution and manipulation of SQL data directly within Java applications.

## Technologies Used

- Jupyter Notebooks

- Microsoft SQL Server

- Docker

- Azure Data Studio

- Java JDBC

- JSON

## Getting Started

### Prerequisites

- Docker installed and running

- Azure Data Studio or similar SQL client

- Java Development Kit (JDK) installed

### Installation
1. Clone this repository to your local machine:

```
git clone https://github.com/ileana-aguilar/SQL-Programming.git
```

2. Navigate to the project directory:

```cd path-to-your-project
```

3. Load the SQL scripts into your SQL Server using Azure Data Studio to set up the database.

### Running the Application

1. Compile the Java files using:

```
javac -cp ".:path/to/mssql-jdbc-12.6.1.jre11.jar" Main.java
```

2. Run the Java application:

```
java -cp ".:path/to/mssql-jdbc-12.6.1.jre11.jar" Main
```