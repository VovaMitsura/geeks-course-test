# Math Equation Application

This project is a test task developed for GeeksForLess. It's a mathematical equation application that assists mathematics teachers in various tasks related to equations and their roots.

## Requirements

The application should provide the following features:

1. Input mathematical equations containing numbers (integers or decimal fractions), as well as mathematical operations (+, -, *, /) and parentheses. The nesting level of parentheses can be arbitrary. The unknown variable is denoted by the English letter 'x'.

2. Validate the correct placement of parentheses in the entered equations.

3. Check the correctness of the entered expressions. For example, expressions like "3+*4" should be considered incorrect, while "4*-7" is considered valid.

4. Store correctly formatted equations in a database (or files, if chosen).

5. Allow users to input equation roots. Validate whether the given number is a root of the equation. If it is, store it in the database.

6. Implement functions to search for equations in the database based on their roots. For example, search for all equations that have a specific root or find equations with exactly one root.

7. Develop the project using Maven as the build system in IntelliJ IDEA or Eclipse.

8. Upload the project to a GitHub repository and provide a link for access. Alternatively, you can send an archive with the project.

## Notes

- The project should be developed using Java 11 or 17.
- It's recommended to use databases such as MySQL, MariaDB, or PostgreSQL.
- Using JUnit and other testing tools for testing is encouraged.
- File storage can be used instead of databases for storing information about entered and edited equations.
- A number is considered a root of an equation if substituting that number for all occurrences of 'x' in the equation results in a difference between the values of the left and right sides of the equation not exceeding 10^-9.

---

#### How to Run manually

This application is packaged as a jar which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary.</br> You run it using the ```java -jar``` command.

* Clone this repository
* Make sure you are using JDK 1.17 and Maven 3.x
* Make sure you are using MySQL 8.0
* Create Mysql database
``` 
create database "your_database_name"
```
*  Change mysql username, password and datasource as per your installation
    - open `src/main/resources/application.properties`
    - change `spring.datasource.username` , `spring.datasource.password` and `spring.datasource.url` as per your mysql installation

* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the project by this method:
```
java -jar target/interview-planning-0.0.1-SNAPSHOT.jar
```
The app will start running at http://localhost:8080.
