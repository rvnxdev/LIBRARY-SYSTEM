
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private static final String DATABASE_URL =
        "jdbc:sqlite:innerbook.db";

    public BookRepository() {
        initializeDatabase();
    }

    private Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(DATABASE_URL);
    }

    private void initializeDatabase() {

        try {
            Class.forName("org.sqlite.JDBC");

            String sql =
                "CREATE TABLE IF NOT EXISTS books ("
                + "id INTEGER PRIMARY KEY, "
                + "title TEXT NOT NULL, "
                + "author TEXT NOT NULL, "
                + "genre TEXT NOT NULL, "
                + "borrowed INTEGER NOT NULL DEFAULT 0, "
                + "cover_path TEXT"
                + ")";

            try (
                Connection connection = getConnection();
                Statement statement =
                    connection.createStatement()
            ) {
                statement.executeUpdate(sql);
            }

            System.out.println(
                "INNERBOOK SQLite connected successfully!"
            );

        } catch (ClassNotFoundException | SQLException ex) {

            throw new IllegalStateException(
                "SQLite initialization failed: "
                + ex.getMessage(), ex
            );
        }
    }

    public List<Books> load() throws IOException {

        List<Books> books = new ArrayList<>();

        String sql =
            "SELECT id, title, author, genre, "
            + "borrowed, cover_path "
            + "FROM books ORDER BY id";

        try (
            Connection connection = getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()
        ) {
            while (result.next()) {

                books.add(new Books(
                    result.getInt("id"),
                    result.getString("title"),
                    result.getString("author"),
                    result.getString("genre"),
                    result.getString("cover_path"),
                    result.getBoolean("borrowed")
                ));
            }

        } catch (SQLException ex) {

            throw new IOException(
                "Unable to load books: " + ex.getMessage(), ex
            );
        }

        return books;
    }

    public void insertBook(Books book)
            throws SQLException {

        String sql =
            "INSERT INTO books "
            + "(id, title, author, genre, borrowed, cover_path) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getGenre());
            statement.setBoolean(5, book.isBorrowed());
            statement.setString(6, book.getCoverPath());

            statement.executeUpdate();
        }
    }

    public void updateBook(Books book)
            throws SQLException {

        String sql =
            "UPDATE books SET "
            + "title = ?, author = ?, genre = ?, "
            + "borrowed = ?, cover_path = ? "
            + "WHERE id = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setBoolean(4, book.isBorrowed());
            statement.setString(5, book.getCoverPath());
            statement.setInt(6, book.getId());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new SQLException(
                    "Book not found: " + book.getId()
                );
            }
        }
    }

    public void deleteBook(int id)
            throws SQLException {

        String sql = "DELETE FROM books WHERE id = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new SQLException(
                    "Book not found: " + id
                );
            }
        }
    }

    public boolean isConnected() {

        try (Connection connection = getConnection()) {
            return connection.isValid(3);
        } catch (SQLException ex) {
            return false;
        }
    }
}
