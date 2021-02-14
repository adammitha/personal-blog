package ui;

import model.*;

import java.time.LocalDate;
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
        System.out.println("\te -> edit an existing article");
        System.out.println("\tq -> quit");
    }

    private void parseCommand(String command) {
        switch (command) {
            case "n":
                createNewArticle();
                break;
            case "l":
                listArticles();
                break;
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

        System.out.println("Title:");
        String title = readNonEmptyString();

        System.out.println("Author:");
        String author = readNonEmptyString();

        System.out.println("Content:");
        String content = readNonEmptyString();

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

    // EFFECTS: Reads from stdin and returns result if it is nonempty
    private String readNonEmptyString() {
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

    private void editArticle() {
        System.out.println("Create a new article");
    }
}
