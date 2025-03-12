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
PostgreSQL also has easily configurable indexes, which can help us optimize the query time in case the number of ibans becomes huge.
In this project, I assumed that readings are much more frequent than writings, as every time that a documents needs to be validated, all IBANs are fetched.
In production, it needs to be accounted that each index comes with additional space and writing time cost.
I also created a "projection" model of the iban table because, in case it will start containing a growing number of columns,
so that only the fields that are strictly needed for the validations have to be queried.

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
The purpose of Kafka in this project is to enable asynchronous communication when scanning a significant number of documents.
The `document-scan-topic` is used to manage the document asynchronous download and a `DocumentValidationMessage` is then created as a result of the scan.
The `DocumentValidationMessage` can then be sent to another topic and consumed elsewhere, for instance a webhook can be implemented to send the scan results to the client when they are ready.
Since in this project there is only one producer, one topic and one consumer, I decided not to use Zookeeper and use Kafka in KRaft mode.

## Design decisions

### Integration testing
In this application I decided to use some SQL scripts, since I needed some test data stored in the DB for the integration test.
I went for this approach because there is only one persisted table at the moment and there are no complex FKs and DB relations to manage.
For a demonstration purposes, I decided to create one single integration test.

## Validation
In order to keep the application structure flexible and easily apply new validations in the future, 
I decided to use the Spring `Validator` so that the validation logic can be easily expanded and changed.

## Endpoints
During the development of this small project, I proceeded by the following iterations:
1. Scan an uploaded document
2. Scan a document served by a url
3. Scan multiple documents coming from different urls asynchronously

I decided to keep the endpoints I created for each iteration, because it makes the microservice more flexible:
- Some clients might not be able to serve a document from an url and just want to send the file to have it scanned.
- Some clients might want a synchronous validation because they would otherwise require the setup of an asynchronous communication channel. 