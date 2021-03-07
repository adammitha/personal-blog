package ui;

import jdk.nashorn.internal.ir.debug.JSONWriter;
import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

// User interface logic for a Blog application that runs on the command line
public class BlogApp {
    private static final String JSON_STORE = "./data/blog.json";
    private Blog blog;
    private Scanner input;
    JsonWriter writer;
    JsonReader reader;

    // EFFECTS: runs the blog application
    public BlogApp() {
        writer = new JsonWriter(JSON_STORE);
        reader = new JsonReader(JSON_STORE);
        runBlog();
    }

    // MODIFIES: this
    // EFFECTS: processes user input and responds
    // NB: structure of this method is based on the runTeller() function from the Teller App
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
                parseMainMenuCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Asks user if they want to load their existing blog, or create a new one
    private void initBlog() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        System.out.println("Welcome to the BloggingApp!\nPlease select an option:");
        System.out.println("\tn -> create a new blog");
        System.out.println("\tl -> load your existing blog");
        String command = input.next();
        command = command.toLowerCase();
        parseInitCommand(command);
    }

    // MODIFIES: this
    // EFFECTS: creates a new blog with the name provided by the user
    private void createNewBlog() {
        System.out.println("Welcome to the BloggingApp!\nPlease enter the name of your blog:");
        String title = input.next();
        blog = new Blog(title);
    }

    // MODIFIES: this
    // EFFECTS: loads existing blog from file
    private void loadBlog() {
        try {
            blog = reader.read();
            System.out.println(String.format("Successfully loaded \"%s\" from %s", blog.getTitle(), JSON_STORE));
        } catch (IOException e) {
            System.out.println("Unable to read from file " + JSON_STORE);
        }
    }

    // EFFECTS: saves blog to file
    private void saveBlog() {
        try {
            writer.open();
            writer.write(blog);
            writer.close();
            System.out.println(String.format("Saved %s to %s", blog.getTitle(), JSON_STORE));
        } catch (IOException e) {
            System.out.println("Unable to save blog to " + JSON_STORE);
        }
    }

    // EFFECTS: displays menu to user
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tn -> write new article");
        System.out.println("\tl -> list existing articles");
        System.out.println("\tr -> read an existing article");
        System.out.println("\te -> edit an existing article");
        System.out.println("\tt -> tag an existing article");
        System.out.println("\ts -> save your blog to file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: evaluates command entered at the init menu and calls relevant function
    private void parseInitCommand(String command) {
        switch (command) {
            case "n":
                createNewBlog();
                break;
            case "l":
                loadBlog();
                break;
            default:
                System.out.println("I don't recognize that command. Please try again.");
        }
    }

    // EFFECTS: evaluates the command entered by the user and calls the corresponding function
    private void parseMainMenuCommand(String command) {
        switch (command) {
            case "n":
                createNewArticle();
                break;
            case "l":
                listArticles();
                break;
            case "r":
                readArticle();
                break;
            case "e":
                editArticle();
                break;
            case "t":
                tagArticle();
                break;
            case "s":
                saveBlog();
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
            String tagString = article.getTags().stream().map(Tag::getName)
                    .collect(Collectors.joining(", "));
            System.out.println(article.getId() + ": " + article.toString());
            if (article.getTags().size() > 0) {
                System.out.println("\tTags: " + tagString);
            }
        }
    }

    // EFFECTS: prints the article to the screen for the user to read
    private void readArticle() {
        System.out.println("Which article would you like to read?");
        Article articleToRead = findArticle();
        if (articleToRead == null) {
            return;
        }
        System.out.println(articleToRead.toString());
        System.out.println(articleToRead.getContent());
        System.out.print("\n");
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

        String newAuthor = getInputOrDefault(
                "Please enter a new author (leave blank if you don't want to change it): ",
                articleToEdit.getAuthor()
        );

        String newContent = getInputOrDefault(
                "Please enter new content (leave blank if you don't want to change it): ",
                articleToEdit.getContent()
        );

        articleToEdit.edit(
                newTitle,
                newAuthor,
                newContent
        );
    }

    // MODIFIES: this
    // EFFECTS: prompts the user for the article to tag and the name of the tag they would like to add
    private void tagArticle() {
        System.out.println("Which article would you like to tag?");
        Article articleToTag = findArticle();

        if (articleToTag == null) {
            return;
        }

        boolean addedValidTag = false;

        while (!addedValidTag) {
            String tagName = readNonEmptyString("Please enter the name of your tag: ");

            Tag newTag = new Tag(tagName);

            addedValidTag = articleToTag.addTag(newTag);

            if (!addedValidTag) {
                System.out.println("Please create a tag with a unique name");
            }
        }
    }
}
