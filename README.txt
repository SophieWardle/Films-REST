# Films Project

## Films MVC

### Dependencies:
- Eclipse 
- Apache Tomcat 9.0 (can be installed through Eclipse)

### Setup Instructions:
1. Import the project to Eclipse.
2. Resolve any server-related errors:
   - Right-click Java Resources or src and go to Build Path.
   - Remove the unbound JDK or server and add the one on your machine.
3. Start the Apache Tomcat server.
4. Run the HomeServlet.java controller; it should open in your browser.

## Films REST

### Dependencies:
- Eclipse 
- Apache Tomcat 9.0 (can be installed through Eclipse)
- Postman 

### Setup Instructions:
1. Import the project to Eclipse.
2. Resolve any server-related errors as explained above.
3. Start the Apache Tomcat server.
4. Open Postman.
5. To make requests, use the following endpoints:
   - GET: http://localhost:8080/Films-REST/FilmREST (Accept headers: text/plain, application/xml, application/json)
   - POST: http://localhost:8080/Films-REST/FilmREST (Content-Type headers: text/plain, application/xml, application/json)
   - PUT: http://localhost:8080/Films-REST/FilmREST (Content-Type headers: text/plain, application/xml, application/json)
   - DELETE: http://localhost:8080/Films-REST/FilmREST (Content-Type headers: text/plain, application/xml, application/json)

## Films Client

### Dependencies:
- Eclipse 
- Apache Tomcat 9.0 (can be installed through Eclipse)

### Setup Instructions:
1. Import the project to Eclipse.
2. Resolve any server-related errors as explained above.
3. Start the Apache Tomcat server.
4. Run the index.html; it should open in your browser.
5. To change the data format of GET and DELETE requests, use the drop-down in the top right.
6. To change data format of POST and PUT requests, use the drop-down in their respective modals.


## DOWNLOADS
- [Download Eclipse](https://www.eclipse.org/downloads/) and make sure to tick Eclipse Enterprise
- [Download Apache Tomcat](https://tomcat.apache.org/download-90.cgi)
- [Download Postman](https://www.postman.com/downloads/)
