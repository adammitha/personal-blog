package ui;

import model.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BlogApp {
    private Blog blog;
    private Scanner input;

    // EFFECTS: runs the blog application
    public BlogApp() {
        runBlog();
    }

    // MODIFIES: this
    // EFFECTS: processes user input and responds
    private void runBlog() {
        boolean keepGoing = true;
        String command = null;

        initBlog();

        while (keepGoing) {
            displayMainMenu();

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                parseCommand(command);
            }

        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new blog with the name provided by the user
    private void initBlog() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        System.out.println("Welcome to the BloggingApp!\nPlease enter the name of your blog:");
        String title = input.next();
        title = title.toLowerCase();
        blog = new Blog(title);
    }

    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tn -> write new article");
        System.out.println("\tl -> list existing articles");
        System.out.println("\tr -> read an existing article");
        System.out.println("\te -> edit an existing article");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: evaluates the command entered by the user and calls the appropriate function
    private void parseCommand(String command) {
        switch (command) {
            case "n":
                createNewArticle();
                break;
            case "l":
                listArticles();
                break;
            case "r":
                readArticle();
            case "e":
                editArticle();
                break;
            default:
                System.out.println("I don't recognize that command. Please try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates a new blog article by collecting input from the user
    private void createNewArticle() {
        System.out.println("Create a new article");

        String title = readNonEmptyString("Title:");

        String author = readNonEmptyString("Author:");

        String content = readNonEmptyString("Content:");

        Article newArticle = new Article(
                Article.getNextId(),
                title,
                author,
                content,
                LocalDate.now()
        );

        blog.addArticle(newArticle);

        System.out.println("Successfully created new article:");
        System.out.println(newArticle.toString());
    }

    // REQUIRES: promptMessage is a nonempty string that describes the input wanted from the user
    // EFFECTS: Reads from stdin and returns result if it is nonempty
    private String readNonEmptyString(String promptMessage) {
        System.out.println(promptMessage);
        String in = "";
        while (in.equals("")) {
            in = input.next();
        }
        return in;
    }

    // EFFECTS: Prints out list of articles
    private void listArticles() {
        for (Article article : blog.getArticles()) {
            System.out.println(article.getId() + ": " + article.toString());
        }
    }

    // EFFECTS: prints the article to the screen for the user to read
    private void readArticle() {
        System.out.println("Which article would you like to read?");
        listArticles();
        Article articleToRead = findArticle();
        System.out.println(articleToRead.toString());
        System.out.println(articleToRead.getContent());
    }

    // EFFECTS: Displays list of articles and asks user which one they would like to edit
    //          Returns article if found, or displays prompt again. User can type 'q' to return
    //          to main menu.
    private Article findArticle() {
        listArticles();

        Article article = null;
        while (article == null) {

            String id = readNonEmptyString(
                    "Enter the id of the article or type q to return to the main menu:"
            );

            if (id.equals("q")) {
                return null;
            }

            try {
                article = blog.findArticleById(Integer.parseInt(id));
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
            }
        }

        return article;
    }

    // REQUIRES: promptMessage and defaultValue are nonempty
    // EFFECTS: Prompts user for input and returns the value. Returns defaultValue if the input is empty.
    private String getInputOrDefault(String promptMessage, String defaultValue) {
        System.out.println(promptMessage);
        String in = input.next();
        if (in.equals("")) {
            return defaultValue;
        }
        return in;
    }

    // MODIFIES: this
    // EFFECTS: prompts the user for a new title and content for the article they want to edit
    private void editArticle() {
        System.out.println("Which article would you like to edit?");
        Article articleToEdit = findArticle();

        if (articleToEdit == null) {
            return;
        }

        String newTitle = getInputOrDefault(
                "Please enter a new title (leave blank if you don't want to change it): ",
                articleToEdit.getTitle()
        );

        String newContent = getInputOrDefault(
                "Please enter new content (leave blank if you don't want to change it): ",
                articleToEdit.getContent()
        );

        articleToEdit.edit(
                newTitle,
                newContent
        );

    }
}
