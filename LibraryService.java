
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LibraryService {

    private final List<Books> books;
    private final BookRepository repository;

    // CONSTRUCTOR: LOAD BOOKS FROM SQLITE
    public LibraryService(BookRepository repository) {

        this.repository = repository;
        this.books = new ArrayList<>();

        try {
            this.books.addAll(repository.load());

        } catch (IOException ex) {
            throw new IllegalStateException(
                "Unable to load books from SQLite: "
                + ex.getMessage(), ex
            );
        }
    }

    // ADD BOOK
    public void addBook(Books book) {

        if (book == null) {
            throw new IllegalArgumentException(
                "Book cannot be empty."
            );
        }

        if (findBook(book.getId()) != null) {
            throw new IllegalArgumentException(
                "Book ID already exists."
            );
        }

        try {
            repository.insertBook(book);
            books.add(book);

        } catch (SQLException ex) {
            throw new IllegalStateException(
                "Unable to save book: " + ex.getMessage(), ex
            );
        }
    }

    // EDIT BOOK
    public void editBook(int id, String title, String author,
                         String genre, String coverPath) {

        Books book = requireBook(id);

        String oldTitle = book.getTitle();
        String oldAuthor = book.getAuthor();
        String oldGenre = book.getGenre();
        String oldCover = book.getCoverPath();

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setCoverPath(coverPath);

        try {
            repository.updateBook(book);

        } catch (SQLException ex) {

            book.setTitle(oldTitle);
            book.setAuthor(oldAuthor);
            book.setGenre(oldGenre);
            book.setCoverPath(oldCover);

            throw new IllegalStateException(
                "Unable to update book: " + ex.getMessage(), ex
            );
        }
    }

    // DELETE BOOK
    public void deleteBook(int id) {

        Books book = requireBook(id);

        try {
            repository.deleteBook(id);
            books.remove(book);

        } catch (SQLException ex) {
            throw new IllegalStateException(
                "Unable to delete book: " + ex.getMessage(), ex
            );
        }
    }

    // BORROW BOOK
    public void borrowBook(int id) {

        Books book = requireBook(id);

        if (!book.borrowBook()) {
            throw new IllegalStateException(
                "Already borrowed."
            );
        }

        try {
            repository.updateBook(book);

        } catch (SQLException ex) {

            book.setBorrowed(false);

            throw new IllegalStateException(
                "Unable to save borrowing status: "
                + ex.getMessage(), ex
            );
        }
    }

    // RETURN BOOK
    public void returnBook(int id) {

        Books book = requireBook(id);

        if (!book.returnBook()) {
            throw new IllegalStateException(
                "Book is not borrowed."
            );
        }

        try {
            repository.updateBook(book);

        } catch (SQLException ex) {

            book.setBorrowed(true);

            throw new IllegalStateException(
                "Unable to save return status: "
                + ex.getMessage(), ex
            );
        }
    }

    // FIND BOOK
    public Books findBook(int id) {

        for (Books book : books) {
            if (book.getId() == id) {
                return book;
            }
        }

        return null;
    }

    private Books requireBook(int id) {

        Books book = findBook(id);

        if (book == null) {
            throw new IllegalArgumentException(
                "Book with ID " + id + " was not found."
            );
        }

        return book;
    }

    // COUNTS
    public int getTotalCount() {
        return books.size();
    }

    public int getAvailableCount() {

        int count = 0;

        for (Books book : books) {
            if (!book.isBorrowed()) {
                count++;
            }
        }

        return count;
    }

    public int getBorrowedCount() {

        int count = 0;

        for (Books book : books) {
            if (book.isBorrowed()) {
                count++;
            }
        }

        return count;
    }

    // GENRES
    public Set<String> getGenres() {

        Set<String> genres = new TreeSet<>(
            String.CASE_INSENSITIVE_ORDER
        );

        for (Books book : books) {
            genres.add(book.getGenre());
        }

        return genres;
    }

    // SEARCH
    public List<Books> search(String query, String genre) {

        List<Books> results = new ArrayList<>();

        String text = query == null
            ? ""
            : query.trim().toLowerCase();

        for (Books book : books) {

            boolean matchesText =
                String.valueOf(book.getId()).contains(text)
                || book.getTitle().toLowerCase().contains(text)
                || book.getAuthor().toLowerCase().contains(text)
                || book.getGenre().toLowerCase().contains(text);

            boolean matchesGenre =
                genre == null
                || genre.equals("All Genres")
                || book.getGenre().equalsIgnoreCase(genre);

            if (matchesText && matchesGenre) {
                results.add(book);
            }
        }

        return results;
    }

    // GET ALL BOOKS
    public List<Books> getAllBooks() {
        return new ArrayList<>(books);
    }
}
