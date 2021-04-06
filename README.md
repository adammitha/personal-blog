# Personal Blog

## An application to write and manage your personal blog or diary

The purpose of this application is to give users a simple interface for 
writing and organizing their private thoughts without needing to store them remotely
or share them with the world. The application will allow users to write entries
formatted using Markdown syntax. They will also be able to categorize the posts, 
tag them with various topics, and search through them based on their titles, tags
or content. 


## User stories - Phase 1
- As a user, I want to be able to write and save a blog post
- As a user, I want to be able to view a list of blog posts that I have written
- As a user, I want to be able to edit a blog post that already exists
- As a user, I want to be able to tag a blog post with one or more topics 
  or categories
  
## User stories - Phase 2
- As a user, I want to save my blog posts to a file
- As a user, I want to retrieve the blog posts that I wrote previously to read and edit them

## Phase 4: Task 2
The Blog class in the model package is robust. In particular, the findArticleById() method throws a 
NoSuchElementException if the function is invoked with the id of an article that does not exist in the blog. 
There are two test cases in the BlogTest class - one where we would expect an exception to be thrown, and one
where no exception should be thrown.

## Phase 4: Task 3
Both the BlogGui and Blog Cli classes have a Blog, JsonWriter, and JsonReader field. This repetition could be eliminated
by creating a shared abstract class or superclass that both the BlogGui and BlogCli classes inherit 
from that contains the shared fields. This would ensure that the BlogGui and BlogCli classes focus
on implementation details that are specific to the graphical or command line interfaces (e.g. reading input from 
stdin, or rendering images to the screen).

I would also like to give the end user the option to decide which user interface they would like to use. Currently, 
I have to manually modify the Main class to instantiate either the BlogGui or BlogCli. It would be nice to provide a
simple prompt on start-up to give the user a choice without having to modify the source code.