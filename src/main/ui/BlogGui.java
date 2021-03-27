package ui;

import model.Article;
import model.Blog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

// The BlogGui class is based on the following Notes app:
// http://www.javaquizplayer.com/examples/notesapp-using-swing-h2database-JPA-example.html
// BlogGui is a graphical user interface for the BlogApp
public class BlogGui extends JPanel {
    private static final String JSON_STORE = "./data/blog.json";
    private static final Font FONT_FOR_WIDGETS =
            new Font("SansSerif", Font.PLAIN, 16);
    private static final Font FONT_FOR_EDITOR =
            new Font("Comic Sans MS", Font.PLAIN, 16);
    private Blog blog;
    private JFrame frame;
    private JList<Article> list;
    private JTextField title;
    private JTextField author;
    private JTextArea content;
    private JButton newButton;
//    private JButton deleteButton;
    private JButton saveButton;
    private JButton loadButton;
//    private JButton cancelButton;
    private JLabel messageLabel;
    private boolean updateFlag;
    private JsonReader reader;
    private JsonWriter writer;

    enum MessageType { INFO, WARN, NONE }

    enum ActionType { LOAD, CREATE, UPDATE, DELETE }

    // EFFECTS: Starts the Blog GUI
    public BlogGui() {
        reader = new JsonReader(JSON_STORE);
        writer = new JsonWriter(JSON_STORE);
        blog = new Blog("My blog");

        frame = new JFrame("BlogApp");
        addWidgetsToFrame(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(350, 50);
        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true);

    }

    // MODIFIES: this
    // EFFECTS: adds UI components to application window
    private void addWidgetsToFrame(JFrame frame) {
        Container pane = frame.getContentPane();
        pane.setLayout(new GridBagLayout());

        pane.add(getListInScrollPane(), getConstraintsForList());
        pane.add(getTitleField(), getConstraintsForTitle());
        pane.add(getAuthorField(), getConstraintsForAuthor());
        pane.add(getContentInScrollPane(), getConstraintsForContent());
        pane.add(getToolBarWithButtons(), getConstraintsForButtonToolBar());
        pane.add(getMessageLabel(), getConstraintsForMessageLabel());
    }

    // MODIFIES: this
    // EFFECTS: constructs and returns the list of articles
    private JScrollPane getListInScrollPane() {
        list = new JList<Article>(new ArticleListModel(blog.getArticles()));
        list.setToolTipText("Articles: double click an Article to edit");
        list.setBorder(new EmptyBorder(5, 5, 5, 5));
        list.setFixedCellHeight(26);
        list.setFont(FONT_FOR_WIDGETS);
        list.addMouseListener(new ListMouseListener());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectListener());
        JScrollPane scroller = new JScrollPane(list);
        scroller.setPreferredSize(new Dimension(250, 350));
        scroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        return scroller;
    }

    // MODIFIES: this
    // EFFECTS: constructs and returns the title field
    private JTextField getTitleField() {
        title = new JTextField("", 30);
        title.setToolTipText("Article title must be unique and no more than 30 characters");
        title.setFont(FONT_FOR_WIDGETS);
        title.setEditable(false);
        title.setMargin(new Insets(5, 5, 5, 5));
        return title;
    }

    // MODIFIES: this
    // EFFECTS: constructs and returns the author field
    private JTextField getAuthorField() {
        author = new JTextField("", 30);
        author.setToolTipText("Author must be nonempty");
        author.setFont(FONT_FOR_WIDGETS);
        author.setEditable(false);
        author.setMargin(new Insets(5, 5, 5, 5));
        return author;
    }

    // MODIFIES: this
    // EFFECTS: constructs and returns the content field
    private JScrollPane getContentInScrollPane() {
        content = new JTextArea("");
        content.setToolTipText("Content");
        content.setFont(FONT_FOR_EDITOR);
        content.setEditable(false);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setMargin(new Insets(5, 5, 5, 5)); // top, left, bottom, rt

        JScrollPane scroller = new JScrollPane(content);
        scroller.setPreferredSize(new Dimension(550, 0)); // wxh
        scroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scroller;
    }

    // MODIFIES: this
    // EFFECTS: Returns toolbar populated with buttons
    private JToolBar getToolBarWithButtons() {

        newButton = getButton("New");
        saveButton = getButton("Save");
        loadButton = getButton("Load");
//        deleteButton = getButton("Delete");
//        cancelButton = getButton("Cancel");

        JToolBar toolBar = getToolBarForButtons();
        toolBar.add(newButton);
        toolBar.addSeparator(new Dimension(2, 0));
        toolBar.add(saveButton);
        toolBar.addSeparator(new Dimension(2, 0));
        toolBar.add(loadButton);
//        toolBar.add(deleteButton);
//        toolBar.addSeparator(new Dimension(2, 0));
//        toolBar.add(cancelButton);
//        toolBar.addSeparator(new Dimension(5, 0));
//        toolBar.add(getButton("Help"));

        return toolBar;
    }

    // MODIFIES: this
    // EFFECTS: instantiates and returns toolbar
    private JToolBar getToolBarForButtons() {

        JToolBar toolBar = new JToolBar();
        toolBar.setBorderPainted(false);
        toolBar.setFloatable(false);
        return toolBar;
    }

    // EFFECTS: instantiates button
    private JButton getButton(String label) {

        JButton button = new JButton();
        button.setBorderPainted(false);

        switch (label) {
            case "New":
                button.setIcon(getIconForButton("new24.gif"));
                button.addActionListener(new NewActionListener());
                break;
            case "Save":
                button.setEnabled(false);
                button.setIcon(getIconForButton("save24.gif"));
                button.addActionListener(new SaveActionListener());
                break;
            case "Load":
                button.setEnabled(true);
                button.setIcon(getIconForButton("open24.gif"));
                button.addActionListener(new LoadActionListener());
                break;
            default:
                throw new IllegalArgumentException("*** Invalid button label ***");
        }

        button.setToolTipText(label);
        return button;
    }

    // EFFECTS: Retrieves icon for button
    private ImageIcon getIconForButton(String iconName) {

        String urlString = "./img/" + iconName;
        return new ImageIcon(urlString);
    }

    // MODIFIES: this
    // EFFECTS: instantiates message label and configures font
    private JLabel getMessageLabel() {

        messageLabel = new JLabel("");
        messageLabel.setFont(FONT_FOR_WIDGETS);
        return messageLabel;
    }

    // MODIFIES: this
    // EFFECTS: renders message
    private void displayMessage(String msg, MessageType type) {

        if (type == MessageType.WARN) {

            messageLabel.setText("<html><font color=red>" + msg + "</font></html>");
        } else if (type == MessageType.INFO) {

            messageLabel.setText("<html><font color=blue>" + msg + "</font></html>");
        } else {
            messageLabel.setText("");
        }
    }

    // EFFECTS: returns geometric constraints for list of blog posts
    private GridBagConstraints getConstraintsForList() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 3;
        c.insets = new Insets(12, 12, 11, 11);
        return c;
    }

    // EFFECTS: returns geometric constraints for title field
    private GridBagConstraints getConstraintsForTitle() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(12, 0, 11, 11);
        return c;
    }

    // EFFECTS: returns geometric constrains for author field
    private GridBagConstraints getConstraintsForAuthor() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 11, 12);
        return c;
    }

    // EFFECTS: returns geometric constraints for content field
    private GridBagConstraints getConstraintsForContent() {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 11, 11);
        return c;
    }

    // EFFECTS: returns geometric constraints for toolbar
    private GridBagConstraints getConstraintsForButtonToolBar() {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 12, 11, 11);
        return c;
    }

    // EFFECTS: returns geometric constraints for message label
    private GridBagConstraints getConstraintsForMessageLabel() {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 11, 11, 11);
        c.anchor = GridBagConstraints.WEST;
        return c;
    }

    // ListSelectListener listens for selection events in the list of articles
    private class ListSelectListener implements ListSelectionListener {

        // MODIFIES: this
        // EFFECTS: Handles list selection event
        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (e.getValueIsAdjusting()) {

                return;
            }

            if (getArticleListModel().isEmpty()) {

//                deleteButton.setEnabled(false);
                title.setText("");
                author.setText("");
                content.setText("");
            } else {
//                deleteButton.setEnabled(true);
                Article a = list.getSelectedValue();
                title.setText(a.getTitle());
                author.setText(a.getAuthor());
                content.setText(a.getContent());
                content.setCaretPosition(0);
            }

            displayMessage("", MessageType.NONE);
            title.setEditable(false);
            author.setEditable(false);
            content.setEditable(false);
            newButton.setEnabled(true);
//            saveButton.setEnabled(false);
//            cancelButton.setEnabled(false);
            updateFlag = false;
        }
    }

    // ListMouseListener processes MouseEvents
    private class ListMouseListener extends MouseAdapter {

        // MODIFIES: this
        // EFFECTS: Processes mouse click event in list of articles
        @Override
        public void mouseClicked(MouseEvent e) {

            if ((e.getButton() == MouseEvent.BUTTON1)
                    && (e.getClickCount() == 2)) { // double click on left button

                Rectangle r = list.getCellBounds(0,
                        list.getLastVisibleIndex());

                if ((r == null) || (! r.contains(e.getPoint()))) {

                    // Double clicked on empty space
                    list.requestFocusInWindow();
                    return;
                }

                doubleClickActionRoutine();
            }
        }
    }

    // NewActionListener handles creation of new articles
    private class NewActionListener implements ActionListener {

        // MODIFIES: BlogGui
        // EFFECTS: Renders the new Article UI
        @Override
        public void actionPerformed(ActionEvent e) {

            newButton.setEnabled(false);
            saveButton.setEnabled(true);
//            deleteButton.setEnabled(false);
//            cancelButton.setEnabled(true);
            title.setEditable(true);
            author.setEditable(true);
            content.setEditable(true);
            title.setText("");
            author.setText("");
            content.setText("");
            displayMessage("Write a new Blog post", MessageType.INFO);
            title.requestFocusInWindow();
        }
    }

    // LoadActionListener handles loading a blog from file
    private class LoadActionListener implements ActionListener {

        // MODIFIES: BlogGui
        // EFFECTS: Loads blog from file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                blog = reader.read();
            } catch (IOException exception) {
                alertUserToException(ActionType.LOAD);
            }
            getArticleListModel().setArticles(blog.getArticles());
            list.updateUI();
            displayMessage("Blog loaded from file", MessageType.INFO);
            loadButton.setEnabled(false);
        }
    }

    // SaveActionListener handles saving the blog to file
    private class SaveActionListener implements ActionListener {

        // MODIFIES: BlogGui
        // EFFECTS: Saves new/updated article and persists to file
        @Override
        public void actionPerformed(ActionEvent e) {
            String titleText = title.getText();
            String authorText = author.getText();
            String contentText = content.getText();

            titleText = (titleText == null) ? "" : titleText.trim();

            int i = 0;

            if (updateFlag) {
                // Update article
                Article originalArticle = list.getSelectedValue();
                originalArticle.edit(titleText, authorText, contentText);
                persistBlog(ActionType.UPDATE);
                i = getArticleListModel().indexOf(originalArticle);
            } else {
                Article newArticle = new Article(
                        Article.getNextId(), titleText, authorText, contentText, LocalDate.now()
                );
                getArticleListModel().add(newArticle);
                persistBlog(ActionType.CREATE);
                i = getArticleListModel().indexOf(newArticle);
            }

            updateListAfterSave(i);
        }

        // MODIFIES: BlogGui
        // EFFECTS: updates list of articles with newly added/edited article
        private void updateListAfterSave(int i) {
            list.updateUI();


            list.setSelectedIndex(i);
            list.ensureIndexIsVisible(i);

            title.setEditable(false);
            author.setEditable(false);
            content.setEditable(false);
            newButton.setEnabled(true);
            saveButton.setEnabled(false);
            updateFlag = false;
            displayMessage("Blog is saved", MessageType.INFO);
        }
    }

    // MODIFIES: this
    // EFFECTS: helper function to update UI when user double clicks an article
    private void doubleClickActionRoutine() {

        displayMessage("Note is being edited", MessageType.INFO);
        newButton.setEnabled(false);
        saveButton.setEnabled(true);
//        deleteButton.setEnabled(false);
//        cancelButton.setEnabled(true);
        updateFlag = true;
        title.setEditable(true);
        author.setEditable(true);
        content.setEditable(true);
        title.setCaretPosition(0);
        title.requestFocusInWindow();
    }

    // MODIFIES: this
    // EFFECTS: persists updated blog state to file
    private void persistBlog(ActionType action) {
        try {
            blog.setArticles(getArticleListModel().getArticles());
            writer.open();
            writer.write(blog);
            writer.close();
        } catch (FileNotFoundException e) {
            alertUserToException(action);
        }
    }

    // EFFECTS: displays error message to user
    private void alertUserToException(ActionType action) {
        String s = "";
        switch (action) {
            case CREATE: s = " creating ";
                break;
            case UPDATE: s = " updating ";
                break;
            case DELETE: s = " deleting ";
                break;
            case LOAD: s = "loading ";
                break;
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }

        String msg = "<html><center>"
                + "An database exception has occurred<br>"
                + "while " + s + " the Article(s). Please check<br>"
                + "the log to view the technical details.<br> Exiting the app.";

        JOptionPane.showMessageDialog(frame, new JLabel(msg), "Error", JOptionPane.PLAIN_MESSAGE);

        System.exit(1);
    }

    // EFFECTS: Retrieves ArticleListModel
    private ArticleListModel getArticleListModel() {
        return (ArticleListModel) list.getModel();
    }
}
