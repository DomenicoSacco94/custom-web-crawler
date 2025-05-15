# CUSTOM WEB CRAWLER
This repo contains a web crawler which scans the web pages starting from some seed URLs, looking for occurrences about a given Topic (expressed in a collection of regexps).
Once some occurrences are found, some facts are extrapolated on them using Spring AI.

## Purpose of the Repo
Good question... I wanted to create a POC to check if LLMs could be leveraged to quickly browse through high-volume information sources (Social medias, news feeds, ...)
and extrapolate only given important facts. 
In a time when LLMs will produce a humungous amount of noise and duplicated information on the web, 
I wondered how effectively they could be used as their own antidote: extrapolate only the facts that are relevant at a given moment from huge amounts of text.

## Implementation choices

Through this POC I wanted to get acquainted with the following technologies:
- [Spring AI](https://spring.io/projects/spring-ai), particularly its integration with chat-like LLMs.
- [Kafka](https://kafka.apache.org/), used here to handle the asynchronous communication.
- [TestContainers](https://testcontainers.com/), used to create comprehensive (and complicated) integration tests.

If you are interested in checking in more detail the decision process and design choices that I made during the implementation,
please refer to the Architectural Decision Records located at `/docs/adr`.

## How to run the application locally
To spin up the Spring Boot App and the Postgres DB, please run the following command:

```sh
docker-compose up --build
```

Once the Docker containers are running, the Swagger can be consulted at this link:

http://localhost:8080/swagger-ui/index.html

## Example of usage

Say you would like to scan the Google News feed for relevant facts about a given industry sector.

First, we need to create a Topic with some Regexp representing it:

**POST /v1/topics**

```
{
    "name": "Sector name",
    "description": "An important industry sector",
    "regexps": [
        {
            "pattern": "(?i)\\bCompanyA\\b",
            "description": "Company A"
        },
        {
            "pattern": "(?i)\\bCompanyB\\b",
            "description": "Company A"
        }
    ]
}
```

Then we can trigger the scan of the corresponding news Feed endpoint:

**POST /v1/scan**

```
{
    "topicId": 1,
    "urls": [
        "http://news.google.com/rss/search?q=sector%20name&hl=en&gl=US&ceid=US:en"
    ]
}
```

The DB will then start getting filled of facts about the chosen industry sector, which are accessible through the endpoint **GET /v1/facts**.

DISCLAIMER: Depending on the Ollama model used, the results might be false, irrelevant and/or hallucinated.
There is also the strong tendency to the extrapolated facts being duplicated. 
An improvement of this crawler would be to re-use the LLM on the result collection itself and remove the duplicated facts, compiling a holistic report.

## Running tests locally
The tests can be run using the Gradle wrapper:

```sh
./gradlew test
```

This command can be used to generate the testing reports:

```sh
./gradlew test jacocoTestReport
```

An HTML summary of the test coverage is available at the path `build/reports/jacoco/test/html/index.html`.

## Test with exposed documents

In order to easily test the app, this custom script can be used:

```sh
./local-test.sh --serve-port <port-number>
```

The script will serve all the files in the `src/test/resources/testfiles` path and produce an url object like this one:

```
{
    "urls": [
        "http://192.168.2.105:34000/test_png.png",
        "http://192.168.2.105:34000/Testdata_Invoices.pdf",
    ]
}
```

Which can then be fed to the `/v1/scan` endpoint.



