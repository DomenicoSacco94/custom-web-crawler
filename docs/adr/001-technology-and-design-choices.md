# Technology and Design Choices

## Chosen tools

### Java 17
Java 17 is an LTS version that has been supported for a long time (since September 2021)
I used it because I would like to leverage on all the optimizations implemented after the older java versions, including:
1. [The JVM improvements involving container support which occurred in Java 12.](https://www.happycoders.eu/java/java-12-features/)
2. [The JVM memory improvements performed on Java 17](https://codezup.com/java-17-new-features-enhanced-performance/).

I could also have built the project with the newest Java LTS such as 21, but I feel a bit more experienced using Java 17.

### PostgreSQL
I decided to use PostgreSQL because I often worked with it previously. 
PostgreSQL also has easily configurable indexes, which can help us optimize the query times.
In this project, I assumed that readings of the regexps are much more frequent than writings, as every time that a web page will be parsed, all the regexps for a relevant topic are going to be fetched.
In production contexts, it needs to be accounted that each index comes with additional space and writing time cost.
I also created a "projection" model of the regexp table, in case it will start containing a growing number of columns,
so that only the regexp fields that are strictly needed will be queried.

### Flyway
In a production application, performing data migrations is a challenging endeavour.
I used this migration tool in the past (and also Liquibase) to properly keep track of the DB history and to easily perform incremental changes to the DB structure once that the application already holds production data.

### Gradle
In this project I chose Gradle over Maven for these main reasons:
- Flexibility: The syntax of Gradle makes it easier to implement custom tasks, like when I need to exclude Lombok generated code from the test coverage computation.
- Readability: In my opinion Gradle is easier to read than Maven’s XML configurations.

### Lombok
I used lombok in order to reduce the boilerplate code; this came at the cost of additional complexity in excluding the Lombok
generated code from the tests. In order to do this, I am using the `afterEvaluate` lifecycle hook in the `jacocoTestReport` task 
defined in the `build.gradle`.

## Kafka
The purpose of Kafka in this project is to enable and manage the asynchronous communication.
The asynchronous communication takes place for 2 operations in particular:
- Download of the web pages (I/O bounded)
- Usage of the chosen Ollama modle through Spring AI (CPU bounded)
Since in this project there are only 2 topics to be managed, I decided not to use Zookeeper and use Kafka in KRaft mode instead.

## Design decisions

### Integration testing
In this application I decided to use some SQL scripts, since I needed some test data stored in the DB for the integration test.
For a demonstration purposes, I decided to create one single integration test.
Creating an integration test that spins 3 containers at the same time (Kafka, PostgreSQL and Ollama) was challenging and I did it mostly for
demonstrative purpose (and as a personal challenge). DO NOT TRY THIS AT HOME :-D

## Validation
In order to keep the application structure flexible and easily apply new validations in the future, 
I decided to use the Spring `Validator` so that the validation logic can be easily expanded and changed.
Its current only application is checking that the Regexp are actually valid Regexps before saving them.

## Endpoints
During the development of this small project, I proceeded by the following iterations:
1. Scan a pdf document or a web page manually uploaded
2. Scan a single web page served by an external url
3. Scan multiple URLs coming asynchronously

I decided to keep only the last endpoint I implemented, because it makes the microservice much easier to maintain.