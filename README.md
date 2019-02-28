# desktop-finance
The desktop version of the Easy Finance web application

The current state of the project:
- I have the login and register features (more or less) completed, changes could happen
of course
- I have yet to add any styling whatsoever to the GUI, so it has the default look
- I'm using an SQLite database and the JDBC API with it. The communication between
the database and the application is done by stored procedures.
- I managed to get Maven to work together with the project structure that IntelliJ uses.
I needed to add a line of configuration to the Surefire plugin in the POM. It is
definitely a relief. Now the project should "play nice" with Travis CI, without having
to change its structure by hand.
- This version is not relying on Spring for dependency management. I will create a Spring
version to see if its better that way.

# Changelog (28/02/2019)

- Made a number of changes, mostly refactoring existing code.
- Made some progress on the summary tab of the main page. It's not complete yet, though.
- Attempted to create a Spring Boot version, which has failed. Apparently the Java 11 + Java FX
combination does not go well with it.
- Attempted to create a JPA Hibernate version, which has failed. Hibernate with SQLite is not
a good combination by the looks of it.
- Decided to keep going with my custom setup. The other JPA implementations had much overhead and
little functionality to offer in return. Spring without Spring Boot takes a lot of configuration.
Overall, I believe that the current design is simpler than the alternatives.
- I should really start using Git. Removing redundant stuff on GitHub takes a lot of time and effort.
