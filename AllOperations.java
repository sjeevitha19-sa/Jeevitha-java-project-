package LibraryManager;

import java.sql.*;

public class AllOperations {
    public static final Connection c = DBConnection.getConnection();

    // 1. Add Book
    public static void addBook(int id, String title, String author) {
        String query = "INSERT INTO Books (book_id, title, author, is_available) VALUES (?, ?, ?, TRUE)";
        try {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.executeUpdate();
            System.out.println("Book added successfully: " + title);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. Add Member
    public static void addMember(int id, String name, String phone) {
        String query = "INSERT INTO Members VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.executeUpdate();
            System.out.println("Member registered: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. View Available Books
    public static void viewAvailableBooks() {
        String query = "SELECT * FROM Books WHERE is_available = TRUE";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            System.out.println("\n--- AVAILABLE BOOKS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("book_id") +
                                   " | Title: " + rs.getString("title") +
                                   " | Author: " + rs.getString("author"));
            }
            System.out.println("-----------------------\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Issue Book
    public static void issueBook(int issueId, int bookId, int memberId) {
        String checkQuery = "SELECT is_available FROM Books WHERE book_id = ?";
        String issueQuery = "INSERT INTO IssueRecords (issue_id, book_id, member_id, issue_date) VALUES (?, ?, ?, CURDATE())";
        String updateBookQuery = "UPDATE Books SET is_available = FALSE WHERE book_id = ?";

        try {
            PreparedStatement checkPs = c.prepareStatement(checkQuery);
            checkPs.setInt(1, bookId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getBoolean("is_available")) {
                // Record the issue
                PreparedStatement issuePs = c.prepareStatement(issueQuery);
                issuePs.setInt(1, issueId);
                issuePs.setInt(2, bookId);
                issuePs.setInt(3, memberId);
                issuePs.executeUpdate();

                // Update book status
                PreparedStatement updatePs = c.prepareStatement(updateBookQuery);
                updatePs.setInt(1, bookId);
                updatePs.executeUpdate();

                System.out.println("Book issued successfully!");
            } else {
                System.out.println("Book is currently unavailable or does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Return Book
    public static void returnBook(int bookId, int issueId) {
        String updateRecordQuery = "UPDATE IssueRecords SET return_date = CURDATE() WHERE issue_id = ?";
        String updateBookQuery = "UPDATE Books SET is_available = TRUE WHERE book_id = ?";

        try {
            PreparedStatement recordPs = c.prepareStatement(updateRecordQuery);
            recordPs.setInt(1, issueId);
            recordPs.executeUpdate();

            PreparedStatement bookPs = c.prepareStatement(updateBookQuery);
            bookPs.setInt(1, bookId);
            bookPs.executeUpdate();

            System.out.println("Book returned successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Main method to test operations
    public static void main(String[] args) {
        // Step 1: Add Books
        addBook(101, "Java Programming", "James Gosling");
        addBook(102, "Clean Code", "Robert C. Martin");

        // Step 2: Add Member
        addMember(501, "Alex Johnson", "9876543210");

        // Step 3: Check Available Books
        viewAvailableBooks();

        // Step 4: Issue Book (IssueID: 1001, BookID: 101, MemberID: 501)
        issueBook(1001, 101, 501);

        // Step 5: Check Available Books after issuing
        viewAvailableBooks();

        // Step 6: Return Book
        returnBook(101, 1001);

        // Step 7: Check Available Books after returning
        viewAvailableBooks();
    }
}
