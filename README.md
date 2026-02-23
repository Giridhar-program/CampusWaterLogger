# Campus Drinking Water Refill Logger 💧

A Java-based desktop application designed to track, manage, and log daily water dispenser refills across a campus. Built with Java Swing for the graphical user interface and JDBC for MySQL database integration, this application features a complete set of CRUD (Create, Read, Update, Delete) operations.

## ✨ Features

* **Secure Access:** A login screen to prevent unauthorized access to the system.
* **Staff Logging Panel:** Allows staff to quickly select a dispenser and log a new refill with a single click, automatically updating the timestamp and daily count.
* **Admin Dashboard:** Provides a real-time, read-only overview of all dispensers, their locations, and their daily refill statistics.
* **Management Panel (CRUD):** * **Create:** Add new dispensers to the campus network.
    * **Read:** Real-time synchronization with the MySQL database.
    * **Update:** Modify the location of existing dispensers.
    * **Delete:** Remove dispensers from the system securely.
* **Ocean Theme UI:** A custom, cohesive color palette and styling applied across all panels for a modern look.

## 🛠️ Prerequisites

Before you begin, ensure you have met the following requirements:
* **Java Development Kit (JDK):** Version 8 or higher installed.
* **MySQL Server:** Installed and running on your local machine.
* **MySQL Connector/J:** The official JDBC driver for MySQL (`mysql-connector-j-x.x.x.jar`) must be downloaded to connect Java to your database.

## 🗄️ Database Setup

1. Open your MySQL client (e.g., MySQL Workbench, phpMyAdmin, or terminal).
2. Run the following SQL script to create the necessary database and table:

```sql
CREATE DATABASE IF NOT EXISTS campus_water_db;
USE campus_water_db;

CREATE TABLE dispenser_logs (
    id VARCHAR(50) PRIMARY KEY,
    location VARCHAR(100) NOT NULL,
    refills_today INT DEFAULT 0,
    last_refill_time VARCHAR(20)
);
Run the Application: You must include the MySQL Connector JAR file in your classpath when running the program.

On Windows:

```
javac -cp ".;lib/mysql-connector-j-9.6.0.jar" -d bin src/*.java
java -cp "bin;lib/mysql-connector-j-9.6.0.jar" ApplicationLauncher
```
On Mac/Linux:

```
java -cp ".:path/to/mysql-connector-j-8.x.x.jar" ApplicationLauncher
```
🔐 Default Login Credentials
When the application launches, use the following dummy credentials to bypass the login screen:

Username: admin

Password: password

📁 File Structure
ApplicationLauncher.java: Application entry point and Login Screen.

WaterLoggerApp.java: Main frame holding the tabbed interface.

DatabaseHandler.java: Manages the JDBC connection and handles all SQL queries.

Dispenser.java: The core data model object.

StaffPanel.java: GUI for logging refills.

AdminPanel.java: GUI for viewing daily statistics.

ManagePanel.java: GUI for executing Create, Update, and Delete operations.

Theme.java: Centralized styling constants and UI methods.