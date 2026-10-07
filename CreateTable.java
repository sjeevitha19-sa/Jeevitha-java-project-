package LibraryManager;

import java.sql.Connection;
import java.sql.Statement;

public class CreateTable {
    public static void main(String[] args) throws Exception {
        Connection c = DBConnection.getConnection();

        // Table 1: Books
        String createBooksTable = """
            CREATE TABLE IF NOT EXISTS Books (
                book_id INT PRIMARY KEY,
                title VARCHAR(150),
                author VARCHAR(100),
                is_available BOOLEAN DEFAULT TRUE
            );
            """;

        // Table 2: Members
        String createMembersTable = """
            CREATE TABLE IF NOT EXISTS Members (
                member_id INT PRIMARY KEY,
                name VARCHAR(100),
                phone VARCHAR(15)
            );
            """;

        // Table 3: Issue Records
        String createIssueRecordsTable = """
            CREATE TABLE IF NOT EXISTS IssueRecords (
                issue_id INT PRIMARY KEY,
                book_id INT,
                member_id INT,
                issue_date DATE,
                return_date DATE,
                FOREIGN KEY (book_id) REFERENCES Books(book_id),
                FOREIGN KEY (member_id) REFERENCES Members(member_id)
            );
            """;

        Statement s = c.createStatement();
        s.executeUpdate(createBooksTable);
        s.executeUpdate(createMembersTable);
        s.executeUpdate(createIssueRecordsTable);

        System.out.println("All Library Tables Created Successfully");
    }
}
