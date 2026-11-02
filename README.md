# Chat Log Analyzer

A Java full-stack web application for importing, searching, and analyzing chat logs.

## Stack

- Java 17, Maven, and Tomcat 10.1+
- Jakarta Servlets and JSP
- Hibernate ORM with MySQL 8
- HTML, CSS, and browser JavaScript

## First-time setup

1. Create the database by running `database/schema.sql` in MySQL Workbench, or run:

   ```powershell
   & 'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe' -u root -p < database/schema.sql
   ```

2. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties`. Set your local MySQL password in that new file. It is intentionally ignored by Git.
3. Install Apache Maven and make sure `mvn -version` works in a new terminal.
4. Build the deployable WAR:

   ```powershell
   mvn clean package
   ```

5. Copy `target/chat-log-analyzer.war` to Tomcat's `webapps` directory and start Tomcat. Open `http://localhost:8080/chat-log-analyzer/`.

## Main routes

- `/` - dashboard
- `/messages/search?q=meeting` - keyword search
- `/conversations` - conversation list

## Next feature: imports

Implement an `ImportServlet` with `@MultipartConfig`, parse a chosen export format, validate each row, and save all rows inside one Hibernate transaction. Keep import parsing separate from the DAO so the same data model can support WhatsApp, Slack, or plain CSV exports.

