# Math Equation Application

This project is a test task developed for GeeksForLess. It's a mathematical equation application that assists mathematics teachers in various tasks related to equations and their roots.

## Requirements

The application should provide the following features:

1. Вводити математичні рівняння, що містять числа (цілі, або десяткові дроби), а також математичні операції +, -, *, / та круглі дужки, рівень вкладеності дужок – довільний. У всіх рівняннях невідома величина позначається англійською літерою
x.

2. Перевіряти введене рівняння на коректність розміщення дужок.

3. Перевіряти коректність введеного виразу (не повинно бути 2 знаків математичних операцій поспіль, наприклад, неприпустимий вираз 3+*4, в той же час, вираз 4*-7 є допустимим). Приклади коректних рівнянь:
2*x+5=17, -1.3*5/x=1.2, 2*x=10, 2*x+5+х+5=10, 17=2*x+5

4. Якщо рівняння є коректним, зберегти його у БД.

5. Надати можливість ввести корені рівняння, під час введення перевіряти, чи є задане число коренем, і якщо так – зберігати його в БД.

6. Реалізувати функції пошуку рівнянь у БД за їхніми коренями. Наприклад, можливий запит: знайти всі рівняння, що мають один із зазначених коренів або знайти всі рівняння, які мають рівно один корінь.
   
7. Проект має бути реалізований з використанням системи збирання Maven в одному із середовищ розробки: IntelliJ IDEA або Eclipse.

8. Проект має бути завантажений у репозиторій GitHub та надано посилання для його отримання. Також допустимо надіслати архів із проектом.

## Test Coverage

The project includes comprehensive testing to ensure its functionality and reliability. The test coverage for the project is shown in the image below:

![image](https://github.com/VovaMitsura/geeks-course-test/assets/95585344/e5a7d28f-b07b-48a5-bed1-aa1bf5fcde8d)

Ensure that you maintain high test coverage to catch and prevent potential issues in the application.


## Notes

- The project should be developed using Java 11 or 17.
- It's recommended to use databases such as MySQL, MariaDB, or PostgreSQL.
- Using JUnit and other testing tools for testing is encouraged.
- File storage can be used instead of databases for storing information about entered and edited equations.
- A number is considered a root of an equation if substituting that number for all occurrences of 'x' in the equation results in a difference between the values of the left and right sides of the equation not exceeding 10^-9.


### How to Run manually

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
