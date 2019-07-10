desktop-finance
The desktop version of the Easy Finance web application
The current state of the project:
•	I have the login and register features (more or less) completed, changes could happen of course
•	I have yet to add any styling whatsoever to the GUI, so it has the default look
•	I'm using an SQLite database and the JDBC API with it. The communication between the database and the application is done by stored procedures.
•	I managed to get Maven to work together with the project structure that IntelliJ uses. I needed to add a line of configuration to the Surefire plugin in the POM. It is definitely a relief. Now the project should "play nice" with Travis CI, without having to change its structure by hand.
•	This version is not relying on Spring for dependency management. I will create a Spring version to see if its better that way.
Changelog (28/02/2019)
•	Made a number of changes, mostly refactoring existing code.
•	Made some progress on the summary tab of the main page. It's not complete yet, though.
•	Attempted to create a Spring Boot version, which has failed. Apparently the Java 11 + Java FX combination does not go well with it.
•	Attempted to create a JPA Hibernate version, which has failed. Hibernate with SQLite is not a good combination by the looks of it.
•	Decided to keep going with my custom setup. The other JPA implementations had much overhead and little functionality to offer in return. Spring without Spring Boot takes a lot of configuration. Overall, I believe that the current design is simpler than the alternatives.
•	I should really start using Git. Removing redundant stuff on GitHub takes a lot of time and effort.
Changelog (10/07/2019)
It has been quite a while since my last update. I do apologise for that. My two excuses for such a delay are my new (full-time) job, where I have started in March this year, and the fact that I have gotten way too carried away with making changes. 
So, what has changed during this lengthy period?
It is too hard to tally up every single change I have made, but here is a brief description:
•	The history page is now complete, but as always, changes may happen
•	I have made quite some progress regarding the replacement of inheritance with object composition wherever possible. My end goal is to make all of my objects as modular as possible, ideally making it possible to “assemble” new objects using existing interfaces and classes
•	I have added a load of UML diagrams to the project. They represent my understanding of the current state of the application. You can find them in the “Desktop Finance overview” file. I have used StarUML to create them, so you may need it to view these diagrams. It is free to evaluate indefinitely, which is nice.
•	As always, I have added tests for the new features.
Okay, with this out of the way, what are the next steps?
There are a great many issues I would like to address in the next few days/weeks/months. Of course, the main thing is to keep adding in the features from Easy Finance, but here some things I have noted about the program:
•	The error-handling is quite lacking throughout the app aside from a few cases (such as the error-handling around retrieving records). I need to come up with a coherent error handling strategy. I’m thinking about introducing an error logging system at the moment.
•	The model objects do not pull their weight. I originally intended to keep these objects as simple as possible, but I am starting to think that I may have misinterpreted Uncle Bob Martin’s words regarding objects and data structures. I will try a new approach, one where these classes take on some more responsibility, and see how it will turn out.
•	Connected to this earlier point, I have come to the conclusion that the object structure is overly fragmented. I have too many small classes (in the utility package and perhaps one or two in services) which could be merged with others (such as the models). Yet again, I will take steps to change this and we will see how that works.
•	Controllers will most likely need to be separated from their current functionality. I have not done a lot on the front-end yet, but I will get around to that eventually. This means that the controllers will not be able to handle their current responsibilities without running the risk of becoming too big and complex to maintain. I’m looking forward to that time because it promises to be a challenging problem to solve.
•	I have many problems with my test cases. The main issue is that they take far too long to run. If at all possible, I would like to decrease the number of GUI tests to address this issue. They will also need refactoring. I have done what I could to them in the time I had to spend on them, but the work is far from done. Finally, I have yet to map these classes out with UML.
•	I also have an issue with getting Travis CI to work with some of my tests. The errors indicate that the configuration I use cannot handle popup windows (of which I use two in the application currently). I will need to do some research to find a solution to this problem. This is my top priority for the moment.
