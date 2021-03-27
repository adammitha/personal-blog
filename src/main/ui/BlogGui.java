package ui;

import model.Article;
import model.Blog;
import persistence.JsonReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
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
    private JTextArea content;
    private JButton newButton;
//    private JButton deleteButton;
    private JButton saveButton;
//    private JButton cancelButton;
    private JLabel messageLabel;
    private boolean updateFlag;
    private JsonReader reader;

    enum MessageType { INFO, WARN, NONE }

    enum ActionType { LOAD, CREATE, UPDATE, DELETE }

    // EFFECTS: Starts the Blog GUI
    public BlogGui() {
        reader = new JsonReader(JSON_STORE);
        try {
            blog = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame = new JFrame("BlogApp");
        addWidgetsToFrame(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(350, 50);
        frame.setResizable(false);
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
        pane.add(getContentInScrollPane(), getConstraintsForContent());
        pane.add(getToolBarWithButtons(), getConstraintsForButtonToolBar());
        pane.add(getMessageLabel(), getConstraintsForMessageLabel());
    }

    private ArticleListModel getArticleListModel() {
        return (ArticleListModel) list.getModel();
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
//        deleteButton = getButton("Delete");
//        cancelButton = getButton("Cancel");

        JToolBar toolBar = getToolBarForButtons();
        toolBar.add(newButton);
        toolBar.addSeparator(new Dimension(2, 0));
        toolBar.add(saveButton);
//        toolBar.addSeparator(new Dimension(2, 0));
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
                button.setIcon(getIconForButton("New24.gif"));
                button.addActionListener(new NewActionListener());
                break;
            case "Save":
                button.setEnabled(false);
                button.setIcon(getIconForButton("Save24.gif"));
                button.addActionListener(new SaveActionListener());
                break;
            default:
                throw new IllegalArgumentException("*** Invalid button label ***");
        }

        button.setToolTipText(label);
        return button;
    }

    // EFFECTS: Retrieves icon for button
    private ImageIcon getIconForButton(String iconName) {

        String urlString = "/toolbarButtonGraphics/general/" + iconName;
        URL url = this.getClass().getResource(urlString);
        return new ImageIcon(url);
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
        c.gridheight = 2;
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

    // EFFECTS: returns geometric constraints for content field
    private GridBagConstraints getConstraintsForContent() {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 11, 11);
        return c;
    }

    // EFFECTS: returns geometric constraints for toolbar
    private GridBagConstraints getConstraintsForButtonToolBar() {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 12, 11, 11);
        return c;
    }

    // EFFECTS: returns geometric constraints for message label
    private GridBagConstraints getConstraintsForMessageLabel() {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
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
                content.setText("");
            } else {
//                deleteButton.setEnabled(true);
                Article a = list.getSelectedValue();
                title.setText(a.getTitle());
                content.setText(a.getContent());
                content.setCaretPosition(0);
            }

            displayMessage("", MessageType.NONE);
            title.setEditable(false);
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

        // MODIFIES: this
        // EFFECTS: Renders the new Article UI
        @Override
        public void actionPerformed(ActionEvent e) {

            newButton.setEnabled(false);
            saveButton.setEnabled(true);
//            deleteButton.setEnabled(false);
//            cancelButton.setEnabled(true);
            title.setEditable(true);
            content.setEditable(true);
            title.setText("");
            content.setText("");
            displayMessage("Write a new Blog post", MessageType.INFO);
            title.requestFocusInWindow();
        }
    }

    private class SaveActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String titleText = title.getText();
            String contentText = content.getText();

            titleText = (titleText == null) ? "" : titleText.trim();

            Article newArticle = new Article(Article.getNextId(), titleText, "<Author goes here>", contentText, LocalDate.now());
            Article originalArticle = list.getSelectedValue();

        }
    }

    // MODIFIES: this
    // EFFECTS: helper function to update UI when user double clicks an article
    private void doubleClickActionRoutine() {

        displayMessage("Note is being edited", MessageType.INFO);
        newButton.setEnabled(false);
//        saveButton.setEnabled(true);
//        deleteButton.setEnabled(false);
//        cancelButton.setEnabled(true);
        updateFlag = true;
        title.setEditable(true);
        content.setEditable(true);
        title.setCaretPosition(0);
        title.requestFocusInWindow();
    }
}
