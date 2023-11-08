# Booking

This application is used for booking hotel rooms. It is a simple application that allows you to create, update, delete and view reservations. It also allows you to view the list of reservations.

## Development

Before you can build/run this project, you must install and configure the following on your machine:

1. [Docker](https://docs.docker.com/get-docker/): We use Docker to run the application, database and its dependencies in a containerized environment to avoid version conflicts and make it easier to run the application on different machines.
2. [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html): The application is built using Java 17. You can download and install it from the link provided.
3. [Maven](https://maven.apache.org/download.cgi): We use Maven to build the application. You can download and install it from the link provided.


## Testing

### Spring Boot tests

To launch your application's tests, run:

```mvn verify```

## Building

To build the application, run:

```mvn clean install```

This will create a jar file in the target folder which you can run using the command:

```java -jar target/booking-0.0.1-SNAPSHOT.jar```

### Using Docker to simplify development 

To run the application and its dependencies in a containerized environment, run:

```docker build -t booking .```

This will create a docker image with the name `booking` which you can run using the command:

```docker run -it -p 8080:8080 booking```

This will run the application on port 8080. You can access the application on http://localhost:8080 once it is running.

## Database

The application uses an in-memory database (H2) which is created when the application starts and destroyed when the application stops. 
This means that any data you add to the database will be lost when you stop the application. 

### Sample Data

The application uses a csv files to populate the database with sample data when the application starts. This data is found in the following directory:

[src/main/resources/liquibase/data](src/main/resources/liquibase/data)

Once you have the application running, we have provided some sample requests you can use to test the application. These can be found in the following directory:

[src/test/resources/requests/reservation-requests.http](src/test/resources/requests/reservation-requests.http)

