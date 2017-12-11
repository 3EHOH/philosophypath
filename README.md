Philosophy Path

- This is a Spring Boot REST service that traverses the first link on a Wikipedia article that reaches another Wikipedia article, until it finds the article on "Philosophy".

How it works:

- A user can issue a query as such: `localhost:8080/philosophypath?title=Yellowhammer`

- The REST service first checks if a record for `Yellowhammer` already exists in a MongoDB database called `philosophypath` and a collection within that also called `philosophypath`.

- Records are stored as `{id: <firstArticleTitle>, articlePath: <stringifiedArrayOfArticleTitles>}`

- If a record is found, it is returned view the /philosophypath /GET endpoint

- If a record isn't found, the restful service uses a recursive search algorithm, as well as JSoup, a Java library for parsing HTML, which helps us filter out red herring links.

- It also writes that newly-retrieved record to our MongoDB collection, so that if we re-query, it's already saved.

Run:

- from the command line, run using gradle: `./gradlew bootRun`

Install:

- MongoDB


What's Next:

Back End:

- Create a PhilosophyPathDTO class to better abstract the interactions between the REST service and the database

- Consider alternative data schemas. Storing the paths as stringified lists works, but serializing would be a more robust alternative. An entirely different database that's actually design for DAGs, such as Neo4j, would be the ideal way to store the information.


Front End:

- Break the front end out of index.html into individual module files

- Add name tags to the D3 directed graph, so one can actually see the trip from X to Philosophy

Misc:

- Create a more robust failure system. Currently, the program simply throws an exception, which isn't clear to the end user whether the program failed or a path wasn't found.

- Create a more robust query system. The simplest thing to do would be to ensure that if a user enters a query with spaces, they're replaced by underscores, as per the standard Wiki article path.


Known Issues:

- Currently, the program only searches `.mw-content-ltr p a[href^=\"/wiki/\"]`, which gets `<a href>`'s from `<p>`'s within the `.mw-content-ltr` class. Certain list articles that lack a `<p>` tag may be missed.

- The D3 directed graph doesn't re-render properly when a new query is entered.