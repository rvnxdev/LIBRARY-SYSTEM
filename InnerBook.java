
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.*;

// ==========================================
// INNERBOOK - DIGITAL LIBRARY
// PART 1 OF 8
// LOGIN AND SIGN UP GUI
// NO DATABASE REQUIRED
// ==========================================

public class InnerBook extends JFrame {

    // ======================================
    // 1. COLOR THEME
    // ======================================

    private final Color BG = new Color(10, 14, 27);
    private final Color SIDEBAR = new Color(15, 22, 39);
    private final Color PANEL = new Color(23, 32, 52);
    private final Color PANEL2 = new Color(32, 44, 68);

    private final Color CYAN = new Color(65, 210, 245);
    private final Color PURPLE = new Color(158, 119, 255);
    private final Color GREEN = new Color(75, 215, 150);
    private final Color RED = new Color(255, 105, 120);

    private final Color TEXT = new Color(240, 244, 255);
    private final Color MUTED = new Color(145, 160, 185);
    private final Color BORDER = new Color(48, 64, 91);

    // ======================================
    // 2. USER ACCOUNTS
    // ======================================

    private final Map<String, String> accounts =
        new HashMap<>();

    private String loggedInUser = "";

    // ======================================
    // 3. LIBRARY SERVICE
    // ======================================

    private LibraryService service;

    // ======================================
    // 4. MAIN COMPONENTS
    // ======================================

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JLabel totalLabel;
    private JLabel availableLabel;
    private JLabel borrowedLabel;

    // ======================================
    // 5. COLLECTION COMPONENTS
    // ======================================

    private JPanel bookCardsPanel;
    private JTextField searchField;
    private JComboBox<String> genreFilter;
    private JLabel collectionCount;

    private int selectedBookId = -1;

    // ======================================
    // 6. NAVIGATION COMPONENTS
    // ======================================

    private JButton dashboardBtn;
    private JButton booksBtn;
    private JButton uploadBtn;
    private JButton categoriesBtn;
    private JButton logoutBtn;

    // ======================================
    // 7. LOGIN COMPONENTS
    // ======================================

    private JTextField loginUsername;
    private JPasswordField loginPassword;

    // ======================================
    // 8. SIGN UP COMPONENTS
    // ======================================

    private JTextField signupUsername;
    private JPasswordField signupPassword;
    private JPasswordField confirmPassword;

    // ======================================
    // 9. CONSTRUCTOR
    // ======================================
public InnerBook() {

    // Initialize SQLite repository.
    BookRepository repository = new BookRepository();

    // Load saved books from SQLite.
    service = new LibraryService(repository);

    // Initialize the GUI.
    initializeWindow();
}

    // ======================================
    // 10. MAIN METHOD
    // ======================================

    // ======================================
    // 11. INITIALIZE WINDOW
    // ======================================

    private void initializeWindow() {

        setTitle("INNERBOOK | Digital Library");

        setSize(1280, 820);

        setMinimumSize(
            new Dimension(1050, 680)
        );

        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        getContentPane().setBackground(BG);

        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG);

        contentPanel.add(
            buildLoginPage(),
            "login"
        );

        contentPanel.add(
            buildSignupPage(),
            "signup"
        );

        setContentPane(contentPanel);

        cardLayout.show(contentPanel, "login");
    }

    // ======================================
    // 12. LOGIN PAGE
    // ======================================

    private JPanel buildLoginPage() {

        JPanel page = new JPanel(
            new GridBagLayout()
        );

        page.setBackground(BG);

        JPanel loginCard = new RoundedPanel(
            PANEL, 28
        );

        loginCard.setLayout(
            new BoxLayout(
                loginCard,
                BoxLayout.Y_AXIS
            )
        );

        loginCard.setBorder(
            new EmptyBorder(40, 45, 40, 45)
        );

        loginCard.setPreferredSize(
            new Dimension(430, 510)
        );

        // LOGO

        JLabel logo = new JLabel(
            "◈ INNERBOOK",
            SwingConstants.CENTER
        );

        logo.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                29
            )
        );

        logo.setForeground(CYAN);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // HEADING

        JLabel heading = new JLabel(
            "Welcome Back",
            SwingConstants.CENTER
        );

        heading.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                25
            )
        );

        heading.setForeground(TEXT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "Sign in to access your personal library."
        );

        subtitle.setFont(
            new Font(
                "SansSerif",
                Font.PLAIN,
                12
            )
        );

        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // USERNAME

        JLabel usernameLabel = createAuthLabel(
            "USERNAME"
        );

        loginUsername = createAuthTextField();

        // PASSWORD

        JLabel passwordLabel = createAuthLabel(
            "PASSWORD"
        );

        loginPassword = new JPasswordField();

        styleAuthField(loginPassword);

        // LOGIN BUTTON

        JButton loginButton = accentButton(
            "LOGIN TO INNERBOOK",
            CYAN
        );

        loginButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        loginButton.setMaximumSize(
            new Dimension(340, 48)
        );

        loginButton.addActionListener(e -> login());

        // SIGN UP LINK

        JLabel noAccount = new JLabel(
            "Don't have an account?"
        );

        noAccount.setFont(
            new Font(
                "SansSerif",
                Font.PLAIN,
                12
            )
        );

        noAccount.setForeground(MUTED);
        noAccount.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton signupButton = new JButton(
            "Create an Account"
        );

        signupButton.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                12
            )
        );

        signupButton.setForeground(CYAN);
        signupButton.setContentAreaFilled(false);
        signupButton.setBorderPainted(false);
        signupButton.setFocusPainted(false);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        signupButton.addActionListener(e ->
            cardLayout.show(contentPanel, "signup")
        );

        // ADD COMPONENTS

        loginCard.add(logo);
        loginCard.add(Box.createVerticalStrut(28));

        loginCard.add(heading);
        loginCard.add(Box.createVerticalStrut(8));

        loginCard.add(subtitle);
        loginCard.add(Box.createVerticalStrut(35));

        loginCard.add(usernameLabel);
        loginCard.add(Box.createVerticalStrut(8));

        loginCard.add(loginUsername);
        loginCard.add(Box.createVerticalStrut(20));

        loginCard.add(passwordLabel);
        loginCard.add(Box.createVerticalStrut(8));

        loginCard.add(loginPassword);
        loginCard.add(Box.createVerticalStrut(30));

        loginCard.add(loginButton);
        loginCard.add(Box.createVerticalStrut(22));

        loginCard.add(noAccount);
        loginCard.add(signupButton);

        page.add(loginCard);

        return page;
    }

    // ======================================
    // 13. SIGN UP PAGE
    // ======================================

    private JPanel buildSignupPage() {

        JPanel page = new JPanel(
            new GridBagLayout()
        );

        page.setBackground(BG);

        JPanel signupCard = new RoundedPanel(
            PANEL, 28
        );

        signupCard.setLayout(
            new BoxLayout(
                signupCard,
                BoxLayout.Y_AXIS
            )
        );

        signupCard.setBorder(
            new EmptyBorder(35, 45, 35, 45)
        );

        signupCard.setPreferredSize(
            new Dimension(440, 550)
        );

        // LOGO

        JLabel logo = new JLabel(
            "◈ INNERBOOK",
            SwingConstants.CENTER
        );

        logo.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                27
            )
        );

        logo.setForeground(CYAN);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // HEADING

        JLabel heading = new JLabel(
            "Create Your Account"
        );

        heading.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                24
            )
        );

        heading.setForeground(TEXT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "Start building your personal reading universe."
        );

        subtitle.setFont(
            new Font(
                "SansSerif",
                Font.PLAIN,
                11
            )
        );

        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // USERNAME

        signupUsername = createAuthTextField();

        // PASSWORD

        signupPassword = new JPasswordField();

        styleAuthField(signupPassword);

        // CONFIRM PASSWORD

        confirmPassword = new JPasswordField();

        styleAuthField(confirmPassword);

        // SIGN UP BUTTON

        JButton signupButton = accentButton(
            "CREATE ACCOUNT",
            CYAN
        );

        signupButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        signupButton.setMaximumSize(
            new Dimension(340, 48)
        );

        signupButton.addActionListener(
            e -> registerAccount()
        );

        // BACK TO LOGIN

        JButton backButton = new JButton(
            "← Back to Login"
        );

        backButton.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                12
            )
        );

        backButton.setForeground(CYAN);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton.addActionListener(e ->
            cardLayout.show(contentPanel, "login")
        );

        // ADD COMPONENTS

        signupCard.add(logo);
        signupCard.add(Box.createVerticalStrut(22));

        signupCard.add(heading);
        signupCard.add(Box.createVerticalStrut(8));

        signupCard.add(subtitle);
        signupCard.add(Box.createVerticalStrut(28));

        signupCard.add(createAuthLabel("USERNAME"));
        signupCard.add(Box.createVerticalStrut(7));
        signupCard.add(signupUsername);

        signupCard.add(Box.createVerticalStrut(17));

        signupCard.add(createAuthLabel("PASSWORD"));
        signupCard.add(Box.createVerticalStrut(7));
        signupCard.add(signupPassword);

        signupCard.add(Box.createVerticalStrut(17));

        signupCard.add(createAuthLabel("CONFIRM PASSWORD"));
        signupCard.add(Box.createVerticalStrut(7));
        signupCard.add(confirmPassword);

        signupCard.add(Box.createVerticalStrut(28));

        signupCard.add(signupButton);
        signupCard.add(Box.createVerticalStrut(15));

        signupCard.add(backButton);

        page.add(signupCard);

        return page;
    }

    // ======================================
    // 14. REGISTER ACCOUNT
    // ======================================

    private void registerAccount() {

        String username =
            signupUsername.getText().trim();

        String password =
            new String(signupPassword.getPassword());

        String confirm =
            new String(confirmPassword.getPassword());

        if (username.isEmpty()
                || password.isEmpty()
                || confirm.isEmpty()) {

            showError("Please complete all fields.");
            return;
        }

        if (username.length() < 3) {

            showError(
                "Username must contain at least 3 characters."
            );

            return;
        }

        if (password.length() < 6) {

            showError(
                "Password must contain at least 6 characters."
            );

            return;
        }

        if (!password.equals(confirm)) {

            showError(
                "Passwords do not match."
            );

            return;
        }

        if (accounts.containsKey(username.toLowerCase())) {

            showError(
                "Username already exists."
            );

            return;
        }

        accounts.put(
            username.toLowerCase(),
            password
        );

        showMessage(
            "Account created successfully!\n"
            + "You can now log in."
        );

        signupUsername.setText("");
        signupPassword.setText("");
        confirmPassword.setText("");

        cardLayout.show(contentPanel, "login");
    }

    // ======================================
    // 15. LOGIN VALIDATION
    // ======================================

    private void login() {

        String username =
            loginUsername.getText().trim();

        String password =
            new String(loginPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            showError(
                "Please enter your username and password."
            );

            return;
        }

        String savedPassword =
            accounts.get(username.toLowerCase());

        if (savedPassword == null
                || !savedPassword.equals(password)) {

            showError(
                "Incorrect username or password."
            );

            return;
        }

        loggedInUser = username;

        loginUsername.setText("");
        loginPassword.setText("");

        openLibrary();
    }

    // ======================================
    // END OF PART 1 OF 8
    // NEXT: MAIN WINDOW AND SIDEBAR
    // ======================================

// ==========================================
// PART 2 OF 8
// MAIN WINDOW AND SIDEBAR
// ==========================================

// ==========================================
// 16. OPEN LIBRARY
// ==========================================

private void openLibrary() {

    getContentPane().removeAll();

    setLayout(new BorderLayout());

    add(
        buildSidebar(),
        BorderLayout.WEST
    );

    cardLayout = new CardLayout();

    contentPanel = new JPanel(cardLayout);
    contentPanel.setBackground(BG);

    contentPanel.add(
        buildDashboardPage(),
        "dashboard"
    );

    contentPanel.add(
        buildBooksPage(),
        "books"
    );

    add(
        contentPanel,
        BorderLayout.CENTER
    );

    cardLayout.show(contentPanel, "dashboard");

    refreshAll();

    revalidate();
    repaint();
}

// ==========================================
// 17. BUILD SIDEBAR
// ==========================================

private JPanel buildSidebar() {

    JPanel sidebar = new JPanel();

    sidebar.setPreferredSize(
        new Dimension(245, 0)
    );

    sidebar.setBackground(SIDEBAR);

    sidebar.setBorder(
        new EmptyBorder(30, 18, 22, 18)
    );

    sidebar.setLayout(
        new BoxLayout(
            sidebar,
            BoxLayout.Y_AXIS
        )
    );

    // LOGO

    JLabel logo = new JLabel("◈ INNERBOOK");

    logo.setFont(
        new Font("SansSerif", Font.BOLD, 24)
    );

    logo.setForeground(CYAN);

    logo.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );

    JLabel subtitle = new JLabel(
        "YOUR PERSONAL LIBRARY"
    );

    subtitle.setFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );

    subtitle.setForeground(MUTED);

    subtitle.setBorder(
        new EmptyBorder(5, 3, 0, 0)
    );

    subtitle.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );

    sidebar.add(logo);
    sidebar.add(Box.createVerticalStrut(6));
    sidebar.add(subtitle);
    sidebar.add(Box.createVerticalStrut(45));

    // LOGGED-IN USER

    JLabel userLabel = new JLabel(
        "SIGNED IN AS"
    );

    userLabel.setFont(
        new Font("SansSerif", Font.BOLD, 10)
    );

    userLabel.setForeground(MUTED);

    userLabel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );

    JLabel userName = new JLabel(
        "👤 " + loggedInUser
    );

    userName.setFont(
        new Font("SansSerif", Font.BOLD, 14)
    );

    userName.setForeground(CYAN);

    userName.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );

    sidebar.add(userLabel);
    sidebar.add(Box.createVerticalStrut(7));
    sidebar.add(userName);
    sidebar.add(Box.createVerticalStrut(40));

    // MENU LABEL

    JLabel menuLabel = new JLabel("MAIN MENU");

    menuLabel.setFont(
        new Font("SansSerif", Font.BOLD, 11)
    );

    menuLabel.setForeground(MUTED);

    menuLabel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );

    sidebar.add(menuLabel);
    sidebar.add(Box.createVerticalStrut(18));

    // NAVIGATION BUTTONS

    dashboardBtn = navButton("⌂     Dashboard");
    booksBtn = navButton("▤     My Collection");
    uploadBtn = navButton("＋     Upload a Book");
    categoriesBtn = navButton("◈     Categories");

    logoutBtn = navButton("↪     Logout");

    // DASHBOARD

    dashboardBtn.addActionListener(e -> {

        setActiveNavigation(dashboardBtn);

        refreshAll();

        cardLayout.show(
            contentPanel,
            "dashboard"
        );
    });

    // COLLECTION

    booksBtn.addActionListener(e -> {

        setActiveNavigation(booksBtn);

        refreshAll();

        cardLayout.show(
            contentPanel,
            "books"
        );
    });

    // UPLOAD

    uploadBtn.addActionListener(e -> {

        addBook();
    });

    // CATEGORIES

    categoriesBtn.addActionListener(e -> {

        showCategories();
    });

    // LOGOUT

    logoutBtn.addActionListener(e -> {

        int answer = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to log out?",
            "Logout",
            JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {

            loggedInUser = "";

            getContentPane().removeAll();

            contentPanel = new JPanel(cardLayout);
            contentPanel.setBackground(BG);

            contentPanel.add(
                buildLoginPage(),
                "login"
            );

            contentPanel.add(
                buildSignupPage(),
                "signup"
            );

            setContentPane(contentPanel);

            cardLayout.show(
                contentPanel,
                "login"
            );

            revalidate();
            repaint();
        }
    });

    // ADD BUTTONS

    sidebar.add(dashboardBtn);
    sidebar.add(Box.createVerticalStrut(10));

    sidebar.add(booksBtn);
    sidebar.add(Box.createVerticalStrut(10));

    sidebar.add(uploadBtn);
    sidebar.add(Box.createVerticalStrut(10));

    sidebar.add(categoriesBtn);

    sidebar.add(Box.createVerticalGlue());

    // SYSTEM STATUS

    JPanel statusPanel = new RoundedPanel(
        PANEL, 20
    );

    statusPanel.setLayout(
        new BoxLayout(
            statusPanel,
            BoxLayout.Y_AXIS
        )
    );

    statusPanel.setBorder(
        new EmptyBorder(16, 13, 16, 13)
    );

    statusPanel.setMaximumSize(
        new Dimension(220, 85)
    );

    statusPanel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );

    JLabel status = new JLabel(
        "● SYSTEM ONLINE"
    );

    status.setFont(
        new Font("SansSerif", Font.BOLD, 11)
    );

    status.setForeground(GREEN);

    JLabel version = new JLabel(
        "INNERBOOK v4.0 | OFFLINE"
    );

    version.setFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );

    version.setForeground(MUTED);

    statusPanel.add(status);
    statusPanel.add(Box.createVerticalStrut(8));
    statusPanel.add(version);

    sidebar.add(statusPanel);

    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(logoutBtn);

    return sidebar;
}

// ==========================================
// 18. NAVIGATION BUTTON
// ==========================================


// ==========================================
// 19. GET ACTIVE NAVIGATION
// ==========================================

// ==========================================
// END OF PART 2 OF 8
// NEXT: PART 3 - DASHBOARD
// ==========================================

// ==========================================
// PART 3 OF 8
// DASHBOARD
// ==========================================

// ==========================================
// 21. REFRESH ALL DATA
// ==========================================

private void refreshAll() {

    if (service == null) {
        return;
    }

    if (totalLabel != null) {
        totalLabel.setText(
            String.valueOf(service.getTotalCount())
        );
    }

    if (availableLabel != null) {
        availableLabel.setText(
            String.valueOf(service.getAvailableCount())
        );
    }

    if (borrowedLabel != null) {
        borrowedLabel.setText(
            String.valueOf(service.getBorrowedCount())
        );
    }

    if (genreFilter != null) {

        String selected =
            (String) genreFilter.getSelectedItem();

        genreFilter.removeAllItems();
        genreFilter.addItem("All Genres");

        for (String genre : service.getGenres()) {
            genreFilter.addItem(genre);
        }

        genreFilter.setSelectedItem(
            selected == null ? "All Genres" : selected
        );
    }

    refreshBooks();
}

// ==========================================
// 22. BUILD DASHBOARD PAGE
// ==========================================

private JPanel buildDashboardPage() {

    JPanel page = new JPanel(
        new BorderLayout(0, 25)
    );

    page.setBackground(BG);

    page.setBorder(
        new EmptyBorder(35, 35, 30, 35)
    );

    // HEADER

    JPanel header = new JPanel(
        new BorderLayout()
    );

    header.setOpaque(false);

    JPanel heading = new JPanel();

    heading.setOpaque(false);

    heading.setLayout(
        new BoxLayout(
            heading,
            BoxLayout.Y_AXIS
        )
    );

    JLabel welcome = new JLabel(
        "Welcome to InnerBook, " + loggedInUser + "!"
    );

    welcome.setFont(
        new Font("SansSerif", Font.BOLD, 28)
    );

    welcome.setForeground(TEXT);

    JLabel description = new JLabel(
        "Your personal reading universe, all in one place."
    );

    description.setFont(
        new Font("SansSerif", Font.PLAIN, 14)
    );

    description.setForeground(MUTED);

    heading.add(welcome);
    heading.add(Box.createVerticalStrut(8));
    heading.add(description);

    JButton addButton = accentButton(
        "+ Add New Book",
        CYAN
    );

    addButton.addActionListener(e -> addBook());

    header.add(heading, BorderLayout.WEST);
    header.add(addButton, BorderLayout.EAST);

    // STATISTICS

    JPanel statistics = new JPanel(
        new GridLayout(1, 3, 18, 0)
    );

    statistics.setOpaque(false);

    totalLabel = new JLabel("0");
    availableLabel = new JLabel("0");
    borrowedLabel = new JLabel("0");

    statistics.add(
        createStatCard(
            "TOTAL BOOKS",
            totalLabel,
            "▤",
            CYAN,
            "Your entire collection"
        )
    );

    statistics.add(
        createStatCard(
            "AVAILABLE",
            availableLabel,
            "✓",
            GREEN,
            "Ready to be borrowed"
        )
    );

    statistics.add(
        createStatCard(
            "BORROWED",
            borrowedLabel,
            "↗",
            PURPLE,
            "Currently checked out"
        )
    );

    // HERO PANEL

    JPanel hero = new RoundedPanel(
        new Color(29, 42, 69),
        28
    );

    hero.setLayout(
        new BorderLayout(20, 15)
    );

    hero.setBorder(
        new EmptyBorder(30, 30, 30, 30)
    );

    JPanel heroText = new JPanel();

    heroText.setOpaque(false);

    heroText.setLayout(
        new BoxLayout(
            heroText,
            BoxLayout.Y_AXIS
        )
    );

    JLabel heroTag = new JLabel(
        "YOUR DIGITAL LIBRARY"
    );

    heroTag.setFont(
        new Font("SansSerif", Font.BOLD, 11)
    );

    heroTag.setForeground(CYAN);

    JLabel heroTitle = new JLabel(
        "Every book has a story."
    );

    heroTitle.setFont(
        new Font("SansSerif", Font.BOLD, 27)
    );

    heroTitle.setForeground(TEXT);

    JLabel heroDescription = new JLabel(
        "<html>Organize your favorite titles, discover your "
        + "collection, and keep your reading world organized."
        + "</html>"
    );

    heroDescription.setFont(
        new Font("SansSerif", Font.PLAIN, 14)
    );

    heroDescription.setForeground(MUTED);

    heroText.add(heroTag);
    heroText.add(Box.createVerticalStrut(14));
    heroText.add(heroTitle);
    heroText.add(Box.createVerticalStrut(12));
    heroText.add(heroDescription);

    JButton exploreButton = accentButton(
        "Explore My Collection  →",
        PURPLE
    );

    exploreButton.addActionListener(e -> {

        setActiveNavigation(booksBtn);

        refreshAll();

        cardLayout.show(
            contentPanel,
            "books"
        );
    });

    hero.add(
        heroText,
        BorderLayout.CENTER
    );

    hero.add(
        exploreButton,
        BorderLayout.SOUTH
    );

    // QUICK ACTIONS TITLE

    JLabel quickTitle = new JLabel(
        "Quick Actions"
    );

    quickTitle.setFont(
        new Font("SansSerif", Font.BOLD, 20)
    );

    quickTitle.setForeground(TEXT);

    // QUICK ACTION CARDS

    JPanel quickActions = new JPanel(
        new GridLayout(1, 2, 18, 0)
    );

    quickActions.setOpaque(false);

    quickActions.add(
        createQuickCard(
            "＋",
            "Add a New Book",
            "Expand your personal collection",
            CYAN,
            e -> addBook()
        )
    );

    quickActions.add(
        createQuickCard(
            "▤",
            "View My Books",
            "Browse and manage your books",
            PURPLE,
            e -> {

                setActiveNavigation(booksBtn);

                refreshAll();

                cardLayout.show(
                    contentPanel,
                    "books"
                );
            }
        )
    );

    // CENTER CONTENT

    JPanel center = new JPanel(
        new BorderLayout(0, 25)
    );

    center.setOpaque(false);

    JPanel upper = new JPanel(
        new BorderLayout(0, 20)
    );

    upper.setOpaque(false);

    upper.add(
        statistics,
        BorderLayout.NORTH
    );

    upper.add(
        hero,
        BorderLayout.CENTER
    );

    JPanel lower = new JPanel(
        new BorderLayout(0, 15)
    );

    lower.setOpaque(false);

    lower.add(
        quickTitle,
        BorderLayout.NORTH
    );

    lower.add(
        quickActions,
        BorderLayout.CENTER
    );

    center.add(
        upper,
        BorderLayout.NORTH
    );

    center.add(
        lower,
        BorderLayout.CENTER
    );

    page.add(
        header,
        BorderLayout.NORTH
    );

    page.add(
        center,
        BorderLayout.CENTER
    );

    return page;
}

// ==========================================
// 23. STATISTICS CARD
// ==========================================

private JPanel createStatCard(
    String title,
    JLabel number,
    String symbol,
    Color accent,
    String description
) {

    JPanel card = new RoundedPanel(
        PANEL,
        24
    );

    card.setLayout(
        new BorderLayout(0, 15)
    );

    card.setBorder(
        new EmptyBorder(20, 22, 20, 22)
    );

    JPanel top = new JPanel(
        new BorderLayout()
    );

    top.setOpaque(false);

    JLabel heading = new JLabel(title);

    heading.setFont(
        new Font("SansSerif", Font.BOLD, 11)
    );

    heading.setForeground(MUTED);

    JLabel icon = new JLabel(symbol);

    icon.setFont(
        new Font("SansSerif", Font.BOLD, 25)
    );

    icon.setForeground(accent);

    top.add(
        heading,
        BorderLayout.WEST
    );

    top.add(
        icon,
        BorderLayout.EAST
    );

    number.setFont(
        new Font("SansSerif", Font.BOLD, 36)
    );

    number.setForeground(TEXT);

    JLabel detail = new JLabel(description);

    detail.setFont(
        new Font("SansSerif", Font.PLAIN, 11)
    );

    detail.setForeground(MUTED);

    JPanel content = new JPanel();

    content.setOpaque(false);

    content.setLayout(
        new BoxLayout(
            content,
            BoxLayout.Y_AXIS
        )
    );

    content.add(number);
    content.add(Box.createVerticalStrut(5));
    content.add(detail);

    card.add(
        top,
        BorderLayout.NORTH
    );

    card.add(
        content,
        BorderLayout.CENTER
    );

    return card;
}

// ==========================================
// 24. QUICK ACTION CARD
// ==========================================

private JPanel createQuickCard(
    String symbol,
    String title,
    String description,
    Color accent,
    ActionListener action
) {

    JPanel card = new RoundedPanel(
        PANEL,
        22
    );

    card.setLayout(
        new BorderLayout(15, 0)
    );

    card.setBorder(
        new EmptyBorder(22, 20, 22, 20)
    );

    card.setCursor(
        new Cursor(Cursor.HAND_CURSOR)
    );

    JLabel icon = new JLabel(symbol);

    icon.setFont(
        new Font("SansSerif", Font.BOLD, 30)
    );

    icon.setForeground(accent);

    JPanel textPanel = new JPanel();

    textPanel.setOpaque(false);

    textPanel.setLayout(
        new BoxLayout(
            textPanel,
            BoxLayout.Y_AXIS
        )
    );

    JLabel titleLabel = new JLabel(title);

    titleLabel.setFont(
        new Font("SansSerif", Font.BOLD, 14)
    );

    titleLabel.setForeground(TEXT);

    JLabel descriptionLabel = new JLabel(
        description
    );

    descriptionLabel.setFont(
        new Font("SansSerif", Font.PLAIN, 11)
    );

    descriptionLabel.setForeground(MUTED);

    textPanel.add(titleLabel);
    textPanel.add(Box.createVerticalStrut(7));
    textPanel.add(descriptionLabel);

    card.add(
        icon,
        BorderLayout.WEST
    );

    card.add(
        textPanel,
        BorderLayout.CENTER
    );

    card.addMouseListener(
        new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                action.actionPerformed(
                    new ActionEvent(
                        card,
                        ActionEvent.ACTION_PERFORMED,
                        "quick-action"
                    )
                );
            }

            @Override
            public void mouseEntered(MouseEvent e) {

                card.setBackground(PANEL2);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {

                card.setBackground(PANEL);
                card.repaint();
            }
        }
    );

    return card;
}

// ==========================================
// END OF PART 3 OF 8
// NEXT: PART 4 - BOOK COLLECTION
// ==========================================

// ==========================================
// PART 4 OF 8
// BOOK COLLECTION
// ==========================================

// ==========================================
// 25. BUILD BOOKS PAGE
// ==========================================

private JPanel buildBooksPage() {

    JPanel page = new JPanel(
        new BorderLayout(0, 20)
    );

    page.setBackground(BG);

    page.setBorder(
        new EmptyBorder(30, 35, 25, 35)
    );

    // HEADER

    JPanel heading = new JPanel();

    heading.setOpaque(false);

    heading.setLayout(
        new BoxLayout(
            heading,
            BoxLayout.Y_AXIS
        )
    );

    JLabel title = new JLabel(
        "My Collection"
    );

    title.setFont(
        new Font("SansSerif", Font.BOLD, 29)
    );

    title.setForeground(TEXT);

    JLabel subtitle = new JLabel(
        "Manage your books and organize your reading world."
    );

    subtitle.setFont(
        new Font("SansSerif", Font.PLAIN, 13)
    );

    subtitle.setForeground(MUTED);

    heading.add(title);
    heading.add(Box.createVerticalStrut(7));
    heading.add(subtitle);

    // SEARCH AND FILTER

    JPanel searchPanel = new JPanel(
        new BorderLayout(12, 0)
    );

    searchPanel.setOpaque(false);

    searchField = new JTextField();

    searchField.setFont(
        new Font("SansSerif", Font.PLAIN, 14)
    );

    searchField.setForeground(TEXT);
    searchField.setBackground(PANEL);
    searchField.setCaretColor(CYAN);

    searchField.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                PANEL2,
                1
            ),
            new EmptyBorder(
                13, 15, 13, 15
            )
        )
    );

    searchField.setToolTipText(
        "Search by book ID, title, author or genre"
    );

    genreFilter = new JComboBox<>();

    genreFilter.setFont(
        new Font("SansSerif", Font.PLAIN, 13)
    );

    genreFilter.setBackground(PANEL);
    genreFilter.setForeground(TEXT);

    genreFilter.setPreferredSize(
        new Dimension(175, 45)
    );

    searchPanel.add(
        searchField,
        BorderLayout.CENTER
    );

    searchPanel.add(
        genreFilter,
        BorderLayout.EAST
    );

    JPanel top = new JPanel(
        new BorderLayout(0, 18)
    );

    top.setOpaque(false);

    top.add(
        heading,
        BorderLayout.NORTH
    );

    top.add(
        searchPanel,
        BorderLayout.CENTER
    );

    // BOOK CARDS PANEL

    bookCardsPanel = new JPanel(
        new GridLayout(0, 3, 18, 18)
    );

    bookCardsPanel.setBackground(BG);

    bookCardsPanel.setBorder(
        new EmptyBorder(5, 5, 5, 5)
    );

    JScrollPane scroll = new JScrollPane(
        bookCardsPanel
    );

    scroll.setBorder(null);
    scroll.setBackground(BG);

    scroll.getViewport().setBackground(BG);

    scroll.getVerticalScrollBar()
        .setUnitIncrement(18);

    // ACTION BUTTONS

    JPanel actions = new JPanel(
        new FlowLayout(
            FlowLayout.LEFT,
            10,
            5
        )
    );

    actions.setOpaque(false);

    JButton addButton = accentButton(
        "+ Add Book",
        CYAN
    );

    JButton editButton = accentButton(
        "Edit",
        PURPLE
    );

    JButton deleteButton = accentButton(
        "Delete",
        RED
    );

    JButton borrowButton = accentButton(
        "Borrow / Return",
        GREEN
    );

    addButton.addActionListener(
        e -> addBook()
    );

    editButton.addActionListener(
        e -> editBook()
    );

    deleteButton.addActionListener(
        e -> deleteBook()
    );

    borrowButton.addActionListener(
        e -> borrowOrReturn()
    );

    actions.add(addButton);
    actions.add(editButton);
    actions.add(deleteButton);
    actions.add(borrowButton);

    page.add(
        top,
        BorderLayout.NORTH
    );

    page.add(
        scroll,
        BorderLayout.CENTER
    );

    page.add(
        actions,
        BorderLayout.SOUTH
    );

    // LIVE SEARCH

    searchField.getDocument().addDocumentListener(
        new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshBooks();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshBooks();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshBooks();
            }
        }
    );

    genreFilter.addActionListener(
        e -> refreshBooks()
    );

    return page;
}

// ==========================================
// 26. CREATE BOOK CARD
// ==========================================

private JPanel createBookCard(Books book) {

    RoundedPanel card = new RoundedPanel(
        PANEL,
        24
    );

    card.setLayout(
        new BorderLayout(0, 12)
    );

    card.setBorder(
        new EmptyBorder(15, 15, 15, 15)
    );

    card.setCursor(
        new Cursor(Cursor.HAND_CURSOR)
    );

    // BOOK COVER

    JLabel cover = new JLabel(
        "▤",
        SwingConstants.CENTER
    );

    cover.setPreferredSize(
        new Dimension(100, 145)
    );

    cover.setOpaque(true);
    cover.setBackground(PANEL2);
    cover.setForeground(CYAN);

    cover.setFont(
        new Font("SansSerif", Font.BOLD, 35)
    );

    ImageIcon icon = createCoverIcon(
        book.getCoverPath()
    );

    if (icon != null) {
        cover.setIcon(icon);
        cover.setText("");
    }

    JPanel coverContainer = new JPanel(
        new FlowLayout(
            FlowLayout.CENTER,
            0,
            0
        )
    );

    coverContainer.setOpaque(false);
    coverContainer.add(cover);

    // BOOK DETAILS

    JPanel details = new JPanel();

    details.setOpaque(false);

    details.setLayout(
        new BoxLayout(
            details,
            BoxLayout.Y_AXIS
        )
    );

    JLabel bookTitle = new JLabel(
        book.getTitle()
    );

    bookTitle.setFont(
        new Font("SansSerif", Font.BOLD, 15)
    );

    bookTitle.setForeground(TEXT);

    bookTitle.setAlignmentX(
        Component.CENTER_ALIGNMENT
    );

    JLabel author = new JLabel(
        book.getAuthor()
    );

    author.setFont(
        new Font("SansSerif", Font.PLAIN, 12)
    );

    author.setForeground(MUTED);

    author.setAlignmentX(
        Component.CENTER_ALIGNMENT
    );

    JLabel genre = new JLabel(
        book.getGenre()
    );

    genre.setFont(
        new Font("SansSerif", Font.PLAIN, 11)
    );

    genre.setForeground(CYAN);

    genre.setAlignmentX(
        Component.CENTER_ALIGNMENT
    );

    JLabel status = new JLabel(
        book.isBorrowed()
            ? "● BORROWED"
            : "● AVAILABLE"
    );

    status.setFont(
        new Font("SansSerif", Font.BOLD, 10)
    );

    status.setForeground(
        book.isBorrowed()
            ? PURPLE
            : GREEN
    );

    status.setAlignmentX(
        Component.CENTER_ALIGNMENT
    );

    details.add(bookTitle);
    details.add(Box.createVerticalStrut(7));
    details.add(author);
    details.add(Box.createVerticalStrut(7));
    details.add(genre);
    details.add(Box.createVerticalStrut(12));
    details.add(status);

    card.add(
        coverContainer,
        BorderLayout.NORTH
    );

    card.add(
        details,
        BorderLayout.CENTER
    );

    // BOOK SELECTION

    card.addMouseListener(
        new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                selectedBookId = book.getId();

                refreshBooks();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

                if (selectedBookId != book.getId()) {

                    card.setBackground(PANEL2);
                    card.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                if (selectedBookId != book.getId()) {

                    card.setBackground(PANEL);
                    card.repaint();
                }
            }
        }
    );

    if (selectedBookId == book.getId()) {
        card.setBackground(PANEL2);
    }

    return card;
}

// ==========================================
// 27. LOAD BOOK COVER
// ==========================================

private ImageIcon createCoverIcon(String path) {

    if (path == null || path.trim().isEmpty()) {
        return null;
    }

    File file = new File(path);

    if (!file.isFile()) {
        return null;
    }

    try {

        BufferedImage image = ImageIO.read(file);

        if (image == null) {
            return null;
        }

        Image resized = image.getScaledInstance(
            100,
            145,
            Image.SCALE_SMOOTH
        );

        return new ImageIcon(resized);

    } catch (IOException ex) {

        return null;
    }
}

// ==========================================
// 28. GET SELECTED BOOK
// ==========================================

private Books getSelectedBook() {

    if (selectedBookId == -1) {

        showError(
            "Please select a book first."
        );

        return null;
    }

    Books selected = service.findBook(
        selectedBookId
    );

    if (selected == null) {

        selectedBookId = -1;

        showError(
            "Selected book no longer exists."
        );

        return null;
    }

    return selected;
}

// ==========================================
// 29. REFRESH BOOK COLLECTION
// ==========================================

private void refreshBooks() {

    if (bookCardsPanel == null
            || searchField == null
            || genreFilter == null
            || service == null) {

        return;
    }

    String query = searchField.getText();

    String genre = (String)
        genreFilter.getSelectedItem();

    List<Books> results = service.search(
        query,
        genre
    );

    bookCardsPanel.removeAll();

    boolean selectedStillExists = false;

    for (Books book : results) {

        if (book.getId() == selectedBookId) {
            selectedStillExists = true;
        }

        bookCardsPanel.add(
            createBookCard(book)
        );
    }

    if (!selectedStillExists) {
        selectedBookId = -1;
    }

    if (results.isEmpty()) {

        JPanel empty = new JPanel(
            new BorderLayout()
        );

        empty.setOpaque(false);

        JLabel message = new JLabel(
            "<html><center>"
            + "No books found.<br><br>"
            + "Try another search or add a new book."
            + "</center></html>",
            SwingConstants.CENTER
        );

        message.setFont(
            new Font("SansSerif", Font.PLAIN, 15)
        );

        message.setForeground(MUTED);

        empty.add(
            message,
            BorderLayout.CENTER
        );

        bookCardsPanel.setLayout(
            new BorderLayout()
        );

        bookCardsPanel.add(
            empty,
            BorderLayout.CENTER
        );

    } else {

        bookCardsPanel.setLayout(
            new GridLayout(0, 3, 18, 18)
        );
    }

    bookCardsPanel.revalidate();
    bookCardsPanel.repaint();
}

// ==========================================
// END OF PART 4 OF 8
// NEXT: PART 5 - ADD AND EDIT BOOKS
// ==========================================

// ==========================================
// PART 5 OF 8
// ADDING AND EDITING BOOKS
// ==========================================

// ==========================================
// 30. ADD / UPLOAD BOOK
// ==========================================

private void addBook() {

    JTextField idField = new JTextField();
    JTextField titleField = new JTextField();
    JTextField authorField = new JTextField();

    JTextField genreField = new JTextField(
        "Uncategorized"
    );

    JTextField coverField = new JTextField();

    coverField.setEditable(false);

    JLabel preview = new JLabel(
        "No cover selected",
        SwingConstants.CENTER
    );

    preview.setPreferredSize(
        new Dimension(110, 145)
    );

    preview.setOpaque(true);
    preview.setBackground(PANEL2);
    preview.setForeground(MUTED);

    // BROWSE IMAGE BUTTON

    JButton browseButton = accentButton(
        "Browse Image",
        CYAN
    );

    browseButton.addActionListener(e -> {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle(
            "Choose Book Cover"
        );

        chooser.setFileFilter(
            new FileNameExtensionFilter(
                "Image Files",
                "jpg",
                "jpeg",
                "png",
                "gif"
            )
        );

        if (chooser.showOpenDialog(this)
                == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            ImageIcon icon = createPreviewIcon(
                file.getAbsolutePath(),
                110,
                145
            );

            if (icon == null) {

                showError(
                    "Invalid image file."
                );

                return;
            }

            coverField.setText(
                file.getAbsolutePath()
            );

            preview.setIcon(icon);
            preview.setText("");
        }
    });

    JPanel imageControls = new JPanel(
        new BorderLayout(8, 0)
    );

    imageControls.setOpaque(false);

    imageControls.add(
        coverField,
        BorderLayout.CENTER
    );

    imageControls.add(
        browseButton,
        BorderLayout.EAST
    );

    JPanel imagePanel = new JPanel(
        new BorderLayout(5, 8)
    );

    imagePanel.setOpaque(false);

    imagePanel.add(
        preview,
        BorderLayout.CENTER
    );

    imagePanel.add(
        imageControls,
        BorderLayout.SOUTH
    );

    // FORM

    JPanel form = createForm(
        new String[] {
            "Book ID:",
            "Book Title:",
            "Author:",
            "Genre:",
            "Book Cover:"
        },
        new JComponent[] {
            idField,
            titleField,
            authorField,
            genreField,
            imagePanel
        }
    );

    int result = JOptionPane.showConfirmDialog(
        this,
        form,
        "Add New Book",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
    );

    if (result != JOptionPane.OK_OPTION) {
        return;
    }

    try {

        int id = Integer.parseInt(
            idField.getText().trim()
        );

        Books book = new Books(
            id,
            titleField.getText(),
            authorField.getText(),
            genreField.getText(),
            coverField.getText()
        );

        service.addBook(book);

        refreshAll();

        showMessage(
            "Book added successfully!"
        );

    } catch (NumberFormatException ex) {

        showError(
            "Book ID must be a valid number."
        );

    } catch (IllegalArgumentException ex) {

        showError(
            ex.getMessage()
        );

    } catch (RuntimeException ex) {

        showError(
            "Unable to add book:\n"
            + ex.getMessage()
        );
    }
}

// ==========================================
// 31. CREATE IMAGE PREVIEW
// ==========================================

private ImageIcon createPreviewIcon(
    String path,
    int width,
    int height
) {

    if (path == null || path.trim().isEmpty()) {
        return null;
    }

    try {

        BufferedImage image = ImageIO.read(
            new File(path)
        );

        if (image == null) {
            return null;
        }

        Image resized = image.getScaledInstance(
            width,
            height,
            Image.SCALE_SMOOTH
        );

        return new ImageIcon(resized);

    } catch (IOException ex) {

        return null;
    }
}

// ==========================================
// 32. EDIT BOOK
// ==========================================

private void editBook() {

    Books selected = getSelectedBook();

    if (selected == null) {
        return;
    }

    JTextField titleField = new JTextField(
        selected.getTitle()
    );

    JTextField authorField = new JTextField(
        selected.getAuthor()
    );

    JTextField genreField = new JTextField(
        selected.getGenre()
    );

    JTextField coverField = new JTextField(
        selected.getCoverPath() == null
            ? ""
            : selected.getCoverPath()
    );

    coverField.setEditable(false);

    JLabel preview = new JLabel(
        "No cover",
        SwingConstants.CENTER
    );

    preview.setPreferredSize(
        new Dimension(100, 130)
    );

    preview.setOpaque(true);
    preview.setBackground(PANEL2);
    preview.setForeground(MUTED);

    if (selected.hasCover()) {

        ImageIcon icon = createPreviewIcon(
            selected.getCoverPath(),
            100,
            130
        );

        if (icon != null) {

            preview.setIcon(icon);
            preview.setText("");
        }
    }

    // CHANGE IMAGE BUTTON

    JButton browseButton = accentButton(
        "Change Image",
        CYAN
    );

    browseButton.addActionListener(e -> {

        JFileChooser chooser = new JFileChooser();

        chooser.setFileFilter(
            new FileNameExtensionFilter(
                "Image Files",
                "jpg",
                "jpeg",
                "png",
                "gif"
            )
        );

        if (chooser.showOpenDialog(this)
                == JFileChooser.APPROVE_OPTION) {

            String path = chooser.getSelectedFile()
                .getAbsolutePath();

            ImageIcon icon = createPreviewIcon(
                path,
                100,
                130
            );

            if (icon == null) {

                showError(
                    "Unable to read image."
                );

                return;
            }

            coverField.setText(path);

            preview.setIcon(icon);
            preview.setText("");
        }
    });

    JPanel imagePanel = new JPanel(
        new BorderLayout(5, 8)
    );

    imagePanel.setOpaque(false);

    imagePanel.add(
        preview,
        BorderLayout.CENTER
    );

    imagePanel.add(
        browseButton,
        BorderLayout.SOUTH
    );

    // EDIT FORM

    JPanel form = createForm(
        new String[] {
            "Book Title:",
            "Author:",
            "Genre:",
            "Book Cover:"
        },
        new JComponent[] {
            titleField,
            authorField,
            genreField,
            imagePanel
        }
    );

    int result = JOptionPane.showConfirmDialog(
        this,
        form,
        "Edit Book #" + selected.getId(),
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
    );

    if (result != JOptionPane.OK_OPTION) {
        return;
    }

    try {

        service.editBook(
            selected.getId(),
            titleField.getText(),
            authorField.getText(),
            genreField.getText(),
            coverField.getText()
        );

        refreshAll();

        showMessage(
            "Book updated successfully!"
        );

    } catch (IllegalArgumentException ex) {

        showError(
            ex.getMessage()
        );

    } catch (RuntimeException ex) {

        showError(
            "Unable to update book:\n"
            + ex.getMessage()
        );
    }
}

// ==========================================
// END OF PART 5 OF 8
// NEXT: PART 6 - DELETE, BORROW AND RETURN
// ==========================================

// ==========================================
// PART 6 OF 8
// DELETE, BORROW AND RETURN
// ==========================================

// ==========================================
// 33. DELETE BOOK
// ==========================================

private void deleteBook() {

    Books selected = getSelectedBook();

    if (selected == null) {
        return;
    }

    int answer = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete:\n\n"
            + selected.getTitle(),
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (answer != JOptionPane.YES_OPTION) {
        return;
    }

    try {

        service.deleteBook(
            selected.getId()
        );

        selectedBookId = -1;

        refreshAll();

        showMessage(
            "Book deleted successfully!"
        );

    } catch (RuntimeException ex) {

        showError(
            "Unable to delete book:\n"
                + ex.getMessage()
        );
    }
}

// ==========================================
// 34. BORROW / RETURN BOOK
// ==========================================

private void borrowOrReturn() {

    Books selected = getSelectedBook();

    if (selected == null) {
        return;
    }

    try {

        if (selected.isBorrowed()) {

            service.returnBook(
                selected.getId()
            );

            showMessage(
                "Book returned successfully!"
            );

        } else {

            service.borrowBook(
                selected.getId()
            );

            showMessage(
                "Book borrowed successfully!"
            );
        }

        refreshAll();

    } catch (RuntimeException ex) {

        showError(
            "Unable to update book status:\n"
                + ex.getMessage()
        );
    }
}

// ==========================================
// 35. CREATE MODERN FORM
// ==========================================

private JPanel createForm(
    String[] labels,
    JComponent[] fields
) {

    JPanel form = new JPanel(
        new GridLayout(
            labels.length,
            2,
            12,
            15
        )
    );

    form.setBackground(PANEL);

    form.setBorder(
        new EmptyBorder(
            20,
            20,
            20,
            20
        )
    );

    form.setPreferredSize(
        new Dimension(
            530,
            95 * labels.length
        )
    );

    for (int i = 0; i < labels.length; i++) {

        JLabel label = new JLabel(
            labels[i]
        );

        label.setForeground(TEXT);

        label.setFont(
            new Font(
                "SansSerif",
                Font.BOLD,
                12
            )
        );

        // STYLE TEXT FIELDS

        if (fields[i] instanceof JTextField) {

            JTextField field =
                (JTextField) fields[i];

            field.setBackground(PANEL2);

            field.setForeground(TEXT);

            field.setCaretColor(CYAN);

            field.setFont(
                new Font(
                    "SansSerif",
                    Font.PLAIN,
                    13
                )
            );

            field.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                        new Color(55, 72, 100)
                    ),
                    new EmptyBorder(
                        9,
                        11,
                        9,
                        11
                    )
                )
            );
        }

        form.add(label);
        form.add(fields[i]);
    }

    return form;
}

// ==========================================
// 36. SUCCESS MESSAGE
// ==========================================

private void showMessage(String message) {

    JOptionPane.showMessageDialog(
        this,
        message,
        "INNERBOOK",
        JOptionPane.INFORMATION_MESSAGE
    );
}

// ==========================================
// 37. ERROR MESSAGE
// ==========================================

private void showError(String message) {

    JOptionPane.showMessageDialog(
        this,
        message,
        "INNERBOOK - Error",
        JOptionPane.ERROR_MESSAGE
    );
}
// ==========================================
// LOGIN GUI - MISSING HELPER METHODS
// ==========================================

// 1. CREATE AUTHENTICATION LABEL

private JLabel createAuthLabel(String text) {

    JLabel label = new JLabel(text);

    label.setFont(
        new Font("SansSerif", Font.BOLD, 13)
    );

    label.setForeground(TEXT);

    return label;
}

// 2. CREATE AUTHENTICATION TEXT FIELD

private JTextField createAuthTextField() {

    JTextField field = new JTextField();

    field.setFont(
        new Font("SansSerif", Font.PLAIN, 14)
    );

    field.setForeground(TEXT);
    field.setBackground(PANEL2);
    field.setCaretColor(CYAN);

    field.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(12, 12, 12, 12)
        )
    );

    return field;
}

// 3. STYLE PASSWORD FIELD

private void styleAuthField(JPasswordField field) {

    field.setFont(
        new Font("SansSerif", Font.PLAIN, 14)
    );

    field.setForeground(TEXT);
    field.setBackground(PANEL2);
    field.setCaretColor(CYAN);

    field.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(12, 12, 12, 12)
        )
    );

// 4. END OF LOGIN GUI HELPERS

}

// ==========================================
// END OF PART 6 OF 8
// NEXT: PART 7 - CATEGORIES AND NAVIGATION
// ==========================================

// ==========================================
// PART 7 OF 8
// CATEGORIES AND NAVIGATION
// ==========================================

// ==========================================
// 38. CATEGORY FILTER
// ==========================================

private void showCategories() {

    try {

        List<String> genres = new ArrayList<>(
            service.getGenres()
        );

        if (genres.isEmpty()) {

            showMessage(
                "No categories yet. Add your first book!"
            );

            return;
        }

        String selected = (String)
            JOptionPane.showInputDialog(
                this,
                "Select a book category:",
                "InnerBook Categories",
                JOptionPane.PLAIN_MESSAGE,
                null,
                genres.toArray(),
                genres.get(0)
            );

        if (selected == null) {
            return;
        }

        genreFilter.setSelectedItem(selected);

        refreshBooks();

        setActiveNavigation(booksBtn);

        cardLayout.show(
            contentPanel,
            "books"
        );

    } catch (RuntimeException ex) {

        showError(
            "Unable to load categories:\n"
            + ex.getMessage()
        );
    }
}

// ==========================================
// 39. CHECK IF GENRE EXISTS
// ==========================================

private boolean genreExists(String genre) {

    if (genre == null) {
        return false;
    }

    return service.getGenres().stream()
        .anyMatch(
            g -> g.equalsIgnoreCase(genre)
        );
}

// ==========================================
// 40. NAVIGATION BUTTON
// ==========================================

private JButton navButton(String text) {

    JButton button = new JButton(text);

    button.setFont(
        new Font("SansSerif", Font.BOLD, 13)
    );

    button.setForeground(TEXT);
    button.setBackground(PANEL);

    button.setHorizontalAlignment(
        SwingConstants.LEFT
    );

    button.setPreferredSize(
        new Dimension(205, 47)
    );

    button.setMaximumSize(
        new Dimension(220, 47)
    );

    button.setMinimumSize(
        new Dimension(205, 47)
    );

    button.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );

    button.setBorder(
        new EmptyBorder(
            12, 15, 12, 10
        )
    );

    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setContentAreaFilled(false);

    button.setCursor(
        new Cursor(Cursor.HAND_CURSOR)
    );

    button.addMouseListener(
        new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                if (button != getActiveButton()) {
                    button.setForeground(CYAN);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                if (button != getActiveButton()) {
                    button.setForeground(TEXT);
                }
            }
        }
    );

    return button;
}

// ==========================================
// 41. GET ACTIVE NAVIGATION
// ==========================================

private JButton getActiveButton() {

    JButton[] buttons = {
        dashboardBtn,
        booksBtn,
        uploadBtn,
        categoriesBtn
    };

    for (JButton button : buttons) {

        if (button != null
                && button.getForeground().equals(CYAN)) {

            return button;
        }
    }

    return null;
}

// ==========================================
// 42. SET ACTIVE NAVIGATION
// ==========================================

private void setActiveNavigation(
    JButton selected
) {

    JButton[] buttons = {
        dashboardBtn,
        booksBtn,
        uploadBtn,
        categoriesBtn
    };

    for (JButton button : buttons) {

        if (button == null) {
            continue;
        }

        button.setForeground(
            button == selected ? CYAN : TEXT
        );
    }
}

// ==========================================
// 43. ACCENT BUTTON
// ==========================================

private JButton accentButton(
    String text,
    Color accent
) {

    JButton button = new JButton(text);

    button.setFont(
        new Font("SansSerif", Font.BOLD, 12)
    );

    button.setForeground(BG);
    button.setBackground(accent);

    button.setBorder(
        new EmptyBorder(
            13, 19, 13, 19
        )
    );

    button.setFocusPainted(false);
    button.setBorderPainted(false);

    button.setCursor(
        new Cursor(Cursor.HAND_CURSOR)
    );

    button.setOpaque(true);

    return button;
}

// ==========================================
// 44. ROUNDED PANEL
// ==========================================

private class RoundedPanel extends JPanel {

    private final Color panelColor;
    private final int radius;

    public RoundedPanel(
        Color color,
        int radius
    ) {

        this.panelColor = color;
        this.radius = radius;

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
            (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(panelColor);

        g2.fillRoundRect(
            0,
            0,
            getWidth(),
            getHeight(),
            radius,
            radius
        );

        g2.dispose();

        super.paintComponent(g);
    }
}

// ==========================================
// 45. EXIT APPLICATION
// ==========================================

private void exitSystem() {

    int answer = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to exit InnerBook?",
        "Exit Application",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    if (answer == JOptionPane.YES_OPTION) {
        dispose();
    }
}

// ==========================================
// END OF PART 7 OF 8
// NEXT: PART 8 - FINAL SETUP AND RUNNING
// ==========================================

// ==========================================
// PART 8 OF 8
// FINAL SETUP AND APPLICATION STARTUP
// ==========================================

// ==========================================
// 46. MAIN METHOD
// ==========================================
public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

        try {

            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception ex) {

            System.out.println(
                "Using default interface theme."
            );
        }

        InnerBook app = new InnerBook();
        app.setVisible(true);

    });
}
// ==========================================
// 47. FINAL CLASS CLOSING
// ==========================================

}
